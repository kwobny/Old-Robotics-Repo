package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.GenericOperation.*;
import java.util.ArrayList;
import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import com.qualcomm.robotcore.util.ElapsedTime;

public class WaitCore {

  //RESOURCE OBJECTS

  Waitcore() { //cannot be instantiated outside of package
    //
  }

  //In teleop, it is best to run this method at the beginning of each loop, because then the thread sleep will be at the end of every driver thread run (end of function call)
  public void loop() {
    runLoopCallbacks();
    runIntervals();
    runTimeouts();
  }
  //the order of the event loop is:
  //1: loop callbacks
  //2: loop intervals
  //3: normal timeouts
  //4: driver thread
  //5: Any thread sleep for other things to run (optional, is considered part of the driver thread for now.)

  //experimental order:
  //1: loop callbacks
  //2: loop intervals
  //3: driver thread
  //4: normal timeouts
  //5: Any thread sleep (optional)
  
  //the problem with this is that the driver thread can include a thread sleep in its code, which means that the loop is split at a point where it is not supposed to.

  //simple wait functionality
  //important thing to note here: the loops used are do while loops. As a result, the code that resumes the driver coroutine runs in the same block of time as the rest of the coroutine scanning. This also means that the driver resume code also runs after the loop callbacks, which include setting the various values for that loop.

  public void simpleWait(WaitCondition waitCondition) {
    do {
      //if you wanted to implement a thread.sleep or something for an autonomous program, you would put it right here. Make sure to mirror this in the end program while loop.
      loop();
    }
    while (!waitCondition.pollCondition());
  }
  public void simpleWait(WaitCondition condition, Callback callback, Callback runWhile) {
    final WaitTask task = new WaitTask(condition, callback, runWhile);
    simpleWait(task);
  }
  public void simpleWait(WaitTask task) {
    task.markAsAdd();
    do {
      loop();
      task.run();
    }
    while (task.isRunning());
  }

  //timeout functionality

  //indices to pass to remove indices function
  private ArrayList<Integer> indices = new ArrayList<Integer>();

  //allows things to execute once condition met, does not pause code execution
  private final OperationRunner<WaitTask> timeoutRunner = new OperationRunner<>() {
    
    @Override
    protected void runOp(WaitTask op) {
      op.run();
      if (!op.isRunning()) {
        remove(op);
      }
    }

  };
  
  public WaitTask setTimeout(WaitCondition addCondition, Callback callback, Callback runWhile) {
    final WaitTask retTask = new WaitTask(addCondition, callback, runWhile);
    return setTimeout(retTask);
  }
  public WaitTask setTimeout(WaitCondition addCondition, Callback callback) {
    return setTimeout(addCondition, callback, null);
  }
  public WaitTask setTimeout(WaitTask task) throws Exception {
    timeoutRunner.add(task);
    
    task.markAsAdd();
    return task;
  }

  void runTimeouts() {
    timeoutRunner.runAll();
  }

  //these callbacks are run on every loop, and can be added and removed.
  private OperationRunner<CancellableCallback> loopCallbackRunner = new OperationRunner<>() {
    @Override
    protected void runOp(CancellableCallback op) {
      op.run();
    }
  };

  public CancellableCallback addLoopCallback(final Callback callback) {
    return addLoopCallback(new CancellableCallback(callback));
  }
  public CancellableCallback addLoopCallback(final CancellableCallback callback) {
    loopCallbackRunner.add(callback);
    return callback;
  }
  public CancellableCallback removeLoopCallback(final CancellableCallback callback) {
    loopCallbackRunner.remove(callback);
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
    loopCallbackRunner.runAll();
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