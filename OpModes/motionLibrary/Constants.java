public class Constants {
  private Constants() {}

  public static final double robotWidth = 5; //width of robot in centimeters. Is the width between back 2 wheels
  public static final double robotLength = 5; //length of robot in centimeters. Is the length of the robot between 1 front wheel and 1 back wheel
  //Both distances are the distances between the points of contact between the wheels and ground.
  public static final double robotDiagonalLen = Math.sqrt(Math.pow(robotWidth, 2) + Math.pow(robotLength, 2));
  public static final int ticksPerRevolution = 2600; //number of ticks in each revolution of the motor.
  public static final double wheelDiameter = 3; //wheel diameter in centimeters
  public static final double distancePerTick = (Math.PI*wheelDiameter)/ticksPerRevolution; //how many centimeters wheel runs per wheel tick
  public static final double totalAngleMeasure = 360; //the number of angle units in a full circle
}