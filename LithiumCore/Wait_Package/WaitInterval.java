package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Wait_Package;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils.BoundedElem;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Callback;

public abstract class WaitInterval extends BoundedElem {

  public Callback callback;
  private boolean hasStarted = false;

  //Override to implement behavior when the interval first starts
  protected abstract void _reset();
  //Override to implement what happens when the interval is polled/run
  protected abstract void _run();

  void run() {
    if (!hasStarted) {
      throw new RuntimeException("You have ran a wait interval before it has been started/calibrated. You cannot do that. To fix, reset the interval at least once (call reset method).");
    }
    _run();
  }

  //Is a reset function. Has to be called once to start, and can be called successive times to reset the interval.
  public final WaitInterval reset() {
    hasStarted = true;
    _reset();
    return this;
  }

}