package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

//This class is the element which is stored in a binding full pile.
//Each element can only belong to 1 pile.
public abstract class BoundElement<T extends BoundElement<T>> {

  //these properties have default access
  boolean needsRemoving = false;
  boolean isInPile = false;

  BindingFullPile<T> ownerPile;

  //Utility methods accessible to any subclass
  protected boolean getIsInPile() {
    return isInPile;
  }

  protected BindingFullPile<T> getOwnerPile() {
    return ownerPile;
  }
  
}

/*
To subclass:
public class {NAME} extends BoundElement<{NAME}>

Example:
public class SubClass extends BoundElement<SubClass>
*/