package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import java.util.ArrayList;
import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import com.qualcomm.robotcore.util.ElapsedTime;

public class WaitCore {

  //RESOURCE OBJECTS
  public WaitConditionClass waiters;
  public WaitCallbackClass waitCallbacks;
  public Main main;

  public WaitCore() {
  }

  public void initialize(Main c, WaitConditionClass a, WaitCallbackClass b) {
    waiters = a;
    waitCallbacks = b;
    main = c;
  }

  //simple wait functionality
  public void simpleWait(WaitEnum waitCondition, Object ...args) {
    Object[] dataForWait = waiters.generateData(waitCondition, args);
    while (!waiters.pollCondition(waitCondition, dataForWait)) {
      main.loop();
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
      main.loop();

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
  void runTimeouts() {
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
  void executeIntervals() {
    for (int i = 0; i < intervalPolls.size(); i++) {
      if (waiters.pollCondition(intervalPolls.get(i), intervalData.get(i))) {
        waitCallbacks.callback(intervalCallbacks.get(i));

        intervalData.set(i, waiters.generateData(intervalPolls.get(i), intervalArgs.get(i)));
      }
    }
  }
}