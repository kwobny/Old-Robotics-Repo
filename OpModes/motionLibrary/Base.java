package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import java.util.ArrayList;
import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import com.qualcomm.robotcore.util.ElapsedTime;

class Base {
  //CLASS WIDE CONSTANTS/VARIABLES:
  public final double robotWidth = 5; //width of robot in centimeters. Is the width between back 2 wheels
  public final double robotLength = 5; //length of robot in centimeters. Is the length of the robot between 1 front wheel and 1 back wheel
  //Both distances are the distances between the points of contact between the wheels and ground.
  protected final double lowFreqMaintInterval = 0.2; //period in which low frequency periodic functions like motor cali run (in seconds)
  protected final double highFreqMaintInterval = 0.05; //period in which very high frequency accurate functions run (in seconds)
  protected final int ticksPerRevolution = 2600;
  protected final double wheelDiameter = 3; //wheel diameter in centimeters
  protected final double distancePerTick = (Math.PI*wheelDiameter)/ticksPerRevolution; //how many centimeters wheel runs per wheel tick

  //CLASS WIDE VARIABLES
  public double motorConversionRate = 0; //the rate of powerOutput/velocity (in centimeters/second)

  //wait class stuff
  public WaitConditionClass waiters;
  public WaitCallbacks waitCallbacks;
  protected MadHardware mhw;
  protected ElapsedTime baseRuntime;

  protected Base() {
    addInterval(WaitEnum.TIME, 1, lowFreqMaintInterval);
    addInterval(WaitEnum.TIME, 2, highFreqMaintInterval);
  }

  //START MOTOR COMMANDS

  //motor buffer functionality
  //motor buffers make it possible to superimpose two or more different motions together to achieve a sum of the motions

  //base wheelValueDecimal class
  protected class wheelValueDecimalClass {
    public double leftFront = 0.0;
    public double leftRear = 0.0;
    public double rightFront = 0.0;
    public double rightRear = 0.0;
  }

  //motor buffer class
  public class motorBufferClass extends wheelValueDecimalClass {
    public double speedFactor = 1.0;
    public double acceleration = 0.0; //acceleration in percent of everything per second

    public double minFactor = -1.0;
    public double maxFactor = 1.0;
  }
  protected class linTransBufferClass extends motorBufferClass {
    public double rx = 0.0;
    public double ry = 0.0;
  }
  //declaring all motor buffers
  protected motorBufferClass rotateBuffer = new motorBufferClass();
  protected linTransBufferClass linTransBuffer = new linTransBufferClass();
  //user controlled buffer
  public motorBufferClass userBuffer = new motorBufferClass();

  //upload motor buffer function
  private motorBufferClass universalBuffer = new motorBufferClass();
  private motorBufferClass[] bufferArray = new motorBufferClass[]{universalBuffer, rotateBuffer, linTransBuffer, userBuffer};

  protected boolean notNeedSync = true;

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

    notNeedSync = true;
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

  //START WAIT COMMANDS

  //wait functionality
  //the wait queue allows multiple wait commands and callbacks to occur at once, and pauses program execution until all waits have finished.

  //simple wait functionality
  public void simpleWait(WaitEnum waitCondition, Object ...args) {
    Object[] dataForWait = waiters.generateData(waitCondition, args);
    while (!waiters.pollCondition(waitCondition, dataForWait)) {
      loop();
    }
  }

  //arrays of waits/conditions to loop through
  private ArrayList<WaitEnum> polls = new ArrayList<WaitEnum>();
  private ArrayList<Integer> callbacks = new ArrayList<Integer>();
  //data for polls
  private ArrayList<Object[]> dataList = new ArrayList<Object[]>();
  //array of booleans which specify whether or not condition has been satisfied
  private ArrayList<Boolean> conditionSatisfied = new ArrayList<Boolean>();

  //the name of this method is self explanatory
  public void addWait(WaitEnum pollToAdd, Integer callbackToAdd, Object ...args) {
    polls.add(pollToAdd);
    callbacks.add(callbackToAdd);
    dataList.add(args);

    conditionSatisfied.add(false);
  }

