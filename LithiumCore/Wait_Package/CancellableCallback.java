package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Wait_Package;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils.BoundedElem;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Callback;

public class CancellableCallback extends BoundedElem {
  
  public Callback callback;

  public CancellableCallback(final Callback callback) {
    this.callback = callback;
  }

}