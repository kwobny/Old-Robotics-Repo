package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Function;

import java.util.*;

//A crucial advantage to this type of pile and its subclasses is that the order that you iterate through the pile is the same as the order which you added the elements in.
//You can add to the pile while iterating, but you can only remove from the pile while iterating if you say true for the can remove while iterating constructor parameter.
//If you choose to be able to remove while iterating, the pile will need more space which is O(n) where n is the number of active iterators at a time.
//You can add the same thing to this pile twice using normal add method. If you do not want this, then use the add strict method.
//Remove and contains methods are innefficient since they require you to loop over the whole pile.
public class SimplePile<T> extends AbstractPile<T> {

  public SimplePile(final boolean canRemoveWhileIterating) {
    if (canRemoveWhileIterating) {
      activeIterators = new ArrayList<T>();
    }
  }
  public SimplePile() {
    this(true);
  }

  //Element array. Can be accessed by subclasses
  protected ArrayList<T> elemArr = new ArrayList<>();
  
  @Override
  public T add(final T elem) {
    elemArr.add(elem);
    return elem;
  }

  //A strict version of add which makes it so that you can only add to the pile if the element is not already in it.
  //Inefficient method
  public T addStrict(final T elem) {
    if (contains(elem)) {
      throw new RuntimeException("You cannot add an element to this pile which is already in the pile using the add strict method. For that functionality, go to normal add method.");
    }
    return add(elem);
  }

  //Inefficient/slow method
  //removes first match of the element. If there is no match, then throw an exception.
  @Override
  public T remove(final T element) {
    //two different methods based on whether or not the pile is being iterated with the for each method.
    if (isIterating) {
      //find element index
      for (int i = 0; i < elemArr.size(); ++i) {
        if (element = elemArr.get(i)) {
          removeElement = i;
          return element;
        }
      }
    }
    else {
      //check if an element was actually removed
      //only return if an element was removed
      if (elemArr.remove(element)) {
        return element;
      }
    }
    //If execution reaches this point, then no element has been removed. Raise an error.
    throw new RuntimeException("You cannot remove an element that is not in the list.");
  }

  //Inefficient method
  @Override
  public boolean contains(final T element) {
    return elemArr.contains(element);
  }

  protected final ArrayList<SimplePileIterator> activeIterators;

  protected class SimplePileIterator implements Iterator<T> {
    private int index = -1;
    private boolean elementRemoved = false;

    void onRemoveElement(final int removedIndex) { //default access
      //
    }

    @Override
    public boolean hasNext() {
      return index < elemArr.size()-1;
    }
    @Override
    public T next() {
      if (!hasNext()) {
        throw new NoSuchElementException("Cannot get next element when iterator is exhausted. For Simple Pile.");
      }
      ++index;
      elementRemoved = false;
      return elemArr.get(index);
    }
    @Override
    public void remove() {
      final boolean isOutOfBounds = index < 0 || index >= elemArr.size();
      if (elementRemoved) {
        throw new IllegalStateException("You cannot remove an element twice. Simple Pile.");
      }
      else if (isOutOfBounds) {
        throw new IllegalStateException("You cannot remove an element when the index is out of bounds. Simple Pile.");
      }
      elemArr.remove(index);
      elementRemoved = true;
    }
  }

  @Override
  public Iterator<T> iterator() {
    final SimplePileIterator newIterator = new SimplePileIterator();
    if (activeIterators != null) {
      activeIterators.add(newIterator);
    }
    return newIterator;
  }

  //these two variables only apply when iterating with the for each function.
  private boolean isIterating = false;
  //while iterating using for each, this specifies what index to remove.
  //If it is set to -1, it means do not remove any index.
  //-2 means remove index that you are currently iterating on.
  private int removeElement = -1;

  @Override
  public void remove() {
    if (!isIterating) {
      throw new RuntimeException("You cannot call the remove current element method when you are not iterating through the pile using the for each method.");
    }
    removeElement = -2;
  }

  @Override
  public void forEach(final Function.Consumer<T> consumer) {
    isIterating = true;

    //Iterator<T> iter = elemArr.iterator();
    for (int i = 0; i < elemArr.size(); ++i) {
      //final T elem = iter.next();
      final T elem = elemArr.get(i);

      //run the operation code. In here, the is active properties can be set to true/false/whatever
      consumer.apply(elem);
      if (removeElement != -1) {
        elemArr.remove(removeElement == -2 ? i : removeElement);
        if (removeElement <= i) {
          --i;
        }
        removeElement = -1;
      }

    }

    isIterating = false;
  }

}