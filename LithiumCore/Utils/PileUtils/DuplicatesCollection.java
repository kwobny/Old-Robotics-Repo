package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

import java.util.Collection;

//This interface describes collections that can contain duplicate elements. Should only be implemented by these types of collections.
//All implementations of duplicates collection have to be collections that can contain duplicates, but the reverse is not true. Not all collections that contain duplicates have to implement this interface.
//The normal remove method described in the collections interface specifies that it removes only one instance of the element. This function removes all instances of the element provided (using the formal equals definition).
public interface DuplicatesCollection<T> extends Collection<T> {
  //returns true if elements were removed.
  //returns false if no elements were removed/the collection was unchanged.
  boolean removeAll(Object element);
}

/*

Default implementation of remove all method:

public boolean removeAll(final Object element) {
  boolean hasRemovedElements = remove(element);
  if (hasRemovedElements) while (remove(element));
  return hasRemovedElements;
}

*/