package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.functiontypes.Consumer;
import java.util.Iterator;

//This is the actual, main unit that represents a pile.

//A pile is a collection where elements can be accessed solely by their value. All collections are basically piles.

//modifying during iteration, duplicate elements, null elements, memory usage/performance, performance of different methods
//purpose of a pile is to extend the collections concept and to show what methods should be overridden to make a pile/collection.
//Basically a collection. Can also be defined as a collection where elements are accessed solely by their value.

//A pile is something that contains a bunch of things (elements). These elements are referred to and accessed by their value, sort of like a set or collection. There does not have to be a specific order to the elements.
//The difference between this and a set is that piles have way less methods, and the part about duplicate elements below.
//Ideally, there should be no duplicate elements in a pile, but there can be if it is inefficient to check for duplicates.
//There can be one null element in a pile (maybe not always, as pointed out above).
//If an operation is not supported, UnsupportedOperationException is thrown.
//The only required methods are iterator and add.
public interface Pile<T> extends PileInterface<T>, Iterable<T> {

  //returns an iterator to be used to iterate over the pile.
  //Required method
  @Override
  public Iterator<T> iterator();

  //Remove current element method
  //An overloaded remove function which should and can only be used when ITERATING through the pile via the FOR EACH function. Removes the element which the pile is currently on in its iterating sequence.
  //If the element is removed twice, the function throws an exception.
  public void remove();
  //Another way of iterating through the pile where the pile itself handles iteration.
  public void forEach(Consumer<? super T> consumer);

  //removes all elements in pile.
  public void clear();

  //returns the number of elements in the pile.
  public int size();
  //returns true if the pile is empty. False if the pile is not empty.
  public boolean isEmpty();

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