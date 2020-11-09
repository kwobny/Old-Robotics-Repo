package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.MathFunctions;

public abstract class Transformation implements MathFunction {

  //default access modifiers used instead of private to make sure that they can be overridden by subclass

  public MathFunction next;
  
  protected double alteredX(final double xValue) {
    return xValue;
  }
  protected double alteredY(final double yValue) {
    return yValue;
  }

  @Override
  public final double yValueOf(final double input) {
    return alteredY(next.yValueOf(alteredX(input)));
  }

}