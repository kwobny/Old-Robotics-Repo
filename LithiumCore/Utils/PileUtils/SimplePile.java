package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Function;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.NativeUtils.BuffedLinkedList;

import java.util.*;

//A crucial advantage to this type of pile and its subclasses is that you iterate through the pile in the same order in which you added the elements in.
//You can add the same thing to this pile twice using normal add method. If you do not want this, then use the add strict method.
//Remove and contains methods are innefficient since they require you to loop over the whole pile.
public class SimplePile<T> extends AbstractPile<T> {

  //Element list. Can be accessed by subclasses
  protected BuffedLinkedList<T> elemList = new BuffedLinkedList<>();
  
  @Override
  public T add(final T elem) {
    return elemList.add(elem);
  }

  //A strict version of add which makes it so that you can only add to the pile if the element is not already in it.
  //Inefficient method
  public T addStrict(final T elem) {
    if (contains(elem)) {
      throw new RuntimeException("You cannot add an element to this pile which is already in the pile using the add strict method. For that functionality, go to normal add method. Simple pile.");
    }
    return add(elem);
  }

  //Inefficient/slow method
  //removes first match of the element. If there is no match, then throw an exception.
  @Override
  public T remove(final T element) {
    if (!removeIfPresent(element)) {
      throw new RuntimeException("You cannot remove an element that is not in the list, using the normal remove method. Simple pile remove.");
    }
    return element;
  }

  //removes the first match/occurance of an element.
  @Override
  public boolean removeIfPresent(final T element) {
    return elemList.remove(element);
  }

  @Override
  public void clear() {
    elemList.clear();
  }

  //Inefficient method
  @Override
  public boolean contains(final T element) {
    return elemList.contains(element);
  }

  @Override
  public int size() {
    return elemList.size();
  }
  @Override
  public boolean isEmpty() {
    return elemList.isEmpty();
  }

  @Override
  public Iterator<T> iterator() {
    return new Iterator<T>() {
      private BuffedLinkedList<T>.Iter it = elemList.iterator();

      @Override
      public boolean hasNext() {
        return it.hasNext();
      }
      @Override
      public T next() {
        try {
          return it.next();
        }
        catch (RuntimeException e) {
          throw new RuntimeException("You have tried to call the next method when there were no more elements left to iterate through. The size of the pile is " + size() + ". Simple pile iterator next", e);
        }
      }
      @Override
      public void remove() {
        try {
          it.remove();
        }
        catch (RuntimeException e) {
          throw new RuntimeException("Simple pile iterator remove", e);
        }
      }
    };
  }

  //This variable only applies when iterating with the for each function.
  //If current iterator is null, then it means that the user is not currently iterating with the for each function.
  private BuffedLinkedList<T>.Iter currentIterator;

  @Override
  public void remove() {
    if (currentIterator == null) {
      throw new RuntimeException("You cannot call the remove current element method when you are not iterating through the pile using the for each method.");
    }
    try {
      currentIterator.remove();
    }
    catch (RuntimeException e) {
      throw new RuntimeException("You cannot call the remove current element method twice. Why? Because you cannot remove the current element when it is already removed.", e);
    }
  }

  @Override
  public void forEach(final Function.Consumer<T> consumer) {
    for (final BuffedLinkedList<T>.Iter it = elemList.iterator(); it.hasNext();) {
      currentIterator = it;
      consumer.apply(it.next());
    }
    currentIterator = null;
  }

}