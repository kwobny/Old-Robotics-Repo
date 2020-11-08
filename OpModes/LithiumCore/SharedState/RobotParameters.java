package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.SharedState;

public abstract class RobotParameters extends ConstantsBaseClass {
  
  //CONSTANTS

  public double robotWidth; //width of robot in centimeters. Is the width between back 2 wheels
  public double robotLength; //length of robot in centimeters. Is the length of the robot between 1 front wheel and 1 back wheel
  //Both distances are the distances between the points of contact between the wheels and ground.
  public int ticksPerRevolution; //number of ticks in each revolution of the motor.
  public double wheelDiameter; //wheel diameter in centimeters

  //AUTO DISCERNED PARAMETERS

  public double robotDiagonalLen;
  public double distancePerTick; //how many centimeters wheel runs per wheel tick

  public RobotParameters() {
    if (robotDiagonalLen != 0.0)
      robotDiagonalLen = Math.sqrt(robotWidth*robotWidth + robotLength*robotLength);
    if (distancePerTick != 0.0)
      distancePerTick = (Math.PI * wheelDiameter)/ticksPerRevolution;
  }

  @Override
  protected abstract void _initialize();

}