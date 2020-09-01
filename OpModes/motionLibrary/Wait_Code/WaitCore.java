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

  //these callbacks are run on every loop, and can be added and removed.
  private ArrayList<CancellableCallback> loopCallbacks = new ArrayList<>();

  public CancellableCallback addLoopCallback(final CancellableCallback callback) throws Exception {
    if (callback.isActive) throw new Exception("Cannot add a loop callback that is already added");
    callback.isActive = true;
    loopCallbacks.add(callback);
  }
  public CancellableCallback removeLoopCallback(final CancellableCallback callback) throws Exception {
    if (!callback.isActive) throw new Exception("Cannot remove a loop callback that has not been added in the first place");
    callback.isActive = false;
  }

  //the static callbacks are only meant to be used by the systems, not by the user.
  private Callback[] staticLoopCallbacks;

  void setStaticCallbacks(final Callback ...callbacks) {
    staticLoopCallbacks = callbacks;
  }

  void runLoopCallbacks() {
    //run the static callbacks, and then the regular callbacks
    if (staticLoopCallbacks != null) {
      for (Callback i : staticLoopCallbacks) {
        i.run();
      }
    }

    //now run the main callbacks
    for (int i = 0; i < loopCallbacks.size(); i++) {
      CancellableCallback callback = loopCallbacks.get(i);
      if (callback.isActive) {
        callback.run();
      }
      else {
        indices.add(i);
      }
    }
    Constants.removeFromArray(loopCallbacks, indices);
    indices.clear();
  }


  //Interval code
  private ArrayList<WaitInterval> intervals = new ArrayList<>();

  public WaitInterval setInterval(final WaitInterval interv) throws Exception {
    if (interv.isActive) throw new Exception("Cannot add an interval that has already been added");
    interv.isActive = true;
    intervals.add(interv);

    return interv;
  }
  public void removeInterval(final WaitInterval interv) {
    if (!interv.isActive) throw new Exception("Cannot remove an interval which was not added in the first place");
    interv.isActive = false;
  }

  private WaitInterval[] staticIntervals = null;

  void setStaticIntervals(final WaitInterval ...intervs) {
    staticIntervals = intervs;
  }

  void runIntervals() {
    //run the static intervals first because they are the system intervals
    if (staticIntervals != null) {
      for (WaitInterval i : staticIntervals) {
        i.run();
      }
    }

    //then run the normal intervals
    for (int i = 0; i < intervals.size(); i++) {
      WaitInterval interval = intervals.get(i);

      if (interval.isActive) {
        interval.run();
      }
      else {
        indices.add(i);
      }

    }
    Constants.removeFromArray(intervals, indices);
    indices.clear();
  }

}