package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Consumer;

import java.util.*;

//This is a full pile, except that elements are bounded.
//Elements can only belong to 1 instance of a binding pile. This means that they can belong to a binding pile and a hash pile, but not to two binding piles.
//This is the recommended pile type if you don't care that the data type is bound and if you don't care that this class can only work with Bounded Elems.
//You cannot add the same reference (same exact object) more than once to the pile.
//All elements are iterated in the same order that they are added in.
//You can modify (add to and remove from) the pile while iterating through it.
//This type of pile can only work with BoundElement and its subclasses.
public class BindingPile<T extends BoundElement<T>> extends SimplePile<T> {
  
  //This method binds the provided element to the current pile (this).
  //If the element is already bound to another pile, the method throws an error.
  public void bindElement(final T element) {
    if (element.ownerPile != null) {
      throw new IllegalArgumentException("You cannot bind an element which is already bound. Binding pile bind element.");
    }
    element.ownerPile = this;
  }

  @Override
  public T add(final T elem) {
    //check if the element's current pile is not this one
    if (elem.ownerPile != null && elem.ownerPile != this) {
      throw new RuntimeException("Each bound element can only be assigned to 1 pile.");
    }
    elem.ownerPile = this;

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
    return elem;
  }

  @Override
  public T remove(T elem) {
    if (!elem.isInPile)
      throw new RuntimeException("You cannot remove a bound element that is not in the pile.");
    elem.isInPile = false;
    elem.needsRemoving = true;

    return elem;
  }

  @Override
  public boolean contains(final T elem) {
    return elem.ownerPile == this && elem.isInPile;
  }

  //binding pile iterator
  protected class BPIterator extends SPIterator {
    @Override
    public boolean hasNext() {
      if (!it.hasNext()) return false;

      final T nextElement = it.next();
      if (nextElement.needsRemoving) {
        it.remove();
        nextElement.needsRemoving = false;
      }
    }
    @Override
    public T next() {
      //
    }
    @Override
    public void remove() {
      //
    }
  }

  @Override
  public Iterator<T> iterator() {
    return new BPIterator();
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