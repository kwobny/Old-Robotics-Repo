package lithiumcore.concurrent;

import lithiumcore.utils.Callback;

import lithiumcore.utils.pile.BindingFullPile;
import lithiumcore.utils.pile.BoundedElem;

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