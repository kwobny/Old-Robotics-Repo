package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

//is a class that contains all the information necessary to complete one wait with callbacks.
//implements the command pattern

public class WaitTask {
  public WaitCondition condition;
  public Callback[] callbacks;
  public Callback runWhile; //this is basically an optional callback that continuously runs in the main loop while the wait is ongoing.
  //It is run after the wait condition is tested, so if the wait is determined to be over, then this does not run.

  public boolean endTaskAfter = true;
  //is a property that specifies whether or not to end the wait after the condition has been met.

  //A return value of true means that the wait condition has been satisfied and the wait is over. A return value of false means that the wait is still ongoing.
  boolean run() {

    if (condition.pollCondition()) {
      if (callbacks != null) {
        for (Callback i : callbacks) {
          if (i != null)
            i.run();
        }
      }
      if (endWait)
        return true;
    }
    if (runWhile != null) {
      runWhile.run();
    }
    return false;

  }

  public void setTask(final WaitCondition cond, final Callback ...callbacks) {
    setWait(cond);
    setCallback(callbacks);
  }
  public void setWait(final WaitCondition cond) throws Exception {
    if (cond == null) {
      throw new Exception("wait condition cannot be null");
    }
    condition = cond;
  }
  public void setCallback(final Callback ...callbacks) {
    this.callbacks = callbacks;
  }

  public WaitTask(final WaitCondition condition, final Callback runWhile, final Callback[] callbacks) throws Exception {
    if (condition == null) {
      throw new Exception("There is no WaitCondition provided");
    }
    this.condition = condition;
    this.callbacks = callbacks;
    this.runWhile = runWhile;
  }
  public WaitTask(final WaitCondition condition, final Callback callback, final Callback runWhile) {
    this(condition, runWhile, new Callback[]{callback});
  }
  public WaitTask() {
    //
  }
}