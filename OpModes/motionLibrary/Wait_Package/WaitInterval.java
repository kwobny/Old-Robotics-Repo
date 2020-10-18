package org.firstinspires.ftc.teamcode.OpModes.motionLibrary.Wait_Package;

import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.PileUtils.BoundedElem;
import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.Utils.Callback;

public abstract class WaitInterval extends BoundedElem {

  protected WaitCondition cond;
  public Callback callback;

  public WaitInterval() {
    _incrementCondition();
  }

  protected abstract void _incrementCondition();

  void run() {
    if (cond.pollCondition()) {
      callback.run();
      _incrementCondition();
    }
  }

  public WaitCondition getCond() {
    return cond;
  }

}