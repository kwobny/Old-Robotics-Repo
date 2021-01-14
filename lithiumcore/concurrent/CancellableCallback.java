package lithiumcore.concurrent;

import lithiumcore.utils.pile.BindingFullPile;
import lithiumcore.utils.pile.BoundedElem;

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