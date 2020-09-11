package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

public class MotionProfiles {

  public SCS scs;

  public MotionProfiles(final SCS scs) {
    this.scs = scs;
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

    public void start() {
      //
    }

  }

}