  //a enum for compound polls, which specifies comparison mode
  public enum Comparator {
    AND,
    OR
  }

  //the name of this method is self explanatory
  public void commenceWait(Comparator ...args) {
    Comparator compMode;
    if (args.length == 0) {
      compMode = Comparator.AND;
    }
    else if (args.length == 1) {
      compMode = args[0];
    }
    else {
      return;
    }

    boolean condition = false;

    for (int i = 0; i < dataList.size(); i++) {
      dataList.set(i, waiters.generateData(polls.get(i), dataList.get(i)));
    }

    while (true) {
      for (int i = 0; i < polls.size(); i++) {
        if (!conditionSatisfied.get(i) && waiters.pollCondition(polls.get(i), dataList.get(i))) {
          waitCallbacks.callback(callbacks.get(i));
          conditionSatisfied.set(i, true);
        }
      }
      loop();

      switch (compMode) {
        case AND: {
          condition = true;
          for (boolean bool : conditionSatisfied) {
            if (!bool) {
              condition = false;
              break;
            }
          }
          break;
        }
        case OR: {
          condition = false;
          for (boolean bool : conditionSatisfied) {
            if (bool) {
              condition = true;
              break;
            }
          }
          break;
        }
      }
      if (condition) {
        polls.clear();
        callbacks.clear();
        dataList.clear();
        conditionSatisfied.clear();
        break;
      }
    }
  }

  //timeout functionality

  //important function to remove multiple items from ArrayList
  private ArrayList<Integer> indices = new ArrayList<Integer>();
  private void removeFromArray(ArrayList<?> primaryArray, ArrayList<Integer> indexArray) {
    int valueDecrease = 0;
    for (int i : indexArray) {
      primaryArray.remove(i-valueDecrease);
      valueDecrease += 1;
    }
  }

  //allows things to execute once condition met, does not pause code execution
  private ArrayList<WaitEnum> timeoutPolls = new ArrayList<WaitEnum>();
  private ArrayList<Integer> timeoutCallbacks = new ArrayList<Integer>();
  private ArrayList<Object[]> timeoutData = new ArrayList<Object[]>();
  
  public void setTimeout(WaitEnum addCode, Integer callback, Object ...args) {
    timeoutPolls.add(addCode);
    timeoutCallbacks.add(callback);
    timeoutData.add(waiters.generateData(addCode, args));
  }
  private void runTimeouts() {
    for (int i = 0; i < timeoutPolls.size(); i++) {
      if (waiters.pollCondition(timeoutPolls.get(i), timeoutData.get(i))) {
        waitCallbacks.callback(timeoutCallbacks.get(i));
        indices.add(i);
      }
    }
    removeFromArray(timeoutPolls, indices);
    removeFromArray(timeoutCallbacks, indices);
    removeFromArray(timeoutData, indices);
    indices.clear();
  }

  //interval functionality
  //allows things to execute every so often

  private ArrayList<WaitEnum> intervalPolls = new ArrayList<WaitEnum>();
  private ArrayList<Object[]> intervalData = new ArrayList<Object[]>();
  private ArrayList<Object[]> intervalArgs = new ArrayList<Object[]>();
  private ArrayList<Integer> intervalCallbacks = new ArrayList<Integer>();

  public void addInterval(WaitEnum addIntervalCode, Integer intervalCallback, Object ...args) {
    intervalPolls.add(addIntervalCode);
    intervalArgs.add(args);
    intervalData.add(waiters.generateData(addIntervalCode, args));
    intervalCallbacks.add(intervalCallback);
  }
  private void executeIntervals() {
    for (int i = 0; i < intervalPolls.size(); i++) {
      if (waiters.pollCondition(intervalPolls.get(i), intervalData.get(i))) {
        waitCallbacks.callback(intervalCallbacks.get(i));

        intervalData.set(i, waiters.generateData(intervalPolls.get(i), intervalArgs.get(i)));
      }
    }
  }

