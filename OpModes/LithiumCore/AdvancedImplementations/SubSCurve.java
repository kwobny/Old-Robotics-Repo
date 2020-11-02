package org.firstinspires.ftc.teamcode.OpModes.LithiumCore;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.SCS_Package.UniDirectionalFunc;

//There are 2 constants that you can change:
//L, and k.

//There are 3 things that determine these 2 constants:
//Peak acceleration (peak jerk is dependent on this value)
//Change in output (L)
//Amount of x-axis (domain)

//The top three things are the important factors. You need to provide at least 2 of them into the function.

//This function is used as the velocity vs. time graph for a curve that transitions from 1 velocity to another so that acceleration at both points is 0.
//This class is immutable.
public class SubSCurve extends SigmoidFunc implements UniDirectionalFunc {

  //The y height and its complement that the function domain cuts off at, relative to the maximum height (when the max height is 1).
  private static final double Y_CUTOFF = 0.01;
  //internal processing convenience variable
  private static final double cutoffLogVal = Math.log((1-Y_CUTOFF)/Y_CUTOFF);

  private final double initialYValue;
  
  //change in output can be positive or negative.
  //domain length has to always be positive.
  //peak acceleration is almost always positive. Becomes positive or negative to fit the direction that change in output goes in. However, if the change in output is missing, then peak acceleration can be positive or negative. In this case, the sign determines the direction that the change in output goes in.

  //At least 2 arguments have to be provided. If only 2 are provided, they can be any values possible, as long as they fit criteria above.
  //If all 3 arguments are provided, the function does not check them and assumes that all 3 are valid. Make sure to check this.
  public SubSCurve(Double changeInOutput, Double domainLength, Double peakAccel, double initialYValue) {
    //set initial y value
    this.initialYValue = initialYValue;

    double k;

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
        x0 = domainLength / 2.0;
        k = cutoffLogVal / x0;
        L = (peakAccel / k) * 4.0;
      }
      else if (domainLength == null) {
        L = changeInOutput;
        k = Math.abs(peakAccel / L) * 4.0;
        x0 = cutoffLogVal / k;
      }
      //peak acceleration is null
      else {
        L = changeInOutput;
        x0 = domainLength / 2.0;
        k = cutoffLogVal / x0;
      }
    }
    //no missing arguments, function assumes that all arguments are valid (does no checking).
    else {
      L = changeInOutput;
      k = peakAccel * 4.0;
      x0 = domainLength / 2.0;
    }

    setK(k);
  }

  @Override
  public double getEndThreshold() {
    return x0 * 2;
  }

  @Override
  public double yValueOf(final double x) {
    return super.yValueOf(x) + initialYValue;
  }

}