package org.firstinspires.ftc.teamcode.LithiumCore.Wait_Package;

import org.firstinspires.ftc.teamcode.LithiumCore.Utils.PileUtils.*;
import org.firstinspires.ftc.teamcode.LithiumCore.Utils.Callback;

public class CancellableCallback extends BoundedElem {
  
  public Callback callback;

  public CancellableCallback(final Callback callback) {
    this.callback = callback;
  }

  //Get corresponding pile
  BindingFullPile<CancellableCallback> getCorrPile() { //default access
    return (BindingFullPile<CancellableCallback>) getOwningPile();
  }

}