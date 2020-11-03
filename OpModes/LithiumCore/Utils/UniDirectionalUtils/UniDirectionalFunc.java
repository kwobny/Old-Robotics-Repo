package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.UniDirectionalUtils;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.MathFunctions.*;

//This function is one where the x value / input converges towards a single location/direction. The function starts at x = 0 and goes towards the end threshold (typically positive but can be negative).
//These functions require a get end threshold function.
public interface UniDirectionalFunc extends MathFunction {
  
  //This method might be a costly operation, so it is great to store the value as much as possible. The return value of this function should not change.
  //This method returns the x value where the function ends.
  public double getEndThreshold();
  
}