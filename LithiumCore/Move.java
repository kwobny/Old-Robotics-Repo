package org.firstinspires.ftc.teamcode.OpModes.LithiumCore;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Vector;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Wait_Package.*;
import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.SharedState.*;

public class Move extends MoveCore {

  //UTILITY OBJECTS
  private WaitCore wait;

  Move() {} //default access constructor, cannot be instantiated outside of package

  void initialize(final MadHardware hmw, final RPS rps, final WaitCore wait, final ConstantsContainer constants, final LoopNotifier motorSyncNotifier) { //default access
    mhw = hmw;
    rpss = rps;
    this.wait = wait;
    this.motorSyncNotifier = motorSyncNotifier;

    this.config = constants.config;
    this.robotParams = constants.robotParameters;

    universalBuffer = new MotorBufferClass(config.motor_down_scale);
    ROTATE_CONSTANT = (robotParams.robotWidth + robotParams.robotLength)/robotParams.robotWidth;
  }

  //START MOVE COMMANDS

  //rotate robot
  //power is the power of the left front wheel (or left wheel for two wheel sim)
  //in two wheel sim mode, the robot is abstracted as a machine with two wheels, which are the robot's width distance apart.
  private double ROTATE_CONSTANT;
  public void rotate(double power, boolean use2WheeledSimulation) {

    if (use2WheeledSimulation) {
      power *= ROTATE_CONSTANT;
    }
    
    rotateBuffer.leftFront = power;
    rotateBuffer.leftRear = power;
    rotateBuffer.rightFront = -power;
    rotateBuffer.rightRear = -power;
  }

  //linear translate
  public void translate(final double rx, final double ry) {

    //calculating motor powers
    double a = rx + ry;
    double b = ry - rx;

    //set linear translate buffer
    linTransBuffer.leftFront = a;
    linTransBuffer.rightFront = b;
    linTransBuffer.leftRear = b;
    linTransBuffer.rightRear = a;
  }
  public void translate(final Vector vect) {
    translate(vect.x, vect.y);
  }

  //linear translate, but relative to certain robot angle
  /*private double robotCapturedAngle = 0;
  public void captureRobotAngle() {
    //
  }
  public void moveLinTransRel(double rx, double ry) {
    //calculate how much offset current angle is from robotCapturedAngle
    //0 is at positive vertical, positive goes clockwise, negative goes counterclockwise
    //in radians
    double angleOffset = 0;

    //length = sqrt(rx^2 + ry^2)
    //resultantAngle = atan2(ry, rx)
    //newAngle = resultantAngle + angleOffset
  }
  */

  public void linTransRel(final double rx, final double ry) {
    //
  }
  public void linTransRel(final Vector vect) {
    linTransRel(vect.x, vect.y);
  }

  /*
  //rotational drive (drive robot in a circle around a center point)

  //2 wheel sim version
  //simulates 2 wheels with the same distance apart as the width of robot
  //moveDirection represents the angle between the direction of movement relative to the robot and the front of the robot. If positive, direction of travel is to the right of heading of robot, negative is to the left. Is in degrees. Is an optional parameter
  public void moveRotDriveTank(double leftWheel, double rightWheel, double ...moveDirection) {
    final double centerSpeed = (leftWheel + rightWheel)/2;
    final double deviation = (leftWheel - rightWheel)/2;
    
    final double x;
    final double y;
    if (moveDirection.length == 1) {
      final double angle = 2.0 * Math.PI * (90.0 - moveDirection[0])/360.0;
      x = Math.cos(angle);
      y = Math.sin(angle);
    } else {
      x = 0;
      y = 1;
    }

    rotate(deviation, true);
    translate(x * centerSpeed, y * centerSpeed);
    syncMotors();
  }

  //input radius version

  //radius is in relation to the center of the robot wheels (midpoint of the two wheels on two wheel mode)
  //positive radius makes robot go right
  //negative radius makes robot go left
  //radius is defined in centimeters, or whatever unit the robotWidth is

  //power defines the power speed of the center point of the robot wheels

  //moveDirection is same as above
  public void moveRotDriveRadius(double power, double radius, double ...moveDirection) {
    final double leftWheel = power * (radius + Constants.robotWidth/2)/(radius - Constants.robotWidth/2);
    final double rightWheel = power * (radius - Constants.robotWidth/2)/(radius + Constants.robotWidth/2);

    moveRotDriveTank(leftWheel, rightWheel, moveDirection);
  }

  //rotational translate

  //function executed in interval
  private double rotTransStartTime = 0;
  private double rotTransCurrentTime = 0; //comment this variable out to see where need to get current time

  private double rotTransRadius = 0;
  private double rotTransSpeed = 0;
  private double rotTransPhase = 0;

  private double time = 0;
  private void executeRotTrans() {
    if (executeRotTrans) {
      time = rotTransCurrentTime - rotTransStartTime;

      //x = radius * cos(speed * time + phase)
      //y = radius * sin(speed * time + phase)

      double deltaX = rotTransRadius * rotTransSpeed * -Math.sin(rotTransSpeed * time + rotTransPhase);
      double deltaY = rotTransRadius * rotTransSpeed * Math.cos(rotTransSpeed * time + rotTransPhase);

      translate(deltaX * motorConversionRate, deltaY * motorConversionRate);
    }
  }

  //radius and angle version
  //order of parameters is radius, angle, and speed.
  //no parameters clears/stops rotational translation
  //radius defines the radius of the tracing circle, in centimeters
  //angle defines the angle from the right horizontal relative to robot front. Measured in radians
  //speed defines the speed of the robot in radians per second
  //positive speed goes clockwise, negative speed goes counter clockwise
  public void moveRotTransRadius(double ...args) {
    if (args.length == 0) {
      executeRotTrans = false;
    }
    else if (args.length != 3) {
      return;
    }

    rotTransSpeed = -args[2];
    rotTransPhase = args[1] + Math.PI;
    rotTransRadius = args[0];

    rotTransStartTime = rotTransCurrentTime;
    executeRotTrans = true;
  }

  //coordinates version
  //parameters are cx, cy, and speed
  //cx and cy define the coordinates of the centerpoint of the tracing circle, relative to the robot in it's front orientation. In centimeters.
  //speed is defined same as radial rotational translate.
  public void moveRotTransCoordinates(double cx, double cy, double speed) {
    double radius = Math.sqrt(cx*cx + cy*cy);
    double angle = Math.atan2(cy, cx);
    if (Double.doubleToRawLongBits(angle) < 0) { //check if angle is negative
      angle += 2*Math.PI;
    }
    moveRotTransRadius(radius, angle, speed);
  }
  */

  //END MOVE COMMANDS
}