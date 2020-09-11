package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

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
    private double jerkTime;

    private SCSOpUnit operation;
    

    public SubSCurve(final OutputSink output) {
      operation = new SCSOpUnit(main.time, output, null);
    }

    public void start() {
      operation.graphFunc = new CommonOps.ConstJerk(jerk, 0, initialVelocity);
      scs.addOperation(operation);
      main.wait.setTimeout(new operation.InputCond(jerkTime, true), callback);
      
    }

  }

}