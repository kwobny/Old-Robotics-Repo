package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.UniDirectionalUtils;

//is a sequential operation function where y 0 on each graph is level with the last point on the previous graph.
//The funcs array cannot have null elements, cannot have a length of 0, and cannot be null.
//This class is immutable except when extended.
public class ContinuousSOF implements UniDirectionalFunc {
  
  //constructors
  protected ContinuousSOF() {
    //
  }
  public ContinuousSOF(final UniDirectionalFunc ...funcs) {
    setFuncs(funcs);
  }

  private UniDirectionalFunc[] funcs;
  private int index;
  private double y_reference;

  private double xTranslateVal;
  private double xThreshold;

  protected void setFuncs(final UniDirectionalFunc ...funcs) {
    this.funcs = funcs;
    reset();
  }

  public void reset() {
    y_reference = 0;
    index = 0;
    
    xTranslateVal = 0;
    xThreshold = funcs[0].getEndThreshold();
  }

  @Override
  public double yValueOf(final double x) {
    if (index >= funcs.length) {
      return y_reference;
    }
    if (x > xThreshold) {
      y_reference = funcs[index].yValueOf(xThreshold - xTranslateVal);
      ++index;

      xTranslateVal = xThreshold;
      if (index < funcs.length) {
        xThreshold += funcs[index].getEndThreshold();
      }
      else {
        return y_reference;
      }
    }
    return funcs[index].yValueOf(x - xTranslateVal) + y_reference;
  }

  @Override
  public double getEndThreshold() {
    double sum = 0;
    for (UniDirectionalFunc i : funcs) {
      sum += i.getEndThreshold();
    }
    return sum;
  }
}

public class ContinuousSOF