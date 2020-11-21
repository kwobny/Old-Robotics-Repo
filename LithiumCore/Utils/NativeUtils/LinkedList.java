package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.NativeUtils;

import java.util.*;

//The whole point of making this linked list implementation is so that you can iterate through the list while mutating it.
//You should be able to mutate the list while iterating through it.
//This is a doubly linked list.
public class LinkedList<T> implements Iterable<T> {

  private class Node {
    T data;
    Node prev;
    Node next;
    boolean isRemoved = false;
    public Node() {
      //
    }
    public Node(final T data) {
      this.data = data;
    }
    public Node(final T data, final Node prev, final Node next) {
      this.data = data;
      this.prev = prev;
      this.next = next;
    }
  }

  //This iterator does not implement some of the required methods from the list iterator interface.
  public class ElemIterator implements ListIterator<T> {

    private boolean removedPreviousElement = false;
    private boolean removedNextElement = false;
    private boolean nextCalledLast; //specifies which iterating method (next or previous) was called last. True means next, false means previous.
    private boolean incrementedYet = false; //specifies if the next or previous methods have been called since the last set position command.

    private Node pointer = null;
    //Is a pointer to the iterator cursor
    //pointer is the node pointed to by the next method. I.e. the pointer variable is the node to to the right/tail direction of the cursor.
    //head corresponds with left most edge of list, null corresponds with right most edge.

    private int expectedIndex; //only used for next and previous index functions. Is the index of the pointer. Is index of element returned by next function
    private int expectedModCount; //does not represent reassigning node values.

    ElemIterator(final int expectedModCount) { //default access. Cannot be created outside of package.
      this(expectedModCount, 0);
    }
    //"The specified index indicates the first element that would be returned by an initial call to next"
    ElemIterator(final int expectedModCount, final int index) { //default access
      setPosition(index);
      this.expectedModCount = expectedModCount;
    }

    @Override
    public boolean hasNext() {
      return cursorHasNext(pointer);
    }
    @Override
    public boolean hasPrevious() {
      return cursorHasPrevious(pointer);
    }

    @Override
    public int nextIndex() {
      if (indexIsKnown()) {
        return expectedIndex;
      }
      //if index is not known
      throw new ConcurrentModificationException("You cannot request the next index if the list was modified while iterating. This is because the index is not known.");
    }
    @Override
    public int previousIndex() {
      if (indexIsKnown()) {
        return expectedIndex-1;
      }
      //if index is not known
      throw new ConcurrentModificationException("You cannot request the previous index if the list was modified while iterating. This is because the index is not known.");
    }
    
    //returns if the index of the current element is known, and if user can use the next index and previous index methods.
    public boolean indexIsKnown() {
      return expectedModCount == listModificationCount;
    }

    //sets the iterator's position to some index in the list.
    //the index parameter describes the index of the element returned by a subsequent call to next.
    public void setPosition(final int index) {
      if (index < 0 || index > size) {
        throw new IndexOutOfBoundsException("Linked list iterator set position method (maybe from iterator constructor?)");
      }
      //index is closer to end of list
      if (index >= size/2) {
        pointer = null;
        previous(size-index);
      }
      //index is closer to start of list
      else {
        pointer = head;
        next(index);
      }

      expectedIndex = index;
      incrementedYet = false;
    }

    //displaces (either goes forward or behind) the iterator
    public T displace(final int steps) {
      if (steps < 0) {
        return previous(-steps);
      }
      //steps >= 0
      else {
        return next(steps);
      }
    }

    //returns the node on the head side of the cursor
    @Override
    public T previous() {
      readjustPosition();

      if (!hasPrevious()) {
        throw new NoSuchElementException("linked list iterator previous");
      }

      pointer = getPreviousPart(pointer);

      removedPreviousElement = false;
      removedNextElement = false;
      nextCalledLast = false;
      --expectedIndex;
      incrementedYet = true;

      return pointer.data;
    }
    //same as next step method, but goes the other way.
    public T previous(final int steps) {
      if (steps < 0) {
        throw new IndexOutOfBoundsException("Linked list");
      }
      if (steps == 0) {
        return null;
      }
      for (int i = 0; i < steps-1; ++i) {
        previous();
      }
      return previous();
    }
    
    //returns the node on the tail side of the cursor.
    @Override
    public T next() {
      readjustPosition();

      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      final T data = pointer.data;

      removedPreviousElement = false;
      removedNextElement = false;
      nextCalledLast = true;
      ++expectedIndex;
      incrementedYet = true;

      pointer = pointer.next;
      return data;
    }
    //The step specifies the offset of the element to return. A step of 1 is the same as calling the normal next method.
    //Returns null if step is 0, and the iterator doesn't move.
    public T next(final int steps) {
      if (steps < 0) {
        throw new IndexOutOfBoundsException("next Linked list");
      }
      if (steps == 0) {
        return null;
      }
      for (int i = 0; i < steps-1; ++i) {
        next();
      }
      return next();
    }

