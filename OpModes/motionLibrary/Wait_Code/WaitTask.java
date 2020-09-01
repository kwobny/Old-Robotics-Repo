package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

//is a class that contains all the information necessary to complete one wait with callbacks.
//implements the command pattern

public class WaitTask {
  public WaitCondition condition;
  public WaitCallback callback;
  public WaitCallback runWhile; //this is basically an optional callback that continuously runs in the main loop while the wait is ongoing.
  //It is run after the wait condition is tested, so if the wait is determined to be over, then this does not run.

  //A return value of true means that the wait condition has been satisfied and the wait is over. A return value of false means that the wait is still ongoing.
  boolean run() {

    if (condition.pollCondition()) {
      if (callback != null) {
        callback.run(condition);
      }
      return true;
    }
    if (runWhile != null) {
      runWhile.run(condition);
    }
    return false;

  }

  public WaitTask(final WaitCondition condition, final WaitCallback callback, final WaitCallback runWhile) throws Exception {
    if (condition == null) {
      throw new Exception("There is no WaitCondition provided");
    }
    this.condition = condition;
    this.callback = callback;
    this.runWhile = runWhile;
  }
}