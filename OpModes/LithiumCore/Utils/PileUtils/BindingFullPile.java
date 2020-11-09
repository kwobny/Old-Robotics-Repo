package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils.Pile_Interfaces.*;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Consumer;

import java.util.*;

//This is a full pile, except that elements are bounded.
//Elements can only belong to 1 instance of a binding pile. This means that they can belong to a binding pile and a hash pile, but not to two binding piles.
//This is the recommended pile type if you don't care that the data type is bound and if you don't care that this class can only work with Bounded Elems.
//You cannot add the same thing more than twice to the pile.
//All elements are iterated in the same order that they are added in.
//You can modify (add to and remove from) the pile while iterating through it.
//This type of pile can only work with BoundedElem and its subclasses.
//This pile uses an arraylist, so nothing fancy is going on.
public class BindingFullPile<T extends BoundedElem> extends SimplePile<T> implements FullPile<T>, DPPile<T> {

  @Override
  public void add(T elem) {
    //check if the element's current pile is not this one
    if (elem.currentPile != null && elem.currentPile != this) {
      throw new RuntimeException("Each bound element can only be assigned to 1 pile.");
    }
    elem.currentPile = this;

    //check if the element is already in the pile
    if (elem.isInPile)
      throw new RuntimeException("You cannot add a bound element that is already in the pile.");
    elem.isInPile = true;

    //check if the element was flagged to be removed
    if (elem.needsRemoving) {
      elem.needsRemoving = false;
    }
    else {
      super.add(elem);
    }
  }

  @Override
  public void remove(T elem) {
    if (!elem.isInPile)
      throw new RuntimeException("You cannot remove a bound element that is already in the pile.");
    elem.isInPile = false;

    elem.needsRemoving = true;
  }

  private boolean isIterating = false;
  private boolean removeCurrElem = false;

  //used to remove the current element while iterating through the pile.
  public void markAsRemove() {
    if (!isIterating) {
      throw new RuntimeException("You cannot use the mark as remove function when the pile is not being iterated.");
    }
    removeCurrElem = true;
  }

  @Override
  public boolean has(final T elem) {
    return elem.currentPile == this && elem.isInPile;
  }

  @Override
  public void forAll(final Consumer<T> consumer) {
    isIterating = true;

    //Iterator<T> iter = elemArr.iterator();
    for (int i = 0; i < elemArr.size(); ++i) {
      //final T elem = iter.next();
      final T elem = elemArr.get(i);

      //remove element if it is marked for removal. Otherwise, run it with consumer.
      if (elem.needsRemoving) {
        elemArr.remove(i);
        --i;
        elem.needsRemoving = false;
      }
      else {
        //run the operation code. In here, the is active properties can be set to true/false/whatever
        consumer.run(elem);
        if (removeCurrElem) {
          elemArr.remove(i);
          --i;
          elem.isInPile = false;
          removeCurrElem = false;
        }
      }

    }

    isIterating = false;

  }

}