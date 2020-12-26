package LithiumCore.Utils.PileUtils;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Objects;

//This is a pile that is run internally using an arraylist.
//Use for cases where you want to conserve memory on small amounts of objects.

//Allows duplicate and null elements.

//This is a FIFI (first in first iterated) structure. The first element added gets iterated over first, last element gets iterated over last, etc.
/*
You can modify this pile while iterating, to a certain extent.
  You can always add elements while iterating, regardless of the number of active iterators.
  However, you cannot use an iterator if you removed an element from the pile while it was active, unless
    that iterator is the remove safe iterator, or
    the next method has not been called at all yet.
  You can claim remove safety by calling the claim remove safety method on the corresponding iterator, and you can unclaim it by calling the corresponding unclaim method.

  By default, every iterator automatically unclaims the remove safety. You can set this to not happen (look at comments about the public property below).
  The default iterator method automatically tries to claim remove safety for the iterator which it creates. You can use the other iterator method if you don't want this.
*/

public class ArrayPile<T> extends AbstractPile<T> implements DuplicatesCollection<T> {
  
  private ArrayList<T> internalList;
  private Iter removeSafeIterator = null;
  private int removeCount = 0; //stores the number of times the remove operation was executed.

  public ArrayPile() {
    internalList = new ArrayList<>();
  }
  public ArrayPile(final int initialCapacity) {
    internalList = new ArrayList<>(initialCapacity);
  }

  @Override
  public boolean add(T element) {
    return internalList.add(element);
  }

  @Override
  public boolean addStrict(final T element) {
    if (contains(element)) return false;
    return add(element);
  }

  @Override
  public boolean remove(Object element) {
    final Iterator<T> each = iterator();
    while (each.hasNext()) {
      if (Objects.equals(each.next(), element)) {
        each.remove();
        return true;
      }
    }
    return false;
  }

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
  public boolean contains(Object element) {
    return internalList.contains(element);
  }

  //The only thing that this iterator does not allow is removing elements from the pile while iterating,
  //  except when the iterator is remove safe, or
  //  except when the iteration has not been started (next has not been called yet).
  public class Iter extends Iterator<T> {
    private int index = -1;
    private int expectedRemoveCount = removeCount;
    private boolean isElementRemoved; //stores whether or not the last element returned by this iterator was removed.

    //auto unclaim remove safety.
    //is a public property which can be used by the client. Default is true.
    //If it is true, then the iterator automatically unclaims the remove safety when the has next function first returns false.
    public boolean autoUnclaimRS = true;

    Iter() { //default access. Only meant to be instantiated by array pile.
      //
    }
    Iter(final boolean autoUnclaimRS) {
      this.autoUnclaimRS = autoUnclaimRS;
    }

    private void checkForCoRemoval() {
      if (expectedRemoveCount != removeCount) {
        //iteration has not started yet. Do not complain.
        if (index == -1) {
          expectedRemoveCount = removeCount;
          return;
        }
        //deliberately throws an error even if the size of the pile is 0. Does not allow this case.
        throw new ConcurrentModificationException("You cannot use a remove unsafe iterator while modifying the list. Also, the iterator was " + (isRemoveSafe() ? "a" : "NOT a") + " remove safe iterator. Array pile iterator.");
      }
    }

    @Override
    public boolean hasNext() {
      checkForCoRemoval();
      if (index < internalList.size()-1) return true;
      //else, if the iterator does not have a next element.
      if (autoUnclaimRS) unclaimRemoveSafety();
      return false;
    }
    @Override
    public T next() {
      checkForCoRemoval();
      ++index;
      isElementRemoved = false;
      try {
        return internalList.get(index);
      }
      catch (Exception e) {
        throw new NoSuchElementException("array pile iterator next", e);
        //You likely invoked the next method when there were no more elements to iterate over.
      }
    }
    @Override
    public void remove() {
      checkForCoRemoval();
      if (isElementRemoved)
        throw new IllegalStateException("The element you wanted to remove is already removed. This could be because you already called the remove method before, or because another iterator / the list removed the same element as the one you are trying to remove. Array pile iterator remove.");
      if (index == -1)
        throw new IllegalStateException("You cannot use the array pile iterator remove method when you have not even called next yet.");

      internalList.remove(index);

      if (removeSafeIterator != null && removeSafeIterator != this) {
        if (removeSafeIterator.index >= index)
          --removeSafeIterator.index;
        ++removeSafeIterator.expectedRemoveCount;
        if (removeSafeIterator.index == index)
          removeSafeIterator.isElementRemoved = true;
      }

      isElementRemoved = true;
      ++expectedRemoveCount; ++removeCount;
      --index;
    }

    //returns whether or not the last element returned by the iterator was removed (either by the iterator, some other iterator, or the pile).
    //returns true if the last element was removed.
    //returns false if not.
    public boolean wasElementRemoved() {
      return isElementRemoved;
    }

    //returns whether or not the iterator is remove safe after the call.
    //returns true if the iterator is remove safe after the call, false if not.
    public boolean claimRemoveSafety() {
      if (removeSafeIterator == null) {
        removeSafeIterator = this;
        return true;
      }
      if (removeSafeIterator == this)
        return true;
      
      //returns false if another iterator has already claimed remove safety.
      return false;
    }

    //always make sure to unclaim the remove safety when you are not using the iterator anymore.
    public void unclaimRemoveSafety() {
      if (removeSafeIterator == this)
        removeSafeIterator = null;
    }

    //returns true if this iterator is remove safe.
    //false if not.
    public boolean isRemoveSafe() {
      return removeSafeIterator == this;
    }

  }

  @Override
  public Iter iterator() {
    final Iter retIter = new Iter();
    retIter.claimRemoveSafety();
    return retIter;
  }

  public Iter nonRSIterator() {
    return new Iter();
  }


  @Override
  public void clear() {
    internalList.clear();
    ++removeCount;

    if (removeSafeIterator.index != -1) {
      removeSafeIterator.isElementRemoved = true;
    }
    removeSafeIterator.index = -1;
    ++removeSafeIterator.expectedRemoveCount;
  }

  @Override
  public int size() {
    return internalList.size();
  }

  @Override
  public boolean isEmpty() {
    return internalList.isEmpty();
  }

  //The methods below are used to adjust the capacity of the pile. Are delegate methods to the actual methods in the underlying arraylist.

  public void trimToSize() {
    internalList.trimToSize();
  }

  public void ensureCapacity(final int minCapacity) {
    internalList.ensureCapacity(minCapacity);
  }

}