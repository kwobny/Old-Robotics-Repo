package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

import java.util.ArrayList;

//This is a pile that is run internally using an arraylist.
//Use for cases where you want to conserve memory on small amounts of objects.

public class ArrayPile<T> extends AbstractPile<T> {
  
  ArrayList<T> internalList = new ArrayList<>(); //default access

  @Override
  public T add(final T element) {
    if (contains(element)) {
      throw new IllegalStateException("You cannot add an element to a pile when it is already in it. Array pile add.");
    }
    internalList.add(element);
    return element;
  }

  @Override
  public boolean removeIfPresent(final T element) {
    return internalList.remove(element);
  }

}