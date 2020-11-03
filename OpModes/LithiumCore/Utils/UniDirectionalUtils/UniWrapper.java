package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.UniDirectionalUtils;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.MathFunctions.*;

//This is a conversion wrapper for non-unidirectional functions to become unidirectional.
//This class is immutable.
public class UniWrapper implements UniDirectionalFunc {
  public final double endXVal;
  public final MathFunction graphFunc;
  public UniWrapper(final double endXVal, final MathFunction graphFunc) {
    this.endXVal = endXVal;
    this.graphFunc = graphFunc;
  }
  @Override
  public double getEndThreshold() {
    return endXVal;
  }
  @Override
  public double yValueOf(final double x) {
    return graphFunc.yValueOf(x);
  }
  public static UniWrapper getUniWrapper(final double endXVal, final MathFunction graphFunc) {
    return new UniWrapper(endXVal, graphFunc);
  }
}