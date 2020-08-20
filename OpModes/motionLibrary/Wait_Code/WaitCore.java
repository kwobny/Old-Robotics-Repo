package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import java.util.ArrayList;
import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import com.qualcomm.robotcore.util.ElapsedTime;

public class WaitCore {

  //RESOURCE OBJECTS
  public Main main;

  Waitcore() {} //cannot be instantiated outside of package

  public void initialize(Main c) {
    main = c;
  }

  //simple wait functionality
  public void simpleWait(WaitCondition waitCondition) {
    while (!waiters.pollCondition()) {
      main.loop();
    }
  }

  //arrays of waits/conditions to loop through
  private ArrayList<WaitCondition> polls = new ArrayList<>();
  private ArrayList<WaitCallback> callbacks = new ArrayList<>();

  //the name of this method is self explanatory
  public void addWait(WaitCondition pollToAdd, WaitCallback callbackToAdd) {
    polls.add(pollToAdd);
    callbacks.add(callbackToAdd);
  }

  //the name of this method is self explanatory
  public void commenceWait() {
    commenceWait(Comparator.AND);
  }
  public void commenceWait(Comparator compMode) {

    switch (compMode) {
      case AND:
        boolean[] conditionSatisfied = new boolean[polls.length];
        while (true) {
          for (int i = 0; i < polls.size(); i++) {

            if (!conditionSatisfied[i] && polls.get(i).pollCondition()) {
              callbacks.get(i).run(polls.get(i));
              conditionSatisfied[i] = true;
            }

          }
          main.loop();

          boolean condition = true;
          for (boolean bool : conditionSatisfied) {
            if (!bool) {
              condition = false;
              break;
            }
          }
          if (condition) {
            break;
          }
        }

        break;
      case OR:
        outer:
        while (true) {
          for (int i = 0; i < polls.size(); i++) {

            if (polls.get(i).pollCondition()) {
              callbacks.get(i).run();
              break outer;
            }

          }
          main.loop();
        }

        break;
    }

    
  }

  //timeout functionality

  //indices to pass to remove indices function
  private ArrayList<Integer> indices = new ArrayList<Integer>();

  //allows things to execute once condition met, does not pause code execution
  private ArrayList<WaitCondition> timeoutPolls = new ArrayList<WaitEnum>();
  private ArrayList<WaitCallback> timeoutCallbacks = new ArrayList<Integer>();
  
  public void setTimeout(WaitCondition addCode, WaitCallback callback) {
    timeoutPolls.add(addCode);
    timeoutCallbacks.add(callback);
  }
  void runTimeouts() {
    for (int i = 0; i < timeoutPolls.size(); i++) {
      if (timeoutPolls.get(i).pollCondition()) {
        timeoutCallbacks.get(i).run(timeoutPolls.get(i));
        indices.add(i);
      }
    }
    Constants.removeFromArray(timeoutPolls, indices);
    Constants.removeFromArray(timeoutCallbacks, indices);
    indices.clear();
  }

  //interval functionality
  //allows things to execute every so often
  //currently turned off

  /*private ArrayList<WaitEnum> intervalPolls = new ArrayList<WaitEnum>();
  private ArrayList<Object[]> intervalData = new ArrayList<Object[]>();
  private ArrayList<Object[]> intervalArgs = new ArrayList<Object[]>();
  private ArrayList<Integer> intervalCallbacks = new ArrayList<Integer>();

  public void addInterval(WaitCondition poll, WaitCallback intervalCallback) {
    intervalPolls.add(poll);
    intervalCallbacks.add(intervalCallback);
  }
  void executeIntervals() {
    for (int i = 0; i < intervalPolls.size(); i++) {
      if (waiters.pollCondition(intervalPolls.get(i), intervalData.get(i))) {
        systemCallback(intervalCallbacks.get(i));

        intervalData.set(i, waiters.generateData(intervalPolls.get(i), intervalArgs.get(i)));
      }
    }
  }*/
}