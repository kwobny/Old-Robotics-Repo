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
    while (!waitCondition.pollCondition()) {
      main.loop();
    }
  }
  public void simpleWait(WaitCondition condition, WaitCallback callback, WaitCallback runWhile) {
    final WaitTask task = new WaitTask(condition, callback, runWhile);
    while (!task.run()) {
      main.loop();
    }
  }

  //timeout functionality

  //indices to pass to remove indices function
  private ArrayList<Integer> indices = new ArrayList<Integer>();

  //allows things to execute once condition met, does not pause code execution
  private ArrayList<WaitTask> timeoutTasks = new ArrayList<>();
  
  public WaitTask setTimeout(WaitCondition addCondition, WaitCallback callback, WaitCallback runWhile) {
    final WaitTask retTask = new WaitTask(addCondition, callback, runWhile);
    timeoutTasks.add(retTask);
    return retTask;
  }
  public WaitTask setTimeout(WaitCondition addCondition, WaitCallback callback) {
    return setTimeout(addCondition, callback, null);
  }

  void runTimeouts() {
    for (WaitTask task : timeoutTasks) {
      if (task.run()) {
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