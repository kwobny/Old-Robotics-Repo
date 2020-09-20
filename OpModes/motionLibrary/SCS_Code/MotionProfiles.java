package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.MathFunctions.*;

public class MotionProfiles {

  public Main main;
  public SCS scs;

  public MotionProfiles(final Main main) {
    this.main = main;
    this.scs = main.scs;
  }

  public class SCurveProfile {
    
    //

  }

  //this is the curve which is used to build the full S curve profile. This curve is immutable.
  public class SubSCurve {
    
    //public double changeInVelocity;
    //public double changeInTime;
    private final double jerk;
    private final double maxAccel;

    private final double initialVelocity;
    private final double jerkTime; //the time spent on constant jerk mode
    private final double accelTime; //the time spent on the constant acceleration part
    private final double accelStartVelocity;
    private final double lastStartVelocity;

    private final SCSOpUnit operation;
    private final WaitTask waitTask = new WaitTask();
    private final SCSOpUnit.InputCond condition;

    private final CommonTrans.Translate graphTransformation = new CommonTrans.Translate();
    private final CommonOps.ConstAccel constAccel;
    private final CommonOps.ConstJerk constJerk1 = new CommonOps.ConstJerk();
    private final CommonOps.ConstJerk constJerk2 = new CommonOps.ConstJerk();

    //These are the properties for detection of when the wait is done.
    //this denotes if the profile operation has completed running.
    private boolean isDone = false;
    public Callback opCallback;
    public final WaitCondition opCondition = new WaitCondition() {
      @Override
      public boolean pollCondition() {
        return isDone;
      }
    };

    public SubSCurve(final OutputSink output, final Callback opCallback, final double ...curveArgs) {
      //the argument list is jerk, highest acceleration, time spent on jerk, time spent on const accel, change in velocity in jerk, change in velocity on const accel.
      //if the time spent on const accel is 0.0, then it is a triangular acceleration profile.

      /*
      //finding the required constants
      this.jerk = jerk;
      this.maxAccel = maxAcceleration;
      this.initialVelocity = initialOutput;
      this.jerkTime = this.maxAccel/this.jerk;

      //edit this a little. Works for everything
      this.accelTime = (finalOutput - this.initialVelocity) - (1/this.jerk);

      this.accelStartVelocity = 1/2 * this.jerkTime * this.maxAccel;
      this.lastStartVelocity = this.accelStartVelocity + this.accelTime * this.maxAccel;
      */

      //setting up the actual operation
      this.opCallback = opCallback;
      operation = new SCSOpUnit(main.time, output, null);
      waitTask.autoEndTask = false;

      condition = new operation.InputCond();
      condition.isAbove = true;
      waitTask.condition = condition;

      operation.graphFunc = graphTransformation;

      //inputting in the required constants
      if (curveArgs[3] != 0.0) {
        constAccel = new CommonOps.ConstAccel();
      }
    }

    //this denotes if the profile operation is running.
    private boolean isActive = false;

    public void start() throws Exception {
      //make sure that the operation is not running before using it.
      if (isActive) {
        throw new Exception("You cannot start a motion profile which is already running");
      }
      isActive = true;
      isDone = false;

      //starts the first part of the operation (positive jerk)
      //operation.graphFunc = new CommonOps.ConstJerk(jerk, 0, initialVelocity);
      graphTransformation.next = constJerk;
      constJerk.jerk = jerk;
      constJerk.initialAcceleration = 0;
      constJerk.initialVelocity = initialVelocity;

      condition.threshold = jerkTime;
      waitTask.callback = callback1;

      scs.addOperation(operation);
      main.wait.setTimeout(waitTask);
      
    }

    private Callback callback1 = new Callback() {
      @Override
      public void run() {
        //second part of operation (constant acceleration)
        //if the time spent on acceleration is 0, then run alternative code
        if (accelTime != 0.0) {
          /*MathFunction constAccel = new CommonOps.ConstAccel(maxAccel, accelStartVelocity);
          constAccel = TransUtils.applyTrans(constAccel, new CommonTrans.Translate(jerkTime, 0.0));*/
          constAccel.acceleration = maxAccel;
          constAccel.initialVelocity = accelStartVelocity;
          graphTransformation.next = constAccel;
          graphTransformation.shiftX = jerkTime;

          condition.threshold += accelTime;
          waitTask.callback = callback2;
        }
        else {
          callback2.run();
        }
      }
    };

