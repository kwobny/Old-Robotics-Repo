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
  //important thing to note here: the loops used are do while loops. As a result, the code that resumes the driver coroutine runs in the same block of time as the rest of the coroutine scanning. This also means that the driver resume code also runs after the loop callbacks, which include setting the various values for that loop.

  public void simpleWait(WaitCondition waitCondition) {
    do {
      main.loop();
    }
    while (!waitCondition.pollCondition());
  }
  public void simpleWait(WaitCondition condition, WaitCallback callback, WaitCallback runWhile) {
    final WaitTask task = new WaitTask(condition, callback, runWhile);
    do {
      main.loop();
    }
    while (!task.run());
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

  public WaitInterval addInterval(final WaitInterval interv) throws Exception {
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