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

  //this is the curve which is used to build the full S curve profile.
  public class SubSCurve {
    
    //public double changeInVelocity;
    //public double changeInTime;
    private double jerk;
    private double maxAccel;

    private double initialVelocity;
    private double jerkTime; //the time spent on constant jerk mode
    private double accelTime = 0.0; //the time spent on the constant acceleration part
    private double accelStartVelocity;
    private double lastStartVelocity;

    private SCSOpUnit operation;
    private WaitTask waitTask = new WaitTask();

    //These are the properties for detection of when the wait is done.
    private boolean isDone = false;
    public Callback opCallback;
    private WaitCondition opCondition = new WaitCondition() {
      @Override
      public boolean pollCondition() {
        return isDone;
      }
    };

    public WaitCondition getOpCondition() {
      return opCondition;
    }
    

    public SubSCurve(final Double changeInTime, final Double initialOutput, final Double finalOutput, final Double jerk, final Double maxAcceleration, final OutputSink output, final Callback opCallback) throws Exception {
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

      //finding the required constants
      this.jerk = jerk;
      this.maxAccel = maxAcceleration;
      this.initialVelocity = initialOutput;
      this.jerkTime = this.maxAccel/this.jerk;

      //edit this a little. Works for everything
      this.accelTime = (finalOutput - this.initialVelocity) - (1/this.jerk);

      this.accelStartVelocity = 1/2 * this.jerkTime * this.maxAccel;
      this.lastStartVelocity = this.accelStartVelocity + this.accelTime * this.maxAccel;

      //setting up the actual operation
      operation = new SCSOpUnit(main.time, output, null);
      operation.graphFunc = new CommonOps.ConstJerk(jerk, 0, initialVelocity);
      operation.
      waitTask.endTaskAfter = false;
      this.opCallback = opCallback;
    }

    public void start() {
      //starts the first part of the operation (positive jerk)
      scs.addOperation(operation);
      waitTask.condition = new operation.InputCond(jerkTime, true);
      waitTask.callback = callback1;
      main.wait.setTimeout(waitTask);
      
    }

    private Callback callback1 = new Callback() {
      @Override
      public void run() {
        //second part of operation (constant acceleration)
        //if the time spent on acceleration is 0, then run alternative code
        if (accelTime != 0.0) {
          MathFunction constAccel = new CommonOps.ConstAccel(maxAccel, accelStartVelocity);
          constAccel = TransUtils.applyTrans(constAccel, new CommonTrans.Translate(jerkTime, 0.0));

          ((SCSOpUnit.InputCond) waitTask.condition).threshold = jerkTime + accelTime;
          waitTask.callback = callback2;

          operation.graphFunc = constAccel;
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
        MathFunction constJerk = new CommonOps.ConstJerk(-jerk, maxAccel, lastStartVelocity);
        constJerk = TransUtils.applyTrans(constJerk, new CommonTrans.Translate(jerkTime + accelTime, 0.0));

        ((SCSOpUnit.InputCond) waitTask.condition).threshold = 2 * jerkTime + accelTime;
        waitTask.callback = opCallback;

        operation.graphFunc = constJerk;
      }
    };

    private Callback endCallback = new Callback() {
      @Override
      public void run() {
        isDone = true;
        scs.removeOperation(operation);
        waitTask.endTaskAfter = true;

        opCallback.run();
      }
    };

  }

}