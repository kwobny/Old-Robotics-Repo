package org.firstinspires.ftc.teamcode.OpModes.motionLibrary.Wait_Package;

import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.GenericOperation.*;

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
    task._markAsAdd();
    do {
      loop();
      task._run();
    }
    while (task._isRunning());
  }

  //timeout functionality

  //allows things to execute once condition met, does not pause code execution
  private final OperationRunner<WaitTask> timeoutRunner = new OperationRunner<>() {
    
    @Override
    protected void runOp(WaitTask op) {
      if (!op._isRunning()) {
        remove(op);
        return;
      }
      op._run();
      if (!op._isRunning()) {
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
  public WaitTask setTimeout(final WaitTask task) throws Exception {
    timeoutRunner.add(task);
    
    task._markAsAdd();
    return task;
  }

  //cancel/remove the timeout
  public void removeTimeout(final WaitTask task) {
    timeoutRunner.remove(task);
  }

  private void runTimeouts() {
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

  //this function is not meant to be used by the user, only used by the motion library.
  public void _setStaticCallbacks(final Callback ...callbacks) {
    staticLoopCallbacks = callbacks;
  }

  private void runLoopCallbacks() {
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
  private OperationRunner<WaitInterval> intervalRunner = new OperationRunner<>() {
    @Override
    protected void runOp(WaitInterval op) {
      op.run();
    }
  };

  public WaitInterval addInterval(final WaitInterval interv) {
    intervalRunner.add(interv);

    return interv;
  }
  public void removeInterval(final WaitInterval interv) {
    intervalRunner.remove(interv);
  }

  private WaitInterval[] staticIntervals = null;

  //This function should not be used outside of motion library.
  public void _setStaticIntervals(final WaitInterval ...intervs) {
    staticIntervals = intervs;
  }

  private void runIntervals() {
    //run the static intervals first because they are the system intervals
    if (staticIntervals != null) {
      for (WaitInterval i : staticIntervals) {
        i.run();
      }
    }

    //then run the normal intervals
    intervalRunner.runAll();
  }

}