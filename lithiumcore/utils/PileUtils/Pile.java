package LithiumCore.Utils.PileUtils;

import java.util.Collection;

import LithiumCore.Utils.functiontypes.Predicate;

public interface Pile<T> extends Collection<T>, Foreachable<T> {
  //the functions that return a boolean return true if the collection was modified. Returns false if the collection was not modified.

  //removes all of the elements that satisfy the given predicate. (predicate return value of true --> satisfied, false --> not satisfied)
  boolean removeIf(Predicate<? super T> filter);
}