package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

//This class provides some default implementation for piles.
//Should never be used as a standalone type. Only use this when making a new type of pile.
public abstract class AbstractPile<T> implements Pile<T> {
  
  @Override
  public T remove(final T element) {
    if (!removeIfPresent(element)) {
      throw new IllegalArgumentException("You cannot remove an element that is not in the pile. Pile remove.");
    }
    return element;
  }

}