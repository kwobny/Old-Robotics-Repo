package org.firstinspires.ftc.teamcode.OpModes.LithiumCore;

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
public class SubSCurve extends SigmoidFunc {

  //The y height and its complement that the function domain cuts off at, relative to the maximum height (when the max height is 1).
  private static final double Y_CUTOFF = 0.01;
  
  public SubSCurve() {
    double L;
    double k;
    double x0;

    setParams(L, k, x0);
  }

}