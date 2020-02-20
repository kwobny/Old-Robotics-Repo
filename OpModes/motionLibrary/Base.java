package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import java.util.ArrayList;
import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;

class Base {
  //CLASS WIDE CONSTANTS/VARIABLES:
  public final double robotWidth = 5; //width of robot in centimeters
  public final double robotHeight = 5; //height of robot in centimeters
  private final double maintInterval = 0.1; //period in which functions like motor cali run

  //CLASS WIDE VARIABLES
  public double motorConversionRate = 0; //the rate of powerOutput/velocity (in centimeters/second)

  //wait class stuff
  public WaitConditionClass waiters;
  public WaitCallbacks waitCallbacks;
  protected MadHardware mhw;

  protected Base() {
    addInterval(WaitEnum.TIME, 0, maintInterval);
  }

  //motor buffer functionality
  //motor buffers make it possible to superimpose two or more different motions together to achieve a sum of the motions

  //motor buffer class
  public class motorBufferClass {
    public double leftFront = 0;
    public double leftRear = 0;
    public double rightFront = 0;
    public double rightRear = 0;

    public double speedFactor = 1;
  }
  //declaring all motor buffers
  protected motorBufferClass rotateBuffer = new motorBufferClass();
  protected motorBufferClass linTransBuffer = new motorBufferClass();
  //user controlled buffer
  public motorBufferClass userBuffer = new motorBufferClass();

  //upload motor buffer function
  private motorBufferClass[] bufferArray = new motorBufferClass[]{rotateBuffer, linTransBuffer, userBuffer};
  private motorBufferClass universalBuffer = new motorBufferClass();

  public void syncMotors() {
    universalBuffer.leftFront = 0;
    universalBuffer.leftRear = 0;
    universalBuffer.rightFront = 0;
    universalBuffer.rightRear = 0;

    for (motorBufferClass i : bufferArray) {
      universalBuffer.leftFront += i.leftFront * i.speedFactor;
      universalBuffer.leftRear += i.leftRear * i.speedFactor;
      universalBuffer.rightFront += i.rightFront * i.speedFactor;
      universalBuffer.rightRear += i.rightRear * i.speedFactor;
    }

    motorCali();
  }
  //clear motor buffers function
  public void clearMotors() {
    executeRotTrans = false;
    for (motorBufferClass i : bufferArray) {
      i.leftFront = 0;
      i.leftRear = 0;
      i.rightFront = 0;
      i.rightRear = 0;
    }
    syncMotors();
  }


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

  //quick/custom wait
  /*public interface quickWaitInterface {
    boolean quickPoll();
  }
  public void quickWait(quickWaitInterface pollObject) {
    while (!pollObject.quickPoll()) {
      loop();
    }
  }
  //boolean pauseCodeExecution specifies whether or not to use wait queue or timeout.
  public void quickWait(quickWaitInterface pollObject, CallbackInterface callback, boolean pauseCodeExecution) {
    PollInterface quickPollToAdd = new PollInterface() {
      public Object[] generateData(Object[] args) {return args;}
      public boolean pollCondition(Object[] data) {
        return pollObject.quickPoll();
      }
    };
    
    if (pauseCodeExecution) {
      addWait(quickPollToAdd, new Object[0], callback);
    }
    else {
      setTimeout(quickPollToAdd, new Object[0], callback);
    }
  }
  */

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

  //universal motor calibration system
  private double[] globalPos = {0.0, 0.0, 0.0, 0.0};
  protected void motorCali() {
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

    /* CHANGE MOTOR POWERS */
    mhw.leftFront.setPower(universalBuffer.speedFactor * 0.999 * p0 * rAVG / r0);
    mhw.rightFront.setPower(universalBuffer.speedFactor * 0.999 * p1 * rAVG / r1);
    mhw.rightRear.setPower(universalBuffer.speedFactor * 0.999 * p2 * rAVG / r2);
    mhw.leftRear.setPower(universalBuffer.speedFactor * 0.999 * p3 * rAVG / r3);

    /* SAVE MOTOR POSITIONS */
    globalPos[0] = mhw.leftFront.getCurrentPosition();
    globalPos[1] = mhw.rightFront.getCurrentPosition();
    globalPos[2] = mhw.rightRear.getCurrentPosition();
    globalPos[3] = mhw.leftRear.getCurrentPosition();
  }

  //rotational translate stuff
  protected boolean executeRotTrans = false;

  //loop for the motions
  //allows movements which are time dependent, like rotational translation
  //also allows execution of other stuff
  public void loop() {
    executeIntervals();
    runTimeouts();
  }

  //END CORE SYSTEMS SETUP
}