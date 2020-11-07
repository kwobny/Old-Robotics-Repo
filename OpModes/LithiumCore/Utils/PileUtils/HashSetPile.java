package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils.Pile_Interfaces.*;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Consumer;

import java.util.*;

//This is a full pile which can hold any element. The elements are not bounded.
//You cannot add the same thing more than twice to the pile.
//Elements are not iterated in the same order as they were added.
//This pile uses a hash set internally.
public class HashSetPile<T> implements FullPile<T>, DPPile<T> {

  private HashSet<T> elemSet = new HashSet<>();

  @Override
  public void add(T elem) {
    if (elemSet.contains(elem)) {
      throw new RuntimeException("You cannot add an element to a hash set pile that has already been added.");
    }
    elemSet.add(elem);
  }

  @Override
  public void remove(T elem) {
    if (!elemSet.contains(elem)) {
      throw new RuntimeException("You cannot remove an element from a hash set pile that has not been added.");
    }
    elemSet.remove(elem);
  }

  @Override
  public boolean has(final T elem) {
    return elemSet.has(elem);
  }

  @Override
  public void forAll(final Consumer<T> consumer) {
    Iterator<T> iter = elemSet.iterator();
    while (iter.hasNext()) {
      final T elem = iter.next();
      consumer.run(elem);
    }
  }

}