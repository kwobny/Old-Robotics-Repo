package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

//is a class that contains all the information necessary to complete one wait with callbacks.
//implements the command pattern

public class WaitTask {
  public WaitCondition condition;
  public Callback callback;
  public Callback runWhile; //this is basically an optional callback that continuously runs in the main loop while the wait is ongoing.
  //It is run after the wait condition is tested, so if the wait is determined to be over, then this does not run.

  public boolean endTaskAfter = true;
  //is a property that specifies whether or not to end the wait after the condition has been met.

  //IMPORTANT: this property is not meant to be used by the end user, but only by any other libraries.
  public boolean _isActive = false;

  //A return value of true means that the wait condition has been satisfied and the wait is over. A return value of false means that the wait is still ongoing.
  void run() throws Exception {
    
    if (!_isActive)
      throw new Exception("You cannot run a wait that is not active");
    
    if (condition.pollCondition()) {
      if (callback != null) {
        callback.run();
      }
      if (endTaskAfter) {
        _isActive = false;
        return;
      }
    }
    if (runWhile != null) {
      runWhile.run();
    }

  }

  public void setTask(final WaitCondition cond, final Callback callback) {
    setWait(cond);
    setCallback(callback);
  }
  public void setWait(final WaitCondition cond) throws Exception {
    if (cond == null) {
      throw new Exception("wait condition cannot be null");
    }
    condition = cond;
  }
  public void setCallback(final Callback callback) {
    this.callback = callback;
  }

  public WaitTask(final WaitCondition condition, final Callback callback, final Callback runWhile) throws Exception {
    if (condition == null) {
      throw new Exception("There is no WaitCondition provided");
    }
    this.condition = condition;
    this.callback = callback;
    this.runWhile = runWhile;
  }
  public WaitTask() {
    //
  }
}