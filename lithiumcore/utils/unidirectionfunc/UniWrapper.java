package org.firstinspires.ftc.teamcode.lithiumcore.utils.unidirectionfunc;

import org.firstinspires.ftc.teamcode.lithiumcore.utils.mathfunction.MathFunction;

//This is a conversion wrapper for non-unidirectional functions to become unidirectional.
//This class is immutable.
public class UniWrapper implements UniDirectionalFunc {
  public final double endXVal;
  public final double lastYValue;
  public final MathFunction graphFunc;
  public UniWrapper(final double endXVal, final MathFunction graphFunc) {
    this.endXVal = endXVal;
    this.graphFunc = graphFunc;

    this.lastYValue = graphFunc.yValueOf(endXVal);
  }
  public UniWrapper(final double endXVal, final double lastYValue, final MathFunction graphFunc) {
    this.endXVal = endXVal;
    this.lastYValue = lastYValue;
    this.graphFunc = graphFunc;
  }
  @Override
  public double getEndThreshold() {
    return endXVal;
  }
  @Override
  public double getLastYValue() {
    return lastYValue;
  }
  @Override
  public double yValueOf(final double x) {
    return graphFunc.yValueOf(x);
  }
  /*public static UniWrapper getUniWrapper(final double endXVal, final MathFunction graphFunc) {
    return new UniWrapper(endXVal, graphFunc);
  }*/
}