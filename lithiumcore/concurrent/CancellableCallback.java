package org.firstinspires.ftc.teamcode.lithiumcore.concurrent;

import org.firstinspires.ftc.teamcode.lithiumcore.utils.pile.BindingFullPile;
import org.firstinspires.ftc.teamcode.lithiumcore.utils.pile.BoundedElem;

public class CancellableCallback extends BoundedElem {
  
  public Runnable callback;

  public CancellableCallback(final Runnable callback) {
    this.callback = callback;
  }

  //Get corresponding pile
  BindingFullPile<CancellableCallback> getCorrPile() { //default access
    return getOwningPile();
  }

}