package org.firstinspires.ftc.teamcode.OpModes.LithiumCore;

import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.SCS_Package.*;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Wait_Package.*;

public class RPS {
  //NOTES
  /*
  1. When setting state, you need to explicitly call saveCurrentPosition, or have the second parameter of the first call to state setting function be true.
  2. When you are measuring multiple quantities, it is helpful to call coreDistFunc explicitly, so that it is only called once and extra computation time is not wasted.
  3: All functions that deal with translative motion are measured in whatever unit that the distance per tick constant is defined in (cm probably). All functions that deal with rotative motion are measured in the units defined in the total angle measure constant (degrees for now)
  */

  //RESOURCE OBJECTS
  private MadHardware mhw;
  private Move moveObj;

  RPS() {} //cannot be instantiated outside of package

  public void initialize(MadHardware hmw, Move obj) {
    mhw = hmw;
    moveObj = obj;
  }

  //robot position and distance tracking system. All distance values in centimeters

  //displacement and distance variables
  private double[] syncDisplacement = {0.0, 0.0}; //0 index is x, 1 is y
  private double[] syncDistance = new double[3]; //order is total distance, x distance, and y distance. positive x and y are in the right and up direction on the coordinate plane
  private double syncAngle = 0.0; // in degrees, positive is clockwise, negative is counterclockwise. a direction of 0 indicates robot is facing upwards direction on coordinate plane

  //wheel position variables
  //left front, right front, left back, right back
  private int[] lastWheelPos = new int[4];
  private double[] wheelPosChange = new double[4];

  //get displacement from last sync position
  public double[] coreDistFunc() {
    //get change in wheel position
    wheelPosChange[0] = mhw.leftFront.getCurrentPosition() - lastWheelPos[0];
    wheelPosChange[1] = mhw.rightFront.getCurrentPosition() - lastWheelPos[1];
    wheelPosChange[2] = mhw.leftBack.getCurrentPosition() - lastWheelPos[2];
    wheelPosChange[3] = mhw.rightBack.getCurrentPosition() - lastWheelPos[3];

    for (int i = 0; i < 4; i++) {
      wheelPosChange[i] *= Constants.distancePerTick;
    }

    //isolate components
    //a is value of LF and RB wheels, b is the value of RF and LB wheels, and r is the rotational value, with a positive value going clockwise
    double a; double b; double r;
    a = (wheelPosChange[0] + wheelPosChange[3])/2.0;
    b = (wheelPosChange[1] + wheelPosChange[2])/2.0;
    double aR = wheelPosChange[0] - a;
    double bR = wheelPosChange[2] - b;
    r = (aR + bR)/2.0;

    //determine dx, dy, and dAngle
    double dx = (a - b)/2.0;
    double dy = (a + b)/2.0;
    double dHyp = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    double dAngle = (2 * r / Constants.robotWidth) * (180.0/Math.PI);

    return new double[]{dx, dy, dHyp, dAngle};
  }

  //get the current angle orientation of the robot relative to the origin angle
  //positive is clockwise relative to angle, negative is counterclockwise
  public double getAngle(double[] varArray) {
    return syncAngle + varArray[3];
  }
  public double getAngle() {
    return syncAngle + coreDistFunc()[3];
  }

  //sets the robot angle at the current heading.
  //for everytime state variables are changed, save current position has to be called once and once only. the second parameter is a safeguard to make sure that happens.
  public void setAngle(final double angle, final boolean saveState) {
    if (saveState) saveCurrentPosition();
    syncAngle = angle % Constants.totalAngleMeasure;
  }
  //this sets the current angle to its lowest equivalent. Should be called every so often to avoid angle overflow.
  public void setAToLE(final boolean saveState) {
    if (saveState) saveCurrentPosition();
    syncAngle = syncAngle % Constants.totalAngleMeasure;
  }

  //this function returns an array with the total distance (not displacement) traveled by the robot. index 0 is total distance, 1 is x distance, 2 is y distance
  //x and y is relative to robot itself
  public double[] getDistanceTraveled(double[] displArray) {
    double totalDistance = syncDistance[0] + displArray[2];
    double xDistance = syncDistance[1] + Math.abs(displArray[0]);
    double yDistance = syncDistance[2] + Math.abs(displArray[1]);
    
    return new double[] {totalDistance, xDistance, yDistance};
  }
  public double[] getDistanceTraveled() {
    return getDistanceTraveled(coreDistFunc());
  }

  //this function resets the total distance traveled to 0 centimeters.
  public void resetDistance(boolean saveState) {
    if (saveState) saveCurrentPosition();
    for (int i = 0; i < 3; i++) {
      syncDistance[i] = 0.0;
    }
  }

  //these functions get the position of the robot relative to the reference point/origin
  //positive x and y quadrant is top right, like in normal graphs
  //return value array order is x, then y (in cm)
  private double angleSum = 0.0;
  private double[] tempDisplacement = new double[2];
  public double[] getPosition(double[] displArray) {
    double originalAngle = syncAngle + Math.atan2(displArray[0], displArray[1]) * (180/Math.PI);
  }
  public double[] getPosition() {
    return getPosition(coreDistFunc());
  }
  //this function gets the robot's position relative to a certain point, which itself is relative to the reference point/origin.
  //x and y signify a point, relative to current reference point
  public double[] getRelPosition(double x, double y) {
    double[] temp = getPosition();
    temp[0] -= x;
    temp[1] -= y;
    return temp;
  }

