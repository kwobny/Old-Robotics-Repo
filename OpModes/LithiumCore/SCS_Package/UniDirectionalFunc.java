package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.SCS_Package;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.MathFunctions.*;

//This function is one where the x value / input converges towards a single location/direction. The function starts at x = 0 and goes towards the end threshold (typically positive but can be negative).
//These functions require a get end threshold function.
public interface UniDirectionalFunc extends MathFunction {
  
  public double getEndThreshold();
  
}