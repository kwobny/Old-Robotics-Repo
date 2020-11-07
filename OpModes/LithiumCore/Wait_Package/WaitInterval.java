package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Wait_Package;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils.BoundedElem;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Callback;

public abstract class WaitInterval extends BoundedElem {

  private WaitCondition cond;
  public Callback callback;

  private boolean hasStarted = false;

  protected abstract WaitCondition _incrementCondition();

  void run() {
    if (!hasStarted) {
      throw new RuntimeException("You have ran a wait interval before it has been started.");
    }
    if (cond.pollCondition()) {
      callback.run();
      cond = _incrementCondition();
    }
  }

  public WaitInterval start() {
    if (hasStarted) {
      throw new RuntimeException("You cannot start a wait interval more than once.");
    }
    hasStarted = true;
    cond = _incrementCondition();
    return this;
  }

  public WaitCondition getCond() {
    return cond;
  }

}