  //This function sets the robot's current position.
  public void setPosition(final double x, final double y, final boolean saveState) {
    if (saveState) saveCurrentPosition();
    syncDisplacement[0] = x;
    syncDisplacement[1] = y;
  }
  public void setXPos(final double x, final boolean saveState) {
    if (saveState) saveCurrentPosition();
    syncDisplacement[0] = x;
  }
  public void setYPos(final double y, final boolean saveState) {
    if (saveState) saveCurrentPosition();
    syncDisplacement[0] = y;
  }

  //This function shifts the position/changes it
  //if the shiftRefPoint flag is set, then this function acts as a shift reference point function instead.
  public void shiftPosition(double x, double y, boolean shiftRefPoint, boolean saveState) {
    if (saveState) saveCurrentPosition();
    if (shiftRefPoint) {
      x *= -1.0;
      y *= -1.0;
    }
    syncDisplacement[0] += x;
    syncDisplacement[1] += y;
  }

  //execute distance and displacement save function:
  //Run this function on every upload to motors command or at every number of ticks traveled.
  //Also run whenever you are setting the position parameters
  //This function establishes a vertex in the path traveled (the robot path is made of many lines joined together. A vertex forms when the robot's translation direction changes (logically). This function must be called every time that happens)
  public void saveCurrentPosition() {
    //add dx and dy to displacement and distance
    double[] tempArray = coreDistFunc();
    syncDisplacement = getPosition(tempArray);
    syncDistance = getDistanceTraveled(tempArray);
    syncAngle = getAngle(tempArray);

    //set last wheel ticks to the current one.
    lastWheelPos[0] = mhw.leftFront.getCurrentPosition();
    lastWheelPos[1] = mhw.rightFront.getCurrentPosition();
    lastWheelPos[2] = mhw.leftBack.getCurrentPosition();
    lastWheelPos[3] = mhw.rightBack.getCurrentPosition();
  }


  //Now, for the classes and methods that make this compatible with other classes (mainly SCS)

  public InputSource AngleInput = new InputSource() {

    @Override
    public double get() {
      return getAngle();
    }

  };

  public InputSource DistanceInput = new InputSource() {

    @Override
    public double get() {
      return getDistanceTraveled()[0];
    }

  };

  private class PositionInputClass implements InputSource {

    private final int coordinateNum;
    PositionInput(final int coordinateNum) {
      this.coordinateNum = coordinateNum;
    }
    
    @Override
    public double get() {
      return getPosition()[coordinateNum];
    }

  }

  public InputSource PosInputX = new PositionInputClass(0);
  public InputSource PosInputY = new PositionInputClass(1);


  public class DistanceWait implements WaitCondition {

    private double target_distance;

    //parameter list:
    //1. distance in cm (double).
    //2. flag if measuring change in distance (true) or just plain distance (false)
    public DistanceWait(final double distance, final boolean measure_mode) {
      if (measure_mode) {
        target_distance = distance + getDistanceTraveled()[0];
      }
      else {
        target_distance = distance;
      }
    }
    //in this version, the measure mode is true, which means that it measures change in distance
    public DistanceWait(final double distance) {
      this(distance, true);
    }

    @override
    public boolean pollCondition() {
      return getDistanceTraveled()[0] > target_distance;
    }
  }

  //measuring change in distance
  public DistanceWait getDistWait(final double distance) {
    return new DistanceWait(distance);
  }

}

/*
//WAIT FOR DISPLACEMENT/UNTIL AT POSITION
//parameter list:
//1. x-position/displacement (in cm, double)
//2. y-position/displacement (in cm, double)
//3. flag if waiting until at position (false) or waiting until displacement traveled (true)
//4 & 5. Optional parameters. Are angle measures which indicate what sector surrounding the target position/displacement the robot has to be in for the condition to be satisfied. The sector starts at the first parameter and ends at the second parameter. Default is a sector with its bisector being parallel to the line connecting the robot position to target, its size being predefined in the constants.

//Three things needed to be done:
//1. work out the total angle unit situation (if you are using boolean to represent if using radians or some other unit, maybe have a unit to radians conversion factor constant)
//2. work out how displacement is going to work
//3. work out how wait for angle is going to work.
private Object[] generateDisplacementData(Object[] args) {
  Object quadrantX, quadrantY;
  double[] pos = main.rps.getPosition();
  if (args[2]) {
    if (args.length == 3) {
      quadrantX = (Double) args[0] > 0.0;
      quadrantY = (Double) args[1] > 0.0;
    }
    else {
      quadrantX = args[3];
      quardantY = args[4];
    }
    return new Object[]{(Double) args[0] + pos[0], (Double) args[1] + pos[1], quadrantX, quadrantY};
  }
  else {
    if (args.length == 3) {
      quadrantX = (Double) args[0] > pos[0];
      quadrantY = (Double) args[1] > pos[1];
    }
    else {
      quadrantX = args[3];
      quardantY = args[4];
    }
  }
}
*/