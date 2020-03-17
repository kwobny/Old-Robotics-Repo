package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import com.qualcomm.robotcore.util.ElapsedTime;

class MoveCore {
  //CLASS WIDE CONSTANTS/VARIABLES:
  public final double robotWidth = 5; //width of robot in centimeters. Is the width between back 2 wheels
  public final double robotLength = 5; //length of robot in centimeters. Is the length of the robot between 1 front wheel and 1 back wheel
  //Both distances are the distances between the points of contact between the wheels and ground.
  public final double robotDiagonalLen = Math.sqrt(Math.pow(robotWidth, 2) + Math.pow(robotLength, 2));
  protected final double lowFreqMaintInterval = 0.2; //period in which low frequency periodic functions like motor cali run (in seconds)
  protected final double highFreqMaintInterval = 0.05; //period in which very high frequency accurate functions run (in seconds)
  protected final int ticksPerRevolution = 2600;
  protected final double wheelDiameter = 3; //wheel diameter in centimeters
  protected final double distancePerTick = (Math.PI*wheelDiameter)/ticksPerRevolution; //how many centimeters wheel runs per wheel tick

  protected final double turnThreshold = 1.0; //the amount of rotation in degrees where the robot is considered to have turned.

  //CLASS WIDE VARIABLES
  public double motorConversionRate = 0; //the rate of powerOutput/velocity (in centimeters/second)

  //RESOURCE OBJECTS
  protected WaitConditionClass waiters;
  protected WaitCore wait;
  protected MadHardware mhw;
  protected ElapsedTime baseRuntime = new ElapsedTime();

  protected MoveCore() {
    
  }
  protected void initializeCore() {
    wait.addInterval(WaitEnum.TIME, 1, lowFreqMaintInterval);
    wait.addInterval(WaitEnum.TIME, 2, highFreqMaintInterval);
  }

  //START MOTOR COMMANDS

  //motor buffer functionality
  //motor buffers make it possible to superimpose two or more different motions together to achieve a sum of the motions

  //motor buffer class
  public class motorBufferClass {
    public double leftFront = 0.0;
    public double leftRear = 0.0;
    public double rightFront = 0.0;
    public double rightRear = 0.0;

    public double speedFactor = 1.0;
    public double acceleration = 0.0; //acceleration in percent of everything per second

    public double minFactor = -1.0;
    public double maxFactor = 1.0;
  }

  //declaring all motor buffers
  protected motorBufferClass rotateBuffer = new motorBufferClass();
  protected motorBufferClass linTransBuffer = new linTransBufferClass();
  //user controlled buffer
  public motorBufferClass userBuffer = new motorBufferClass();

  //upload motor buffer function
  private motorBufferClass universalBuffer = new motorBufferClass();
  private motorBufferClass[] bufferArray = new motorBufferClass[]{universalBuffer, rotateBuffer, linTransBuffer, userBuffer};

  public void syncMotors() {
    universalBuffer.leftFront = 0;
    universalBuffer.leftRear = 0;
    universalBuffer.rightFront = 0;
    universalBuffer.rightRear = 0;

    for (int i = 1; i < bufferArray.length; i++) {
      universalBuffer.leftFront += bufferArray[i].leftFront * bufferArray[i].speedFactor;

      universalBuffer.leftRear += bufferArray[i].leftRear * bufferArray[i].speedFactor;

      universalBuffer.rightFront += bufferArray[i].rightFront * bufferArray[i].speedFactor;

      universalBuffer.rightRear += bufferArray[i].rightRear * bufferArray[i].speedFactor;
    }

    uploadToMotors();

  }
  //clear motor buffers function
  public void clearMotors() {
    for (motorBufferClass i : bufferArray) {
      i.leftFront = 0;
      i.leftRear = 0;
      i.rightFront = 0;
      i.rightRear = 0;
    }
    syncMotors();
  }

  //END MOTOR COMMANDS

  //universal motor calibration system
  private double[] globalPos = new double[4];
  private double[] caliPowerFactor = new double[4];

