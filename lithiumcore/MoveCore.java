package lithiumcore;

import other.Backend.MadHardware;

import lithiumcore.scs.InputSource;
import lithiumcore.scs.OutputSink;
import lithiumcore.scs.ThresholdWait;

import lithiumcore.sharedstate.ConstantsContainer;
import lithiumcore.sharedstate.RobotParameters;
import lithiumcore.sharedstate.SystemConfig;

class MoveCore {

  //RESOURCE OBJECTS
  protected MadHardware mhw;
  
  protected SystemConfig config;
  protected RobotParameters robotParams;

  //Only meant to be called by Move class, or any other subclassing entity.
  void initializeCore(final MadHardware mhw, final ConstantsContainer constants) {
    this.mhw = mhw;

    this.config = constants.config;
    this.robotParams = constants.robotParameters;

    universalBuffer = new MotorBufferClass(config.motor_down_scale);

    if (config.turnOnOPLP) {
      motorSyncNotifier = new RunNotifier();
      motorUploadNotifier = new RunNotifier();
    }
  }

  //START MOTOR COMMANDS

  //motor buffer functionality
  //motor buffers make it possible to superimpose two or more different motions together to achieve a sum of the motions

  //motor buffer class

  //Positive wheel power values means the wheel pushes its part of the robot forward.

  public class MotorBufferClass implements InputSource, OutputSink {
    MotorBufferClass(final double refFactor) { //cannot be instantiated outside of package
      refSpeedFactor = refFactor;
      speedFactor = refFactor;
    }
    MotorBufferClass() {
      this(1.0);
    }

    double leftFront = 0.0;
    double leftRear = 0.0;
    double rightFront = 0.0;
    double rightRear = 0.0;

    //the speed factor variable is the raw speedfactor, (which means it is already multiplied by the ref speed factor)
    public final double refSpeedFactor;
    public double speedFactor;

    //reset speed factor to relatively 1.0
    public void resetSF() {
      speedFactor = refSpeedFactor;
    }
    //sets all buffer values to one double value.
    void setBufferValues(final double val) { //default access
      leftFront = val;
      leftRear = val;
      rightFront = val;
      rightRear = val;
    }
    //set all buffer values to 0
    void clearBufferValues() { //default access
      setBufferValues(0.0);
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
      syncMotors();
    }

    //wait class so that the motor buffer speed factor can be waited upon (wow, a THIRD ORDER class!!!!)
    public class SFWait extends ThresholdWait {

      //if untilAbove is true, the object waits until the motor buffer speed factor is above the provided threshold. Otherwise, the object waits until it is below the threshold.
      public SFWait(final double threshold, final boolean untilAbove) {
        super(threshold, untilAbove);
      }

      @Override
      protected double getCompVal() {
        return get();
      }

    }
    
  }
  public class UserBuffer extends MotorBufferClass {
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

  //declaring all individual motor buffers
  public MotorBufferClass rotateBuffer = new MotorBufferClass();
  public MotorBufferClass translateBuffer = new MotorBufferClass();
  //user controlled buffer
  public UserBuffer userBuffer = new UserBuffer();

  //universal buffer. Is the virtual, ideal robot wheel power state. Is the superposition of all individual motors.
  public MotorBufferClass universalBuffer;

  //the motor buffer array, used to iterate through individual motors. Does not include universal buffer.
  private MotorBufferClass[] bufferArray = new MotorBufferClass[]{rotateBuffer, translateBuffer, userBuffer};

  //sync motor and upload motor loop notifiers
  private RunNotifier motorSyncNotifier;
  private RunNotifier motorUploadNotifier;

  //If OPLP is on:
  //the sync motors and upload to motors function will run once at the end of each loop, only if the sync motors / upload to motors function has been marked to run.

  //both of these callbacks have default access
  //loop begin callback is run at beginning of loop before everything else
  //loop end callback is run at end of loop after everything else
  //Both are only run/used if OPLP is on

  //loop begin callback
  Runnable notifierResetter = new Runnable() {
    @Override
    public void run() {
      motorSyncNotifier.reset();
      motorUploadNotifier.reset();
    }
  };
  //loop end callback
  //does the actual sync motors if OPLP is on
  Runnable OPLPEndCallback = new Runnable() {
    @Override
    public void run() {
      if (motorSyncNotifier.hasRunYet()) {
        actuallySyncMotors();
      }
      else if (motorUploadNotifier.hasRunYet()) {
        actuallyUploadToMotors();
      }
    }
  };

  //If OPLP is on, these functions mark the sync motors / upload to motors function to run at the end of the loop.
  //If it is off, then the functions do the normal behavior.

  //This function syncs (updates) the universal buffer values with the buffer values from the individual buffers.
  //It also uploads the change to the motors.
  public void syncMotors() {
    if (config.turnOnOPLP) {
      motorSyncNotifier.setHasRun();
    }
    else {
      actuallySyncMotors();
    }
  }

  //uploads the motor values from the universal buffer into the actual hardware.
  //The universal buffer is the ideal wheel power state, and is a virtual robot wheel power state. This function converts the ideal state into the real state using help from the motor cali function.
  private void uploadToMotors() {
    if (config.turnOnOPLP) {
      motorUploadNotifier.setHasRun();
    }
    else {
      actuallyUploadToMotors();
    }
  }

  //END MOTOR COMMANDS

  //universal motor calibration system
  private double[] globalPos = new double[4];
  private double[] caliPowerFactor = new double[4];
  {
    for (int i = 0; i < caliPowerFactor.length; i++) {
      caliPowerFactor[i] = 1.0;
    }
  }

  void _motorCali() {
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


  //The actual implementations of the sync motors and upload to motors function.

  private void actuallySyncMotors() {
    universalBuffer.leftFront = 0;
    universalBuffer.leftRear = 0;
    universalBuffer.rightFront = 0;
    universalBuffer.rightRear = 0;

    for (MotorBufferClass i : bufferArray) {
      universalBuffer.leftFront += i.leftFront * i.speedFactor;

      universalBuffer.leftRear += i.leftRear * i.speedFactor;

      universalBuffer.rightFront += i.rightFront * i.speedFactor;

      universalBuffer.rightRear += i.rightRear * i.speedFactor;
    }

    actuallyUploadToMotors();
    
    //save the distance only if at least one of the changes is above tick threshold.
    //rpss.saveCurrentPosition();
  }

  private void actuallyUploadToMotors() {
    double[] powers = new double[4];

    powers[0] = universalBuffer.speedFactor * universalBuffer.leftFront * caliPowerFactor[0];

    powers[1] = universalBuffer.speedFactor * universalBuffer.rightFront * caliPowerFactor[1];

    powers[2] = universalBuffer.speedFactor * universalBuffer.rightRear * caliPowerFactor[2];

    powers[3] = universalBuffer.speedFactor * universalBuffer.leftRear * caliPowerFactor[3];

    //CHECK IF POWERS EXCEEDS BOUNDARIES
    //if they do, then power values (and only temporary power values) are adjusted.

    double highestPower = 1.0;
    for (double i : powers) {
      i = Math.abs(i);
      if (i > highestPower) {
        highestPower = i;
      }
    }
    if (highestPower > config.wheelPowerLimit) {
      double multiplier = 0.999 * config.wheelPowerLimit / highestPower;
      for (int i = 0; i < powers.length; i++) {
        powers[i] *= multiplier;
      }
    }

    mhw.leftFront.setPower(powers[0]);
    mhw.rightFront.setPower(powers[1]);
    mhw.rightRear.setPower(powers[2]);
    mhw.leftRear.setPower(powers[3]);
  }
  
}