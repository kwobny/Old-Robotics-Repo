package org.firstinspires.ftc.teamcode.LithiumCore;

import org.firstinspires.ftc.teamcode.LithiumCore.Utils.Vector;
import org.firstinspires.ftc.teamcode.LithiumCore.Wait_Package.*;
import org.firstinspires.ftc.teamcode.LithiumCore.Utils.Callback;
import org.firstinspires.ftc.teamcode.Backend.MadHardware;

import org.firstinspires.ftc.teamcode.LithiumCore.SharedState.*;

public class Move extends MoveCore {

  //UTILITY OBJECTS
  private Main main;
  private RPS rps;

  Move() {} //default access constructor, cannot be instantiated outside of package

  void initialize(final MadHardware hmw, final Main main, final RPS rps, final ConstantsContainer constants) { //default access
    //initialize move core
    initializeCore(hmw, constants);

    this.main = main;
    this.rps = rps;
    
    //ROTATE_CONSTANT = (robotParams.robotWidth + robotParams.robotLength)/robotParams.robotWidth;
    ROTATE_CONSTANT = robotParams.robotDiagonalLen / robotParams.robotWidth;
  }

  //START MOVE COMMANDS

  //clear functions basically remove the corresponding type of movement from the list of superimposed movements.

  public void clearAll() {
    clearRotate();
    clearTranslate();
    clearRT();
  }

  //rotate robot
  //power is the power of the left front wheel (or left wheel for two wheel sim)
  //When you look down from above on the robot, a positive power is clockwise, negative power is counter clockwise.
  //in two wheel sim mode, the robot is abstracted as a machine with two wheels, which are the robot's width distance apart.
  private double ROTATE_CONSTANT;
  public void rotate(double power, boolean use2WheeledSimulation) {

    //check for NaN
    if (Double.isNaN(power)) {
      throw new RuntimeException("You cannot supply NaN as power parameter into the rotate function.");
    }

    if (use2WheeledSimulation) {
      power *= ROTATE_CONSTANT;
    }
    
    rotateBuffer.leftFront = power;
    rotateBuffer.leftRear = power;
    rotateBuffer.rightFront = -power;
    rotateBuffer.rightRear = -power;
  }

  public void clearRotate() {
    rotateBuffer.clearBufferValues();
  }

  //linear translate

  //positive x is in the right direction
  //positive y is in the forward direction

  public void translate(final double rx, final double ry) {

    //check for NaN
    if (Double.isNaN(rx) || Double.isNaN(ry)) {
      throw new RuntimeException("You cannot supply NaN into the translate function.");
    }

    //calculating motor powers
    double a = rx + ry;
    double b = ry - rx;

    //set linear translate buffer
    translateBuffer.leftFront = a;
    translateBuffer.rightFront = b;
    translateBuffer.leftRear = b;
    translateBuffer.rightRear = a;
  }
  public void translate(final Vector vect) {
    translate(vect.x, vect.y);
  }

  public void clearTranslate() {
    translateBuffer.clearBufferValues();
  }

  //linear translate, but relative to the starting/origin/reference orientation of the robot.

  private Vector RTVector;
  private final CancellableCallback RTRunner = new CancellableCallback(new Callback() {
    @Override
    public void run() {
      final double currAngle = rps.getRadianAngle();
      Vector newVector = RTVector.clone().rotate(currAngle);
      translate(newVector);
      syncMotors();
    }
  });

  //Relative linear translate. Translates relative to the robot's 0 / origin angle.
  public void translateRel(final double rx, final double ry) {
    translateRel(new Vector(rx, ry));
  }
  public void translateRel(final Vector vect) {
    RTVector = vect;
    
    //relative translate runner is not in low maint pile
    if (!main.highMaint.has(RTRunner)) {
      main.highMaint.add(RTRunner);
      RTRunner.callback.run();
    }
  }

  //stop doing relative translate
  public void clearRT() {
    //test if tr runner is in running pile
    if (main.highMaint.has(RTRunner)) {
      main.highMaint.remove(RTRunner);
    }
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