  private void motorCali() {
    /* CALCULATE DISTANCES TRAVELLED */
    double d0 = mhw.leftFront.getCurrentPosition() - globalPos[0];
    double d1 = mhw.rightFront.getCurrentPosition() - globalPos[1];
    double d2 = mhw.rightRear.getCurrentPosition() - globalPos[2];
    double d3 = mhw.leftRear.getCurrentPosition() - globalPos[3];

    /* GET MOTOR POWERS */
    double p0 = universalBuffer.leftFront;
    double p1 = universalBuffer.rightFront;
    double p2 = universalBuffer.rightRear;
    double p3 = universalBuffer.leftRear;

    /* ACCOUNT FOR ZEROS IN DENOMINATORS */
    d0 += d0 == 0 ? 0.01 : 0;
    d1 += d1 == 0 ? 0.01 : 0;
    d2 += d2 == 0 ? 0.01 : 0;
    d3 += d3 == 0 ? 0.01 : 0;
    p0 += p0 == 0 ? 0.01 : 0;
    p1 += p1 == 0 ? 0.01 : 0;
    p2 += p2 == 0 ? 0.01 : 0;
    p3 += p3 == 0 ? 0.01 : 0;

    /* CALCULATE PERFORMANCE RATIOS */
    double r0 = Math.abs(d0 / p0);
    double r1 = Math.abs(d1 / p1);
    double r2 = Math.abs(d2 / p2);
    double r3 = Math.abs(d3 / p3);
    double rAVG = (r0 + r1 + r2 + r3) / 4;

    /* CHANGE MOTOR POWER FACTORS */
    caliPowerFactor[0] = 0.999 * rAVG / r0;
    caliPowerFactor[1] = 0.999 * rAVG / r1;
    caliPowerFactor[2] = 0.999 * rAVG / r2;
    caliPowerFactor[3] = 0.999 * rAVG / r3;

    uploadToMotors();

    /* SAVE MOTOR POSITIONS */
    globalPos[0] = mhw.leftFront.getCurrentPosition();
    globalPos[1] = mhw.rightFront.getCurrentPosition();
    globalPos[2] = mhw.rightRear.getCurrentPosition();
    globalPos[3] = mhw.leftRear.getCurrentPosition();
  }

  private void uploadToMotors() {
    double[] powers = new double[4];

    powers[0] = universalBuffer.speedFactor * universalBuffer.leftFront * caliPowerFactor[0];

    powers[1] = universalBuffer.speedFactor * universalBuffer.rightFront * caliPowerFactor[1];

    powers[2] = universalBuffer.speedFactor * universalBuffer.rightRear * caliPowerFactor[2];

    powers[3] = universalBuffer.speedFactor * universalBuffer.leftRear * caliPowerFactor[3];

    //CHECK IF POWERS EXCEEDS BOUNDARIES
    //if they do, then power values and universal speed factor are adjusted, and acceleration is set to 0 if going out of bounds.

    double limit = 1.0;
    for (double i : powers) {
      if (Math.abs(i) > limit) {
        limit = Math.abs(i);
      }
    }
    if (limit != 1.0) {
      double multiplier = 0.999 / limit;
      for (int i = 0; i < powers.length; i++) {
        powers[i] *= multiplier;
      }

      universalBuffer.speedFactor *= multiplier;

      for (motorBufferClass j : bufferArray) {
        if (j.acceleration * j.speedFactor > 0.0) {
          j.acceleration = 0.0;
        }
      }
      
    }

    mhw.leftFront.setPower(powers[0]);
    mhw.rightFront.setPower(powers[1]);
    mhw.rightRear.setPower(powers[2]);
    mhw.leftRear.setPower(powers[3]);

    //save the distance only if at least one of the changes is above tick threshold.
    saveCurrentPosition();
  }

  //START RPS (Robot Positioning System)

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
      wheelPosChange[i] *= distancePerTick;
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
    double dAngle = (2 * r / robotWidth) * (180.0/Math.PI);

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
  private void saveCurrentPosition() {
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

  //END RPS

  //START BASIC SYSTEM FUNCTIONS

  //system loop functions
  private boolean isZero(double value){
    final double threshold = 0.01;
    return value >= -threshold && value <= threshold;
  }

  protected void runBaseLowInterval() {
    motorCali();
  }
  protected void runBaseHighInterval() {
    //Common acceleration system
    byte accel = 0;
    for (motorBufferClass i : bufferArray) {
      if (!isZero(i.acceleration)) {
        i.speedFactor += i.acceleration * waiters.changeInTime;
        
        if (i.speedFactor > i.maxFactor) {
          i.speedFactor = i.maxFactor;
          i.acceleration = 0.0;
        }
        if (i.speedFactor < i.minFactor) {
          i.speedFactor = i.minFactor;
          i.acceleration = 0.0;
        }

        if (i == universalBuffer) {
          accel = 1;
        } else {
          accel = 2;
        }
      }
    }
    if (accel == 1) {
      uploadToMotors();
    } else if (accel == 2) {
      syncMotors();
    }
  }

  //END BASIC SYSTEM FUNCTIONS
}