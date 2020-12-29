package LithiumCore.Utils.PileUtils;

import java.util.Collection;

//This interface describes collections that can contain duplicate elements. Should only be implemented by these types of collections.
//All implementations of duplicates collection have to be collections that can contain duplicates, but the reverse is not true. Not all collections that contain duplicates have to implement this interface.
public interface DuplicatesCollection<T> extends Collection<T> {
  //the methods below return true if the collection was altered.
  //returns false if the collection was unchanged.

  //The normal remove method described in the collections interface specifies that it removes only one instance of the element. This function removes every element in the collection that is equal to the element provided (using the formal equals definition). Basically removes all instances of an element.
  boolean removeEvery(Object element);

  //adds the element only if it is not already present in the collection. Adds the element if there is no duplicate in the collection.
  boolean addStrict(T element);

  //The normal add method declared in the collection interface allows duplicate elements to be added.
}

/*

Default implementation of methods:

@Override
public boolean removeEvery(final Object element) {
  boolean removed = false;
  while (remove(element)) removed = true;
  return removed;
}

alternative implementation:

@Override
public boolean removeEvery(final Object element) {
  boolean removed = false;
  final Iterator<T> each = iterator();
  while (each.hasNext()) {
    if (Objects.equals(each.next(), element)) {
      each.remove();
      removed = true;
    }
  }
  return removed;
}

@Override
public boolean addStrict(final T element) {
  if (contains(element)) return false;
  return add(element);
}

*/