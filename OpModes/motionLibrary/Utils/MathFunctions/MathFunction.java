package org.firstinspires.ftc.teamcode.OpModes.motionLibrary.Utils.MathFunctions;

//This abstract class is the one which represents a mathematical function (linear, quadratic, sine, etc.) that takes a numerical input and returns a numerical output

//normally the class would contain a constructor which initializes the data needed for the function itself.

public interface MathFunction {
  
  public double yValueOf(double input);

}