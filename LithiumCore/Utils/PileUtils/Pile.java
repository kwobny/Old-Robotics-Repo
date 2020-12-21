package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

import java.util.Collection;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.functiontypes.Predicate;

public interface Pile<T> extends Collection<T>, Foreachable<T> {
  //the functions that return a boolean return true if the collection was modified. Returns false if the collection was not modified.

  //removes all of the elements that satisfy the given predicate. (predicate return value of true --> satisfied, false --> not satisfied)
  boolean removeIf(Predicate<? super T> filter);

  //adds the provided element according to the boolean.
  //if present is true, then the function only adds if the element is already in the collection.
  //if present is false, then the function only adds if the element is not already in the collection.
  boolean addIf(T element, boolean present);
}