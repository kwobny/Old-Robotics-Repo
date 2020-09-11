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

    private SCSOpUnit operation = new SCSOpUnit();
    

    public SubSCurve(final OutputSink output) {
      operation.input = main.time;
      operation.output = output;
    }

    public void start() {
      //
    }

  }

}