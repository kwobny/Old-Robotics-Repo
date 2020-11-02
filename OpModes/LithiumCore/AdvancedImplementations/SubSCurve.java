package org.firstinspires.ftc.teamcode.OpModes.LithiumCore;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.SCS_Package.UniDirectionalFunc;

//There are 2 constants that you can change:
//L, and k.

//There are 4 things that determine these 2 constants:
//Peak acceleration or peak jerk (both are exclusively dependent on each other)
//Change in output (L)
//Amount of x-axis (domain)
//y-height where function stops. This will be a  predetermined constant/value that cannot be changed by user. Will be L * constant

//The top three things are the important factors. You need to provide at least 2 of them into the function.

//This function is used as the velocity vs. time graph for a curve that transitions from 1 velocity to another so that acceleration at both points is 0.
//This class is immutable.
public class SubSCurve extends SigmoidFunc implements UniDirectionalFunc {

  //The y height and its complement that the function domain cuts off at, relative to the maximum height (when the max height is 1).
  private static final double Y_CUTOFF = 0.01;

  private final double initialYValue;
  
  public SubSCurve(Double changeInOutput, Double domainLength, Double peakAccel, double initialYValue) {
    //set initial y value
    this.initialYValue = initialYValue;

    double L;
    double k;
    double x0;

    //find out how many arguments are missing
    int missingElems = 0;
    if (changeInOutput == null) {
      ++missingElems;
    }
    if (domainLength == null) {
      ++missingElems;
    }
    if (peakAccel == null) {
      ++missingElems;
    }

    //if there are more than 1 missing argument to solve for, raise an error.
    if (missingElems > 1)
      throw new RuntimeException("There is more than 1 missing argument to the sub s curve constructor. There can only be 1 missing argument.");
    
    //if there is 1 missing argument, solve for it.
    if (missingElems == 1) {
      if (changeInOutput == null) {
        //
      }
      else if (domainLength == null) {
        //
      }
    }

    //put arguments into parameter variables (L, k, x0)
    L = changeInOutput;
    k = 2 * Math.log((1-Y_CUTOFF)/Y_CUTOFF) / domainLength;
    x0 = domainLength / 2;

    setParams(L, k, x0);
  }

  @Override
  public double getEndThreshold() {
    //
  }

  @Override
  public double yValueOf(final double x) {
    return super.yValueOf(x) + initialYValue;
  }

}