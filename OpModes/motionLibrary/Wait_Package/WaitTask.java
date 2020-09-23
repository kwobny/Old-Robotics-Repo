package org.firstinspires.ftc.teamcode.OpModes.motionLibrary.Wait_Package;

import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.GenericOperation.*;

//is a class that contains all the information necessary to complete one wait with callbacks.
//implements the command pattern

public class WaitTask extends Operation {
  public WaitCondition condition;
  public Callback callback;
  public Callback runWhile; //this is basically an optional callback that continuously runs in the main loop while the wait is ongoing.
  //It is run after the wait condition is tested, so if the wait is determined to be over, then this does not run.

  public boolean autoEndTask = true;
  //is a property that specifies whether or not to automatically end the wait after the condition has been met.

  //IMPORTANT: this property is not meant to be used by the end user, but only by any other libraries.
  private boolean _isActive2 = false;

  //A return value of true means that the wait condition has been satisfied and the wait is over. A return value of false means that the wait is still ongoing.
  //the three methods below are not meant to be used commonly by the user.
  public void _run() {
    
    if (!_isActive2)
      throw new RuntimeException("You cannot run a wait that is not running.");
    if (condition == null) {
      throw new RuntimeException("There is no wait condition running. You always need a wait condition.");
    }
    
    if (condition.pollCondition()) {
      if (callback != null) {
        callback.run();
      }
      if (autoEndTask) {
        _isActive2 = false;
        return;
      }
    }
    if (runWhile != null) {
      runWhile.run();
    }

  }

  public void _markAsAdd() {
    _isActive2 = true;
  }
  public boolean _isRunning() {
    return _isActive2;
  }

  public void setTask(final WaitCondition cond, final Callback callback) {
    setCondition(cond);
    setCallback(callback);
  }
  public void setCondition(final WaitCondition cond) {
    condition = cond;
  }
  public void setCallback(final Callback callback) {
    this.callback = callback;
  }

  public WaitTask(final WaitCondition condition, final Callback callback, final Callback runWhile) {
    this.condition = condition;
    this.callback = callback;
    this.runWhile = runWhile;
  }
  public WaitTask() {
    //
  }
}