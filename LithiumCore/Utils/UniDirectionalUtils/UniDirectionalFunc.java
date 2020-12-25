package LithiumCore.Utils.UniDirectionalUtils;

import LithiumCore.Utils.MathFunctions.MathFunction;

//This function is one where the x value / input converges towards a single location/direction. The function starts at x = 0 and goes towards the end threshold (typically positive but can be negative).
//These functions require a get end threshold function and a last y value function.
public interface UniDirectionalFunc extends MathFunction {
  
  //This method might be a costly operation, so it is great to store the value as much as possible. The return value of this function should not change.
  //This method returns the x value where the function ends.
  public double getEndThreshold();

  //This function has the same characteristics and limitations as one above
  //This function gets the y value at the right most point of the domain on the graph (typically at the threshold x value).
  public double getLastYValue();

  //default implementation:
  /*
  @Override
  public double getLastYValue() {
    return yValueOf(getEndThreshold());
  }
  */
  
}