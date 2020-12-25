package LithiumCore.Wait_Package;

import LithiumCore.Utils.Callback;

import LithiumCore.Utils.PileUtils.BindingFullPile;
import LithiumCore.Utils.PileUtils.BoundedElem;

public class CancellableCallback extends BoundedElem {
  
  public Callback callback;

  public CancellableCallback(final Callback callback) {
    this.callback = callback;
  }

  //Get corresponding pile
  BindingFullPile<CancellableCallback> getCorrPile() { //default access
    return getOwningPile();
  }

}