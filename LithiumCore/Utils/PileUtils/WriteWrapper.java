package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

import java.util.Collection;
import java.util.Objects;

//This class is a wrapper class that wraps over a collection. It acts comparatively as a setter for a property. You can only write to/alter the wrapped collection, and you cannot see what elements are in the collection.
//You can only mess with elements that you as the client own.
//This class is immutable.
public class WriteWrapper<T> {

  final Collection<T> collection; // is default access so that dp write wrapper can access.

  public WriteWrapper(final Collection<T> collection) {
    this.collection = Objects.requireNonNull(collection, "The collection you provided into the write wrapper constructor was null. This is illegal.");
  }
  
  public boolean add(T element) {
    return collection.add(element);
  }

  public boolean remove(Object element) {
    return collection.remove(element);
  }

  public boolean contains(Object element) {
    return collection.contains(element);
  }

}