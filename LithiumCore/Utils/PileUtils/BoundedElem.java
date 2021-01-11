package org.firstinspires.ftc.teamcode.LithiumCore.Utils.PileUtils;

//This class is mainly meant to be subclassed, but it can also be used just as is.
//This class is the element which is stored in a binding full pile.
//Each element can only belong to 1 pile.
public class BoundedElem {

  //these properties have default access
  boolean needsRemoving = false;
  boolean isInPile = false;

  BindingFullPile<?> currentPile;

  //Utility methods accessible to any subclass
  protected boolean getIsInPile() {
    return isInPile;
  }

  protected BindingFullPile<?> getOwningPile() {
    return currentPile;
  }
  
}