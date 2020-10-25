package org.firstinspires.ftc.teamcode.OpModes.LithiumCore;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.MathFunctions.*;

//This function is used as the velocity vs. time graph for a curve that transitions from 1 velocity to another so that acceleration at both points is 0.
//This class is immutable
public class SigmoidFunc implements MathFunction {

  private final double L; //curve's maximum value
  private final double x0; //midpoint of curve
  private final double kNeg; //negative of the logistic growth rate

  public SigmoidFunc() {
    //
  }

  @Override
  public double yValueOf(final double x) {
    return 3.0;
  }
}