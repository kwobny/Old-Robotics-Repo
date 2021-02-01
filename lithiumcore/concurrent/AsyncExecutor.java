package org.firstinspires.ftc.teamcode.lithiumcore.concurrent;

import org.firstinspires.ftc.teamcode.lithiumcore.utils.Consumer;

import org.firstinspires.ftc.teamcode.lithiumcore.utils.pile.ArrayPile;

public class AsyncExecutor {

  //RESOURCE OBJECTS

  public AsyncExecutor() { //cannot be instantiated outside of package
    //
  }

  //--------------------------------------
  //The four methods below are not meant to be used by the user as part of the actual program. They are maintenence methods, still used by the user, but not in the user's actual programming semantics/algorithms.
  //--------------------------------------

  //In teleop:
  //run loop before method at the beginning of the teleop loop
  //run loop after method at the end of the teleop loop
  //All teleop code goes in between these two method calls

  //loop before: the part of the loop that is run before main/driver thread stuff
  public void loopBefore() {
    runBeginningLC();
    runIntervals();
  }
  //loop after: the part of the loop that is run after the main/driver thread stuff
  public void loopAfter() {
    runScheduledTasks();
    runEndingLC();
  }
  //Old order of the event loop:
  //1: loop callbacks
  //2: loop intervals
  //3: normal timeouts/scheduled tasks
  //4: driver thread
  //5: Any thread sleep for other things to run (optional, is considered part of the driver thread for now.)

  //Current / ideal order:
  //1: beginning loop callbacks
  //2: loop intervals
  //3: driver thread
  //4: normal timeouts/scheduled tasks
  //5: ending loop callbacks
  //6: Any thread sleep (optional)

  //stuff used in driver thread for single line of execution (ex. autonomous period):

  //These two methods are only used in autonomous like programs (linear execution)
  //Never used in teleop like programs (looping programs)

  //call the start method AFTER you initialize all stuff (loop callbacks, intervals, systems, etc.)
  public void start() {
    loopBefore();
  }

  public void end() {
    loopAfter();
  }

  //--------------------------------------
  //The four methods above are not meant to be used by the user as part of the actual program. They are maintenence methods, still used by the user, but not in the user's actual programming semantics/algorithms.
  //--------------------------------------

  //IMPORTANT:
  //wait for and schedule task are the same things, except one is used in driver thread and the other is not.

  //blocking wait functionality

  //used in autonomous, only inside the driver thread, where there is a single line of execution.
  //Blocks the calling/driver thread

  public void waitFor(WaitCondition waitCondition) {
    do {
      //notice how loop after is before the loop before.
      loopAfter();
      //if you wanted to implement a thread.sleep or something for an autonomous program, you would put it right here. Make sure to mirror this in the end program while loop.
      loopBefore();
    }
    while (!waitCondition.pollCondition());
  }
  public void waitFor(WaitCondition condition, Runnable callback, Runnable runWhile) {
    final WaitTask task = new WaitTask(condition, callback, runWhile);
    waitFor(task);
  }
  public void waitFor(WaitTask task) {
    task._markAsAdd();
    do {
      loopAfter();
      loopBefore();
      task._run();
    }
    while (task._isRunning());
  }

  //timeout functionality

  //allows things to execute once condition met, does not pause code execution
  private final ArrayPile<WaitTask> taskPile = new ArrayPile<>();
  
  public WaitTask scheduleTask(WaitCondition addCondition, Runnable callback, Runnable runWhile) {
    final WaitTask retTask = new WaitTask(addCondition, callback, runWhile);
    return scheduleTask(retTask);
  }
  public WaitTask scheduleTask(WaitCondition addCondition, Runnable callback) {
    return scheduleTask(addCondition, callback, null);
  }
  public WaitTask scheduleTask(final WaitTask task) {
    taskPile.add(task);
    
    task._markAsAdd();
    return task;
  }

  //cancel/remove the timeout
  public void removeTask(final WaitTask task) {
    taskPile.remove(task);
  }

  private final Consumer<WaitTask> waitTaskConsumer = new Consumer<WaitTask>() {
    @Override
    public void run(final WaitTask op) {
      if (!op._isRunning()) {
        taskPile.markAsRemove();
        return;
      }
      op._run();
    }
  };

  private void runScheduledTasks() {
    taskPile.forAll(waitTaskConsumer);
  }

  //Loop Callbacks
  //these callbacks are run on every loop, and can be added and removed.

  //specifies when a loop callback is scheduled to run in the loop.
  //BEGINNING: callback is run at beginning of loop, before anything else runs
  //END: callback is run at end of loop, after everything else runs.
  public enum LCRunBlock {
    BEGINNING, END
  }

  private final ArrayPile<CancellableCallback> loopCallbackBegin = new ArrayPile<>();
  private final ArrayPile<CancellableCallback> loopCallbackEnd = new ArrayPile<>();

  public CancellableCallback addLoopCallback(final Runnable callback, final LCRunBlock runBlock) {
    return addLoopCallback(new CancellableCallback(callback), runBlock);
  }
  public CancellableCallback addLoopCallback(final Runnable callback) {
    return addLoopCallback(callback, LCRunBlock.BEGINNING);
  }
  public CancellableCallback addLoopCallback(final CancellableCallback callback, final LCRunBlock runBlock) {
    switch (runBlock) {
      case BEGINNING:
        loopCallbackBegin.add(callback);
        break;
      case END:
        loopCallbackEnd.add(callback);
        break;
    }
    return callback;
  }
  public CancellableCallback addLoopCallback(final CancellableCallback callback) {
    return addLoopCallback(callback, LCRunBlock.BEGINNING);
  }
  public CancellableCallback removeLoopCallback(final CancellableCallback callback) {
    callback.getCorrPile().remove(callback);
    return callback;
  }
  //Loop Runnable is running
  //returns true if the supplied callback is currently running
  public boolean LCIsRunning(final CancellableCallback callback) {
    ArrayPile<CancellableCallback> pile = callback.getCorrPile();
    return pile != null && pile.has(callback);
  }

  //cancellable callback consumer
  public static final Consumer<CancellableCallback> CCConsumer = new Consumer<CancellableCallback>() {
    @Override
    public void run(final CancellableCallback op) {
      op.callback.run();
    }
  };

  //run beginning and end loop callbacks
  private void runBeginningLC() {
    loopCallbackBegin.forAll(CCConsumer);
  }
  private void runEndingLC() {
    loopCallbackEnd.forAll(CCConsumer);
  }


  //Interval code
  private final ArrayPile<WaitInterval> intervalPile = new ArrayPile<>();

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

  private static final Consumer<WaitInterval> intervalConsumer = new Consumer<WaitInterval>() {
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