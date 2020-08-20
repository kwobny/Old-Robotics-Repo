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
  private ArrayList<WaitTask> waits = new ArrayList<>();

  //the name of this method is self explanatory
  public WaitTask addWait(WaitCondition pollToAdd, WaitCallback callbackToAdd) {
    final WaitTask retWait = new WaitTask(pollToAdd, callbackToAdd);
    waits.add(retWait);
    return retWait;
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
          for (WaitTask currentWait : waits) {

            if (!conditionSatisfied[i] && currentWait.condition.pollCondition()) {
              currentWait.callback.run(currentWait.condition);
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
          for (WaitTask currentWait : waits) {

            if (currentWait.condition.pollCondition()) {
              currentWait.callback.run(currentWait.condition);
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
  private ArrayList<WaitTask> timeoutTasks = new ArrayList<>();
  
  public void setTimeout(WaitCondition addCondition, WaitCallback callback) {
    final WaitTask retTask = new WaitTask(addCondition, callback);
    timeoutTasks.add(retTask);
    return retTask;
  }
  void runTimeouts() {
    for (WaitTask task : timeoutTasks) {
      if (task.condition.pollCondition()) {
        task.callback.run(task.condition);
        indices.add(i);
      }
    }
    Constants.removeFromArray(timeoutTasks, indices);
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