  //END WAIT COMMANDS

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
  }

  //START RPS (Robot Positioning System)

  //robot position and distance tracking system. All distance values in centimeters

  //class for wheel tick storage
  private class tickMeasureClass {
    public int leftFront = 0;
    public int rightFront = 0;
    public int leftBack = 0;
    public int rightBack = 0;
  }

  //displacement and distance variables
  private double[] syncDisplacement = {0.0, 0.0}; //0 index is x, 1 is y
  private double syncDistance = 0.0;

  //wheel position variables
  private tickMeasureClass lastWheelPos = new tickMeasureClass();
  private wheelValueDecimalClass wheelPosChange = new wheelValueDecimalClass();

  //get displacement from last sync position
  private double[] coreDistFunc() {
    //calculate the lintrans component of the change in distance for each wheel.
    wheelPosChange.leftFront = (linTransBuffer.leftFront * linTransBuffer.speedFactor/universalBuffer.leftFront) * distancePerTick * (mhw.leftFront.getCurrentPosition() - lastWheelPos.leftFront);
    wheelPosChange.rightFront = (linTransBuffer.rightFront * linTransBuffer.speedFactor/universalBuffer.rightFront) * distancePerTick * (mhw.rightFront.getCurrentPosition() - lastWheelPos.rightFront);
    wheelPosChange.leftBack = (linTransBuffer.leftBack * linTransBuffer.speedFactor/universalBuffer.leftBack) * distancePerTick * (mhw.leftBack.getCurrentPosition() - lastWheelPos.leftBack);
    wheelPosChange.rightBack = (linTransBuffer.rightBack * linTransBuffer.speedFactor/universalBuffer.rightBack) * distancePerTick * (mhw.rightBack.getCurrentPosition() - lastWheelPos.rightBack);

    //inverse function to get dx and dy
    double dx; double dy;

    return new double[]{dx, dy};
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
    saveDistance();
    syncDistance = 0;
  }

  //this function gets the robot's position relative to a certain point, which itself is relative to the reference point.
  //x and y signify a point, relative to current reference point
  private double[] getRelPosition(double[] displArray) {
    return new double[]{displArray[0] + syncDisplacement[0], displArray[1] + syncDisplacement[1]};
  }
  public double[] getRelPosition() {
    return getRelPosition(coreDistFunc());
  }
  public double[] getRelPosition(double x, double y) {
    double[] temp = getRelPosition();
    temp[0] -= x;
    temp[1] -= y;
    return temp;
  }

  //this function gets the total distance (not displacement) traveled by the robot.
  private double getDistanceTraveled(double[] displArray) {
    return Math.sqrt(Math.pow(displArray[0], 2) + Math.pow(displArray[1], 2)) + syncDistance;
  }
  public double getDistanceTraveled() {
    return getDistanceTraveled(coreDistFunc());
  }

  //execute distance and displacement save function:
  //this function executes everytime a motor buffer is changed by lintrans or rotate command
  protected void saveDistance() {
    //add dx and dy to displacement and distance
    double[] tempArray = coreDistFunc();
    syncDisplacement = getRelPosition(tempArray);
    syncDistance = getDistanceTraveled(tempArray);

    //set last wheel ticks to the current one.
    lastWheelPos.leftFront = mhw.leftFront.getCurrentPosition();
    lastWheelPos.rightFront = mhw.rightFront.getCurrentPosition();
    lastWheelPos.leftBack = mhw.leftBack.getCurrentPosition();
    lastWheelPos.rightBack = mhw.rightBack.getCurrentPosition();
  }

  //END RPS

  //START BASIC SYSTEM FUNCTIONS

  //function that you call at the end of autonomous sequence to wait for program to end. This should be the absolute last function executed in program.
  public void endProgram() {
    while (true) {
      loop();
    }
  }

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

  //loop for the motions
  //allows movements which are time dependent, like rotational translation
  //also allows execution of other stuff
  public void loop() {
    executeIntervals();
    runTimeouts();
  }

  //END BASIC SYSTEM FUNCTIONS
}