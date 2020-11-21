package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Function;
import java.util.Iterator;

//A pile is something that contains a bunch of things (elements). These elements are referred to and accessed by their value, sort of like a set or collection. There does not have to be a specific order to the elements.
//The difference between this and a set is that piles have way less methods and do not have to be strictly unordered.
//If an operation is not supported, UnsupportedOperationException is thrown.
//The only required methods are iterator and add.
public interface Pile<T> extends Iterable<T> {

  //returns an iterator to be used to iterate over the pile.
  //Required method
  public Iterator<T> iterator();

  //Remove current element method
  //An overloaded remove function which should only be used when ITERATING through the pile via the FOR EACH function. Removes the element which the pile is currently on in its iterating sequence.
  public void remove();
  //Another way of iterating through the pile where th pile itself handles iteration
  public void forEach(Function.Consumer<T> consumer);

  //methods to add and remove elements.
  //They should normally return the current element (this), but technically don't have too.

  //Add method is required
  public T add(T element);
  public T remove(T element);

  //returns a boolean signifying whether or not the element is in the pile.
  //true means the element is present in the pile, false means the element is not present.
  public boolean contains(T element);

}

/*

Three iterator methods:

@Override
public boolean hasNext() {
  //
}
@Override
public T next() {
  //
}
@Override
public void remove() {
  //
}

*/