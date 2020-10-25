package org.firstinspires.ftc.teamcode.OpModes.LithiumCore;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.MathFunctions.*;

//This function is used as the velocity vs. time graph for a curve that transitions from 1 velocity to another so that acceleration at both points is 0.
//This class is immutable
//This class can be extended. Only subclasses have access to a bare constructor. This is so that the subclass can set function properties in somewhere other than the first line. The subclass has to call setParams sometime.
public class SigmoidFunc implements MathFunction {

  private double L; //curve's maximum value
  private double x0; //midpoint of curve
  private double kNeg; //negative of the logistic growth rate

  public SigmoidFunc(final double L, final double x0, final double k) {
    setParams(L, x0, k);
  }
  protected SigmoidFunc() {
    //
  }
  protected void setParams(final double L, final double x0, final double k) {
    this.L = L;
    this.x0 = x0;
    this.kNeg = -k;
  }

  @Override
  public double yValueOf(final double x) {
    return L/(1 + Math.exp(kNeg * (x - x0)));
  }
}