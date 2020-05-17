package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;

public class RPS {
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
  private double[] syncDistance = new double[3]; //order is total distance, x distance, and y distance
  private double syncAngle = 0.0; // in degrees, positive is clockwise

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

  //get the current angle orientation of the robot
  //positive is clockwise, negative is counterclockwise
  public double getAngle(double[] varArray) {
    return syncAngle + varArray[3];
  }
  public double getAngle() {
    return getAbsAngle(coreDistFunc());
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

  //these functions get the position of the robot relative to the reference point
  //order is x, then y
  private double angleSum = 0.0;
  private double[] tempDisplacement = new double[2];
  public double[] getPosition(double[] displArray) {
    double originalAngle = syncAngle + Math.atan2(displArray[0], displArray[1]) * (180/Math.PI);
    //science league afterschool, get clarinet, check email about distance learning, bring all the necessary stuff from binders home, disney trip canceled, check genesis
  }
  public double[] getPosition() {
    return getPosition(coreDistFunc());
  }
  //this function gets the robot's position relative to a certain point, which itself is relative to the reference point.
  //x and y signify a point, relative to current reference point
  public double[] getRelPosition(double x, double y) {
    double[] temp = getPosition();
    temp[0] -= x;
    temp[1] -= y;
    return temp;
  }

  //sets the angle
  public void setAngle() {
    //
  }

  //this function shifts the point of reference by these distances in the x and y direction.
  public void moveRefPoint(double x, double y) {
    saveDistance();
    syncDisplacement[0] -= x;
    syncDisplacement[1] -= y;
  }
  //this is an overloaded version which shifts point to current robot position.
  public void moveRefPoint() {
    saveDistance();
    syncDisplacement[0] = 0;
    syncDisplacement[1] = 0;
  }

  //this function resets the total distance traveled to 0 centimeters.
  public void resetDistance() {
    saveCurrentPosition();
    for (int i = 0; i < 3; i++) {
      syncDistance[i] = 0.0;
    }
  }

  //execute distance and displacement save function:
  //Run this function on every upload to motors command or at every number of ticks traveled.
  void saveCurrentPosition() {
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
}