package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Wait_Package;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils.BoundedElem;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Callback;

public abstract class WaitInterval extends BoundedElem {

  private WaitCondition cond;
  public Callback callback;

  private boolean hasCalibrated = false;

  protected abstract WaitCondition _incrementCondition();

  void run() {
    if (!hasCalibrated) {
      throw new RuntimeException("You have ran a wait interval before it has been started/calibrated. You cannot do that.");
    }
    if (cond.pollCondition()) {
      callback.run();
      cond = _incrementCondition();
    }
  }

  //Calibrates/starts the wait interval.
  //Can be called again to restart the interval.
  public WaitInterval calibrate() {
    hasCalibrated = true;
    cond = _incrementCondition();
    return this;
  }

  public WaitCondition getCond() {
    return cond;
  }

}