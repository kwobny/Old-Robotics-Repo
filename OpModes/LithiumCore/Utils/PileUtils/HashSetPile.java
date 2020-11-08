package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils.Pile_Interfaces.*;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Consumer;

import java.util.*;

//This is a full pile which can hold any element. The elements are not bounded.
//You cannot modify (add to or remove from) the hashset while iterating through it. However, you can remove the element that is currently being iterated, while iterating.
//You cannot add the same thing more than twice to the pile.
//Elements are not iterated in the same order as they were added.
//This pile uses a hash set internally.
public class HashSetPile<T> implements FullPile<T>, DPPile<T> {

  private HashSet<T> elemSet = new HashSet<>();

  private boolean isIterating = false;
  private boolean removeCurrElem = false;

  @Override
  public void add(T elem) {
    if (isIterating) {
      throw new RuntimeException("You cannot add an element while iterating through a hashset pile.");
    }
    if (elemSet.contains(elem)) {
      throw new RuntimeException("You cannot add an element to a hash set pile that has already been added.");
    }
    elemSet.add(elem);
  }

  @Override
  public void remove(T elem) {
    if (isIterating) {
      throw new RuntimeException("You cannot remove an element while iterating through a hashset pile.");
    }
    if (!elemSet.contains(elem)) {
      throw new RuntimeException("You cannot remove an element from a hash set pile that has not been added.");
    }
    elemSet.remove(elem);
  }

  //used to remove the current element while iterating through the pile.
  public void markAsRemove() {
    if (!isIterating) {
      throw new RuntimeException("You cannot use the mark as remove function when the pile is not being iterated.");
    }
    removeCurrElem = true;
  }

  @Override
  public boolean has(final T elem) {
    return elemSet.has(elem);
  }

  @Override
  public void forAll(final Consumer<T> consumer) {
    isIterating = true;
    Iterator<T> iter = elemSet.iterator();
    while (iter.hasNext()) {
      final T elem = iter.next();
      consumer.run(elem);
      if (removeCurrElem) {
        iter.remove();
        removeCurrElem = false;
      }
    }
    isIterating = false;
  }

}