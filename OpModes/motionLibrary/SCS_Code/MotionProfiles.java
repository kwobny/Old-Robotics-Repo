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
    
    public double changeInVelocity;
    public double changeInTime;
    public double jerk;
    public double maxAccel;

    private double initialVelocity;
    private double jerkTime; //the time spent on constant jerk mode
    private double accelTime; //the time spent on the constant acceleration part
    private double accelStartVelocity;
    private double lastStartVelocity;

    private SCSOpUnit operation;
    private WaitTask waitTask = new WaitTask();
    

    public SubSCurve(final OutputSink output) {
      operation = new SCSOpUnit(main.time, output, null);
      operation.graphFunc = new CommonOps.ConstJerk(jerk, 0, initialVelocity);
      waitTask.endTaskAfter = false;
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

          waitTask.condition.threshold = jerkTime + accelTime;
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

        waitTask.condition.threshold = 2 * jerkTime + accelTime;

        oper
      }
    };

  }

}