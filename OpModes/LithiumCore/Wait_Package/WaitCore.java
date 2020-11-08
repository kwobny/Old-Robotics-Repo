package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Wait_Package;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils.*;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Callback;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Consumer;

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
  private final BindingFullPile<WaitTask> timeoutPile = new BindingFullPile<>();
  
  public WaitTask setTimeout(WaitCondition addCondition, Callback callback, Callback runWhile) {
    final WaitTask retTask = new WaitTask(addCondition, callback, runWhile);
    return setTimeout(retTask);
  }
  public WaitTask setTimeout(WaitCondition addCondition, Callback callback) {
    return setTimeout(addCondition, callback, null);
  }
  public WaitTask setTimeout(final WaitTask task) {
    timeoutPile.add(task);
    
    task._markAsAdd();
    return task;
  }

  //cancel/remove the timeout
  public void removeTimeout(final WaitTask task) {
    timeoutPile.remove(task);
  }

  private static final Consumer<WaitTask> waitTaskConsumer = new Consumer<>() {
    @Override
    public void run(final WaitTask op) {
      if (!op._isRunning()) {
        timeoutPile.markAsRemove();
        return;
      }
      op._run();
    }
  };

  private void runTimeouts() {
    timeoutPile.forAll(waitTaskConsumer);
  }

  //these callbacks are run on every loop, and can be added and removed.
  private final BindingFullPile<CancellableCallback> loopCallbackPile = new BindingFullPile<>();

  public CancellableCallback addLoopCallback(final Callback callback) {
    return addLoopCallback(new CancellableCallback(callback));
  }
  public CancellableCallback addLoopCallback(final CancellableCallback callback) {
    loopCallbackPile.add(callback);
    return callback;
  }
  public CancellableCallback removeLoopCallback(final CancellableCallback callback) {
    loopCallbackPile.remove(callback);
  }
  //Loop Callback is running
  //returns true if the supplied callback is currently running
  public boolean LCIsRunning(final CancellableCallback callback) {
    return loopCallbackPile.has(callback);
  }

  //cancellable callback consumer
  public static final Consumer<CancellableCallback> CCConsumer = new Consumer<>() {
    @Override
    public void run(final CancellableCallback op) {
      op.callback.run();
    }
  };

  private void runLoopCallbacks() {
    //running callbacks
    loopCallbackPile.forAll(CCConsumer);
  }


  //Interval code
  private final BindingFullPile<WaitInterval> intervalPile = new BindingFullPile<>();

  public WaitInterval addInterval(final WaitInterval interv) {
    intervalPile.add(interv);

    return interv;
  }
  public void removeInterval(final WaitInterval interv) {
    intervalPile.remove(interv);
  }
  //returns true if the supplied interval is currently running
  public boolean intervalIsRunning(final WaitInterval interval) {
    return intervalPile.has(interval);
  }

  private static final Consumer<WaitInterval> intervalConsumer = new Consumer<>() {
    @Override
    public void run(final WaitInterval op) {
      op.run();
    }
  };

  private void runIntervals() {
    //running intervals
    intervalPile.forAll(intervalConsumer);
  }

}