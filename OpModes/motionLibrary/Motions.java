package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import java.util.ArrayList;
import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Motions extends Base {
  //time system has simple wait, complex wait (add wait and commence wait), set timeout, add interval
  //concerns with time system: instantiate wait class and wait callbacks outside of motions function?
  //motor cali
  //moveLinTrans

  // IMPORTANT NOTES:
  // make sure to include loop method in main method
  // make sure to provide MadHardware instance to this object
  //make sure core system is adapted into the new api environment
  //make sure to change class wide constants as fit
  //make sure to include all java libraries needed
  //moveRotDriveRadius can be used to test itself, moveRotDriveTank, and moveRotate

  //methods to integrate:
  //in motions:
  //moveLinTrans

  //in base:
  //motorCali (make sure to incorporate conversionRate calculation)
  //syncMotors

  //incomplete functions:
  //wait time
  //rot trans (get current time)
  //relative linear translate (configure gyro)

  //things to do:
  //consider including compound timeout (multiple polls in one callback)
  //restructure/organize/increase efficiency of wait api

  public Motions(MadHardware hmw) {
    mhw = hmw;
    baseRuntime = new ElapsedTime();
    wait = new WaitCore();
    initializeBase();

    //set up lin trans and rotate buffer speed factor settings
    rotateBuffer.maxFactor = 0.1;
    rotateBuffer.minFactor = -0.1;

    linTransBuffer.maxFactor = 2.0;
    linTransBuffer.minFactor = -2.0;

  }

  //START MOVE COMMANDS

  //rotate robot
  //power is the power of the left front wheel (or left wheel for two wheel sim)
  public void moveRotate(double power, boolean use2WheeledSimulation) {
    rotateBuffer.speedFactor = 0.1;

    final double constant = (robotWidth + robotLength)/robotWidth;

    if (use2WheeledSimulation) {
      power *= constant;
    }
    
    rotateBuffer.leftFront = power;
    rotateBuffer.leftRear = power;
    rotateBuffer.rightFront = -power;
    rotateBuffer.rightRear = -power;
  }

  //linear translate
  public void moveLinTrans(double rx, double ry, boolean boostOverride) {
    double BOOOOOST = boostOverride ? 2.0 : 1.0;
    linTransBuffer.speedFactor = 0.1 * BOOOOOST;

    //using encoder to find magnitude of joystick x and y
    /*if (gamepadControl) {
      rx = gamepad1.right_stick_x;
      ry = -gamepad1.left_stick_y;
    }*/

    //calculating motor powers
    double a = rx + ry;
    double b = ry - rx;

    //sets powers according to compensation
    linTransBuffer.leftFront = a;
    //globalPow[0] = 0.1 * BOOOOOST * a;
    linTransBuffer.rightFront = b;
    //globalPow[1] = 0.1 * BOOOOOST * b;
    linTransBuffer.leftRear = b;
    //globalPow[3] = 0.1 * BOOOOOST * b;
    linTransBuffer.rightRear = a;
    //globalPow[2] = 0.1 * BOOOOOST * a;
  }
  public void moveLinTrans(double rx, double ry) {
    moveLinTrans(rx, ry, false);
  }

  //linear translate, but relative to certain robot angle
  private double robotCapturedAngle = 0;
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

    moveRotate(deviation, true);
    moveLinTrans(x * centerSpeed, y * centerSpeed);
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
    final double leftWheel = power * (radius + robotWidth/2)/(radius - robotWidth/2);
    final double rightWheel = power * (radius - robotWidth/2)/(radius + robotWidth/2);

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

      moveLinTrans(deltaX * motorConversionRate, deltaY * motorConversionRate);
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

  //END MOVE COMMANDS
}