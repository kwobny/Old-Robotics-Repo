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
  protected RPS rpss;
  protected WaitCore wait;
  protected MadHardware mhw;
  protected ElapsedTime baseRuntime = new ElapsedTime();

  protected void initializeCore() {
    wait.addInterval(WaitEnum.TIME, -1, lowFreqMaintInterval);
    wait.addInterval(WaitEnum.TIME, -2, highFreqMaintInterval);
  }

  //START MOTOR COMMANDS

  //motor buffer functionality
  //motor buffers make it possible to superimpose two or more different motions together to achieve a sum of the motions

  //motor buffer class
  public static class motorBufferClass {
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
    rpss.saveCurrentPosition();
  }

  //START RPS (Robot Positioning System)

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