    private Callback callback2 = new Callback() {
      @Override
      public void run() {
        //last part of operation, constant jerk
        /*MathFunction constJerk = new CommonOps.ConstJerk(-jerk, maxAccel, lastStartVelocity);
        constJerk = TransUtils.applyTrans(constJerk, new CommonTrans.Translate(jerkTime + accelTime, 0.0));*/
        constJerk.jerk = -jerk;
        constJerk.initialAcceleration = maxAccel;
        constJerk.initialVelocity = lastStartVelocity;
        graphTransformation.next = constJerk;
        graphTransformation.shiftX += accelTime;

        condition.threshold += jerkTime;
        waitTask.callback = opCallback;
      }
    };

    private Callback endCallback = new Callback() {
      @Override
      public void run() {
        isDone = true;
        isActive = false;
        scs.removeOperation(operation);
        main.wait.removeTimeout(waitTask);

        if (opCallback != null)
          opCallback.run();
      }
    };

  }

  //this function returns a set of complete arguments for the sub scs curve when given a partial set of arguments.
  //arguments that are missing/not given are denoted with null.
  public static void getSubSCurveArgs(final Double changeInTime, final Double initialOutput, final Double finalOutput, final Double jerk, final Double maxAcceleration) {
    //finding input numbers if some parameters are missing

    //check to see if initial and final output are both missing.
    if (initialOutput == null && finalOutput == null) {
      throw new Exception("both the initial and final outputs are null. The function does not know where to place the graph. At least provide something like 0.0 for the initial output.");
    }

    //find how many inputs are missing
    int missing = 0;
    if (changeInTime == null)
      i++;
    if (initialOutput == null || finalOutput == null)
      i++;
    if (jerk == null)
      i++;
    if (maxAcceleration == null)
      i++;
    
    if (missing > 2)
      throw new Exception("There were more than 2 missing arguments out of 4.");
    
    else if (missing == 2) {

      if (maxAcceleration == null) {
        if (jerk == null) {
          //jerk
        }
        else if (changeInTime == null) {
          //change in time
        }
        else {
          //change in velocity
        }
      }
      else if (jerk == null) {
        //jerk and either time or velocity is missing
        throw new Exception("Change in time or change in velocity cannot be null if jerk is also null. Look at scs notes for explanation as to why.");
      }
      else {
        //time and velocity is missing, which is impossible
        throw new Exception("Change in time and change in velocity cannot be null together.");
      }

    }
    else if (missing == 1) {
      if (jerk == null) {
        //jerk
      }
      else if (changeInTime == null) {
        //change in time

        //for scenario 1
        //v = (dt/2)^2 * j
        //2 * sqrt(v/j) = dt

        //for scenario 2
        //v = amax * (dt - amax/j)
        //

        double dtDiv2 = Math.sqrt((finalOutput - initialOutput)/jerk);
        if (dtDiv2 * jerk < maxAcceleration) {
          changeInTime = dtDiv2 * 2;
        }
        else {
          if 
        }
      }
      else if (initialOutput == null || finalOutput == null) {
        //change in velocity
      }
      else {
        //max acceleration
      }
    }
    else {
      //nothing is missing, check to see that everything is valid and checks out
      /*
      ((1/2 * dt)^2 * j == dv)

      (amax/j) = tjerk
      (amax * (dt - amax/j) == dv)
      */

      if (changeInTime/2 * jerk < maxAcceleration) {
        if (!Constants.isEqual(changeInTime * changeInTime/4 * jerk, finalOutput - initialOutput)) {
          throw new Exception("the 4 arguments given were not valid with each other.");
        }
      }
      else if (!Constants.isEqual(maxAcceleration * (changeInTime - maxAcceleration/jerk), finalOutput - initialOutput)) {
        throw new Exception("the 4 arguments given were not valid with each other.");
      }
    }
  }

}