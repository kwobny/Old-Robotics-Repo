package org.firstinspires.ftc.teamcode.OpModes.LithiumCore;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.SCS_Package.SeqOpFunc;

//This is a double s curve. It includes two sub s curve sections separated by an arbitrary constant velocity section.
public class DoubleSCurve extends SeqOpFunc {

  //Parameters:
  //Max s curve acceleration or time on s curves (supply either one, or both)
  //Max output deviation
  //time on constant velocity
  public DoubleSCurve(Double peakAccel, Double timeOnSCurve, double maxOutputDeviation, double timeOnConstVelocity) {
    //
  }

}