    //allows you to peek what elements are ahead/behind the iterator without mutating it
    //These methods allow you to iterate through the list but does not mutate the iterator.
    //If a peek method reaches either end of the list, it returns null.
    public T peek(final int steps) {
      if (steps < 0) {
        return peekBehind(-steps);
      }
      //steps >= 0
      else {
        return peekAhead(steps);
      }
    }

    //get previous element without mutating iterator
    public T peekBehind() {
      readjustPosition();
      if (!hasPrevious()) {
        return null;
      }

      return getPreviousPart(pointer).data;
    }
    //a step of 0 means return null. a step of 1 is just a normal peek behind method
    public T peekBehind(int steps) {
      readjustPosition();
      if (steps < 0) {
        throw new IndexOutOfBoundsException("Bruh why are you providing a negative step? peek behind Linked list.");
      }
      if (steps == 0) {
        return null;
      }
      Node tempPointer = pointer;
      while (true) {
        if (!cursorHasPrevious(tempPointer)) {
          return null;
        }
        tempPointer = tempPointer.prev;
        if (steps == 1) {
          return tempPointer.data;
        }

        --steps;
      }
    }

    //get next element without mutating iterator
    public T peekAhead() {
      readjustPosition();
      if (!hasNext()) {
        return null;
      }
      return pointer.data;
    }
    public T peekAhead(int steps) {
      readjustPosition();
      if (steps < 0) {
        throw new IndexOutOfBoundsException("Bruh why are you providing a negative step? peek ahead Linked list.");
      }
      if (steps == 0) {
        return null;
      }
      Node tempPointer = pointer;
      while (true) {
        if (!cursorHasNext(tempPointer)) {
          return null;
        }
        if (steps == 1) {
          return tempPointer.data;
        }
        tempPointer = tempPointer.next;

        --steps;
      }
    }

    //DESTRUCTIVE / LIST MUTATING METHODS

    //Adds an element after previous element and before next element.
    //Adds the element before the implicit cursor.
    //In the case of a unidirectional iterator, the element is added after the last element returned, and the iterator does not iterate over it.
    @Override
    public void add(final T elem) {
      //readjust position
      readjustPosition();
      
      final Node prevNode = getPreviousPart(pointer); //node returned by the previous method
      final Node newNode = new Node(elem, prevNode, pointer);
      if (hasPrevious()) {
        //connect node behind if there is a node behind.
        prevNode.next = newNode;
      }
      else {
        //set head to new node
        head = newNode;
      }
      if (hasNext()) {
        //connect node ahead if there is one.
        pointer.prev = newNode;
      }
      else {
        //set tail to new node
        tail = newNode;
      }

      ++size;

      ++expectedIndex;
      ++expectedModCount; ++listModificationCount;

    }

    //You can remove elements after adding them through iterator, contrary to what list iterator documentation says.
    //Removes the element that was returned by the last call to next or previous. In the case of a unidirectional iterator, it just removes the current element.
    //Removes the element towards the direction of the last call. If next was called last, then it removes the element towards the head direction, and vise versa. This also applies regardless of if you add an element.
    //If an element is added and the last call was next, then the new element is removed. But if the last call was previous, then the element towards the tail direction is removed.
    //You cannot use the blank remove method if you haven't called previous or next yet.

    //You can only remove the element once.

    @Override
    public void remove() {
      if (!incrementedYet) {
        throw new IllegalStateException("You cannot use the blank remove method when next or previous has not been called yet. Linked list iterator remove.");
      }
      
      if (nextCalledLast) {
        //remove previous element
        removePrevious();
      }
      else {
        //remove next element
        removeNext();
      }
    }

    public void removePrevious() {
      if (removedPreviousElement) {
        throw new IllegalStateException("You cannot remove the previous element more than once. Linked list iterator remove previous.");
      }

      readjustPosition();

      //remove element before the iterator cursor (previous element)
      if (!hasPrevious()) {
        throw new IllegalStateException("cannot remove the previous element when there is none. remove linked list iterator. If being used as iterator: cannot remove the current element because iterator has not been started yet.");
      }

      //order of these operations is important and purposeful.
      final Node removeNode = getPreviousPart(pointer);
      removeNode.isRemoved = true;
      if (hasNext()) { //reconnect next node if there is one
        pointer.prev = removeNode.prev;
      }
      else {
        //set tail to node before previous part / node to be removed.
        tail = removeNode.prev;
      }
      if (cursorHasPrevious(removeNode)) { //reconnect node that is previous to the removed node if there is one.
        getPreviousPart(removeNode).next = pointer;
      }
      else {
        //set head to pointer
        head = pointer;
      }

      //no need to readjust iterator's position

      --expectedIndex;

      //finalize some stuff
      removedPreviousElement = true;
      --size;
      ++expectedModCount; ++listModificationCount;
    }
    public void removeNext() {
      if (removedNextElement) {
        throw new IllegalStateException("You cannot remove the next element more than once. Linked list iterator remove next.");
      }

      readjustPosition();

      //remove element after the iterator cursor (next element)
      if (!hasNext()) {
        throw new IllegalStateException("You cannot remove the next element when there is none. Remove link list iterator.");
      }

      pointer.isRemoved = true;
      if (cursorHasNext(pointer.next)) { //reconnect node after next node (removing node) if there is one
        pointer.next.prev = pointer.prev;
      }
      else {
        //set tail to node before removed node
        tail = getPreviousPart(pointer);
      }
      if (hasPrevious()) { //reconnect previous node if there is one
        getPreviousPart(pointer).next = pointer.next;
      }
      else {
        //set head to pointer next
        head = pointer.next;
      }

      //readjust iterator's position to be correct.
      readjustPosition();

      //finish up stuff
      removedNextElement = true;
      --size;
      ++expectedModCount; ++listModificationCount;
    }

