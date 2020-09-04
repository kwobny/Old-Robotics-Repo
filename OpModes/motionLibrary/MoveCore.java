package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import com.qualcomm.robotcore.util.ElapsedTime;

class MoveCore {
  //SETTING VARIABLES
  protected final double turnThreshold = 1.0; //the amount of rotation in degrees where the robot is considered to have turned.
  
  public double motorConversionRate = 0; //the rate of powerOutput/velocity (in centimeters/second)

  //RESOURCE OBJECTS
  protected RPS rpss;
  protected MadHardware mhw;
  protected ElapsedTime baseRuntime = new ElapsedTime();

  //START MOTOR COMMANDS

  //motor buffer functionality
  //motor buffers make it possible to superimpose two or more different motions together to achieve a sum of the motions

  //motor buffer class
  public static class motorBufferClass implements InputSource, OutputSink {
    motorBufferClass(final double refFactor) { //cannot be instantiated outside of package
      refSpeedFactor = refFactor;
    }
    motorBufferClass() {
      this(1.0);
    }

    double leftFront = 0.0;
    double leftRear = 0.0;
    double rightFront = 0.0;
    double rightRear = 0.0;

    //the speed factor variable is the raw speedfactor, (which means it is already multiplied by the ref speed factor)
    public final double refSpeedFactor;
    public double speedFactor = refSpeedFactor;

    public void resetSpeedFactor() {
      speedFactor = refSpeedFactor;
    }

    //now, the methods/classes which make this compatible with other parts of the library

    //input source get method for SCS
    @Override
    public double get() {
      return speedFactor/refSpeedFactor;
    }
    //output sink set method for SCS
    @Override
    public void set(double val) {
      speedFactor = val * refSpeedFactor;
    }

    //wait class so that the motor buffer speed factor can be waited upon (wow, a THIRD ORDER class!!!!)
    public class SFWait implements WaitCondition {

      private double SF;
      private boolean untilAbove;
      
      //if untilAbove is true, the object waits until the motor buffer speed factor is above the provided threshold. Otherwise, the object waits until it is below the threshold.
      public SFWait(final double SF, final boolean untilAbove) {
        this.SF = SF;
        this.untilAbove = untilAbove;
      }
      
      @Override
      public boolean pollCondition() {
        if (untilAbove) {
          return SF > speedFactor;
        }
        else {
          return SF < speedFactor;
        }
      }

    }
  }
  public static class UserBuffer extends motorBufferClass {
    UserBuffer(final double refFactor) { //cannot be instantiated outside of package
      super(refFactor);
    }
    UserBuffer() {}

    //order is left front, right front, left rear, and right rear.
    //if a value is -0.0 (remember, floating point numbers have two values for 0, one positive and one negative), it signifies not to change the value.
    public void setWheelValues(final double ...values) {
      if (values.length != 4) return;
      
      if (values[0] != -0.0)
        leftFront = values[0];
      if (values[1] != -0.0)
        rightFront = values[1];
      if (values[2] != -0.0)
        leftRear = values[2];
      if (values[3] != -0.0)
        rightRear = values[3];
    }
  }

  //declaring all motor buffers
  public motorBufferClass rotateBuffer = new motorBufferClass();
  public motorBufferClass linTransBuffer = new linTransBufferClass();
  //user controlled buffer
  public UserBuffer userBuffer = new UserBuffer();

  //upload motor buffer function
  protected motorBufferClass universalBuffer = new motorBufferClass(Constants.motor_down_scale);
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

  void motorCali() {
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
    //if they do, then power values (and only temporary power values) are adjusted.

    double limit = 1.0;
    for (double i : powers) {
      if (Math.abs(i) > limit) {
        limit = Math.abs(i);
      }
    }
    if (limit > 1.0) {
      double multiplier = 0.999 / limit;
      for (int i = 0; i < powers.length; i++) {
        powers[i] *= multiplier;
      }

      //universalBuffer.speedFactor *= multiplier;

      /*for (motorBufferClass j : bufferArray) {
        if (j.acceleration * j.speedFactor > 0.0) {
          j.acceleration = 0.0;
        }
      }*/
      
    }

    mhw.leftFront.setPower(powers[0]);
    mhw.rightFront.setPower(powers[1]);
    mhw.rightRear.setPower(powers[2]);
    mhw.leftRear.setPower(powers[3]);

    //save the distance only if at least one of the changes is above tick threshold.
    rpss.saveCurrentPosition();
  }

  //START BASIC SYSTEM FUNCTIONS
  /*
  void runCommonAccelerationSystem(double changeInTime) {
    //This function is run in the high interval
    byte accel = 0;
    for (motorBufferClass i : bufferArray) {
      if (i.acceleration != 0.0) {
        i.speedFactor += i.acceleration * changeInTime;

        if (i == universalBuffer) {
          accel = 1;
        } else {
          accel = 2;
        }

        if (i.thresholdEnabled) {
          if (i.acceleration > 0.0) {
            if (i.speedFactor > i.threshold) {
              i.speedFactor = i.threshold;
              i.acceleration = 0.0;
              i.thresholdEnabled = false;
            }
          } else if (i.speedFactor < i.threshold) {
            i.speedFactor = i.threshold;
            i.acceleration = 0.0;
            i.thresholdEnabled = false;
          }
        }
      }
      if (i.speedFactor > i.maxFactor) {
        i.speedFactor = i.maxFactor;
        i.acceleration = 0.0;
      }
      else if (i.speedFactor < i.minFactor) {
        i.speedFactor = i.minFactor;
        i.acceleration = 0.0;
      }
    }
    if (accel == 1) {
      uploadToMotors();
    } else if (accel == 2) {
      syncMotors();
    }
  }
  */

  //END BASIC SYSTEM FUNCTIONS
}