package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Wait_Package;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils.BoundedElem;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Callback;

public abstract class WaitInterval extends BoundedElem {

  private WaitCondition cond;
  public Callback callback;

  private boolean hasStarted = false;

  protected abstract WaitCondition _incrementCondition();
  //Can be overrided to implement custom reset behavior
  protected void _reset() {
    cond = _incrementCondition();
  }

  void run() {
    if (!hasStarted) {
      throw new RuntimeException("You have ran a wait interval before it has been started/calibrated. You cannot do that. To fix, reset the interval at least once (call reset method).");
    }
    if (cond.pollCondition()) {
      callback.run();
      cond = _incrementCondition();
    }
  }

  //Is a reset function. Has to be called once to start, and can be called successive times to reset the interval.
  public final WaitInterval reset() {
    hasStarted = true;
    _reset();
    return this;
  }

  public WaitCondition getCond() {
    return cond;
  }

}