    //Replaces the value of the node in the direction of the last node returned by previous or next. Add operation does not affect it.
    //You cannot use the blank set function if previous or next has not been called yet, since the function does not know what the last element was.

    @Override
    public void set(final T value) {
      if (!incrementedYet) {
        throw new IllegalStateException("You cannot use the no parameter set function without calling previous or next first. Linked list iterator set");
      }
      if (nextCalledLast) {
        //set previous
        setPrevious(value);
      }
      else {
        //set next
        setNext(value);
      }
    }

    public void setPrevious(final T value) {
      readjustPosition();
      
      if (!hasPrevious()) {
        throw new IllegalStateException("You cannot set the previous element if there is no previous element. Linked list iterator set previous.");
      }

      getPreviousPart(pointer).data = value;
    }
    public void setNext(final T value) {
      readjustPosition();

      if (!hasNext()) {
        throw new IllegalStateException("You cannot set the next element if there is no next element. Linked list iterator set next.");
      }

      pointer.data = value;
    }

    //Adjust the iterator's position if it's pointer variable is on a removed/nonexistant element.
    private void readjustPosition() {
      while (pointer != null && pointer.isRemoved) {
        pointer = pointer.next;
      }
    }

    //These functions expect the cursor / input pointer to represent the same thing in the list as the pointer variable.
    //These functions consider the current state of the cursor with no respect to the current list's state (head, tail, etc).
    private boolean cursorHasNext(final Node cursor) {
      return cursor != null;
    }
    private boolean cursorHasPrevious(final Node cursor) {
      return size > 0 && (cursor == null || cursor.prev != null);
    }
    //This function gets the node behind the cursor provided (cursor in the form of a pointer)
    //returns the node that is returned by the "previous" function.
    //Gets the previous part of the cursor.
    //If cursor is the head element (prev property is null), then the function returns null
    //if list size is 0, then it returns null.
    private Node getPreviousPart(final Node cursor) {
      return cursor == null ? tail : cursor.prev;
    }
  }

  //the prev reference in each node points towards the head. Next reference points towards tail.
  //head and tail are never null unless list size is 0. If list size is 1, head and tail point to same reference.
  // head and tail are inclusive of list
  private Node head = null; //first node
  private Node tail = null; //last node
  private int size = 0;
  private int listModificationCount = 0; //stores the total modifications to the list. Does not include reassigning node values (set operation), only represents changes to the structure of the list itself.
  
  public ElemIterator iterator() {
    return new ElemIterator();
  }
  //see list interface documentation for this method's documentation. Basically, it returns a list iterator with its index set to this index (not exactly, this is just a simplification).
  //"The specified index indicates the first element that would be returned by an initial call to next"
  public ElemIterator listIterator(final int index) {
    //
  }

  public int size() {
    return size;
  }

  //Add methods
  //If you add to the list while iterating, the iterator will obviously skip the added element if it was inserted before the iterator's current position.

  //Add element to head/front of list
  public T addHead(final T elem) {
    if (size == 0) {
      head = new Node(elem);
      tail = head;
    }
    else if (size == 1) {
      head = new Node(elem);
      head.next = tail;
      tail.prev = head;
    }
    else {
      head.prev = new Node(elem, null, head);
      head = head.prev;
    }

    ++size;
    return elem;
  }

  //Add element to tail/back of list
  public T addTail(final T elem) {
    if (size == 0) {
      head = new Node(elem);
      tail = head;
    }
    else if (size == 1) {
      tail = new Node(elem);
      head.next = tail;
      tail.prev = head;
    }
    else {
      tail.next = new Node(elem, tail, null);
      tail = tail.next;
    }

    ++size;
    return elem;
  }

  //Add element to specific index
  public T addAtIndex(final T elem, final int index) {
    //

    ++size;
    return elem;
  }

  //If -1, then it does not exist in list.
  public int getIndexOf() {
    //
  }
  public boolean contains() {
    //
  }

  //add, remove, get, and set for: head, tail, and specific index
  //contains and index of

  //add(E elem)
  //add(int index, E elem)
  //clear
  //contains
  //get(int index)
  //indexOf
  //isEmpty
  //iterator()
  //lastIndexOf
  //listIterator()
  //remove(int index)
  //remove(E elem)
  //set(int index, E element)
  //size()
  
  //subList
  //toArray
}