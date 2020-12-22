package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

import java.util.Collection;

//This interface describes collections that can contain duplicate elements. Should only be implemented by these types of collections.
//All implementations of duplicates collection have to be collections that can contain duplicates, but the reverse is not true. Not all collections that contain duplicates have to implement this interface.
//The normal remove method described in the collections interface specifies that it removes only one instance of the element. This function removes all instances of the element provided (using the formal equals definition).
public interface DuplicatesCollection<T> extends Collection<T> {
  //the methods below return true if the collection was altered.
  //returns false if the collection was unchanged.

  boolean removeAll(Object element);

  //adds the element only if it is not already present in the collection.
  boolean addStrict(T element);
}

/*

Default implementation of methods:

@Override
public boolean removeAll(final Object element) {
  boolean hasRemovedElements = remove(element);
  if (hasRemovedElements) while (remove(element));
  return hasRemovedElements;
}

@Override
public boolean addStrict(final T element) {
  if (contains(element)) return false;
  return add(element);
}

*/