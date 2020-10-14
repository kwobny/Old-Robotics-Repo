package org.firstinspires.ftc.teamcode.OpModes.motionLibrary.PileUtils;

import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.PileUtils.Pile_Interfaces;

import java.util.*;

//This is a full pile, except that elements can only belong to one specific pile. Hence, they are "bound" to one pile.
//This is the recommended pile type if you don't care that the data type is bound and if you don't care that this class can only work with Bounded Elems.
//You cannot add the same thing more than twice to the pile.
//All elements are iterated in the same order that they are added in.
//This type of pile can only work with BoundedElem and its subclasses.
//This pile uses an arraylist, so nothing fancy is going on.
public class BindingFullPile<T extends BoundedElem> extends SimplePile<T> implements FullPile {

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

  @Override
  public void forAll(final Consumer<T> consumer) {

    Iterator<T> iter = elemArr.iterator();
    while (iter.hasNext()) {
      final T elem = iter.next();
      if (!elem.needsRemoving) {
        //run the operation code. In here, the is active properties can be set to true/false/whatever
        consumer.run(elem);
      }
    }

    //scan and delete
    iter = elemArr.iterator();
    while (iter.hasNext()) {
      final T elem = iter.next();
      if (elem.needsRemoving) {
        elem.needsRemoving = false;
        iter.remove();
      }
    }

  }

}