package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.NativeUtils;

import java.util.*;

//The whole point of making this linked list implementation is so that you can iterate through the list while mutating it.
//You should be able to mutate the list while iterating through it.
//This is a doubly linked list.
public class LinkedList<T> implements Iterable<T> {

  private static class Node<U> {
    U data;
    Node<U> prev;
    Node<U> next;
    boolean isRemoved = false;
    public Node() {
      //
    }
    public Node(final U data) {
      this.data = data;
    }
    public Node(final U data, final Node<U> prev, final Node<U> next) {
      this.data = data;
      this.prev = prev;
      this.next = next;
    }
  }

  //is a wrapper for an iterator's cursor. Used to save the current position of an iterator.
  //Cannot be used as an iterator. Just saves one position and thats it.
  //Is immutable.
  public class CursorPointer {
    CursorPointer() { //cannot instantiate class outside of package
      //
    }

    //these properties can only be accessed by the linked list and its iterator.
    //These properties have the same semantics/meanings as explained in the iterator comment documentation.
    Node<T> pointer;
    int expectedIndex;
    int expectedModCount;

    public LinkedList<T> getOwningList() {
      return LinkedList.this;
    }

    //look at the list iterator documentation for information on the next 2 functions.

    //returns the index of the element after the cursor (next element)
    public int nextIndex() {
      if (indexIsKnown()) {
        return expectedIndex;
      }
      //if index is not known
      throw new ConcurrentModificationException("You cannot request the next index of a cursor pointer (or maybe iterator) if the list was modified while iterating. This is because the index is not known.");
    }
    //returns the index of the element before the cursor (previous element)
    public int previousIndex() {
      if (indexIsKnown()) {
        return expectedIndex-1;
      }
      //if index is not known
      throw new ConcurrentModificationException("You cannot request the previous index of a cursor pointer (or maybe iterator) if the list was modified while iterating. This is because the index is not known.");
    }
    
    //returns if the previous and next indices of the cursor are known, and if user can use the next index and previous index methods.
    public boolean indexIsKnown() {
      return expectedModCount == listModificationCount;
    }
  }

  //This iterator is obviously mutable (cause its an iterator). Takes an immutable cursor pointer and makes it mutable.
  public class Iter extends CursorPointer implements ListIterator<T>, Cloneable {

    private boolean removedPreviousElement = false;
    private boolean removedNextElement = false;
    private boolean nextCalledLast; //specifies which iterating method (next or previous) was called last. True means next, false means previous.
    private boolean incrementedYet = false; //specifies if the next or previous methods have been called since the last set position command.

    //private Node pointer = null;
    //Is a pointer to the iterator cursor
    //pointer is the node pointed to by the next method. I.e. the pointer variable is the node to to the right/tail direction of the cursor.
    //head corresponds with left most edge of list, null corresponds with right most edge.

    //private int expectedIndex; //only used for next and previous index functions. Is the index of the pointer. Is index of element returned by next function
    //private int expectedModCount; //does not represent reassigning node values.

    public Iter() { //default access. Cannot be created outside of package.
      this(0);
    }
    //"The specified index indicates the first element that would be returned by an initial call to next"
    public Iter(final int index) { //default access
      setPosition(index);
    }
    //Used to get iterator from cursor.
    public Iter(final CursorPointer cursor) {
      setPosition(cursor);
    }
    
    //equals function
    //Tests to see if the current iterator is "equal" to the supplied iterator. Two iterators are equal if their cursors are in the same position in the list, ie their pointers are equal.
    @Override
    public boolean equals(final Object o) {
      final Iter iter = (Iter) o; //if the object provided is not a linked list element iterator, throw a class cast exception.
      return pointer == iter.pointer;
    }

    //If the iterators are equal and one iterator does not know its own index, then the other iterator can be used to calibrate the index of the first. This is called synchronizing the iterator.
    //this function synchronizes indices if one of them is not known. You can only call this function if the two iterators are equal to each other. Call the equals function to determine this.
    //returns the iterator whose index was synchronized. If none were synchronized, then it returns null.
    public Iter syncIndices(final Iter iter) {
      if (pointer != iter.pointer) { //the equals part of the function.
        throw new IllegalStateException("You cannot synchronize the indices of the current iterator and one provided if they are not on the same node.");
      }
      if (this.indexIsKnown()) {
        if (!iter.indexIsKnown()) {
          //need to synchronize comparing/argument provided iterator.
          iter.expectedIndex = this.expectedIndex;
          iter.expectedModCount = this.expectedModCount;

          return iter;
        }
      }
      else if (iter.indexIsKnown()) {
        //need to synchronize this/current iterator
        this.expectedIndex = iter.expectedIndex;
        this.expectedModCount = iter.expectedModCount;

        return this;
      }

      return null;
    }

    //Clone iterator function
    @Override
    public Iter clone() {
      return (Iter) super.clone();
    }

    //start main iterator functions

    @Override
    public boolean hasNext() {
      return cursorHasNext(pointer);
    }
    @Override
    public boolean hasPrevious() {
      return cursorHasPrevious(pointer);
    }

    //sets the iterator's position to some index in the list.
    //the index parameter describes the index of the element returned by a subsequent call to next.
    //if the index is out of bounds, the function throws an exception.
    public void setPosition(final int targetIndex) {
      if (targetIndex < 0) {
        throw new IllegalArgumentException("You cannot supply a negative target index to the set position function. Linked list iterator set position.");
      }
      if (targetIndex > size) {
        throw new IndexOutOfBoundsException("Linked list iterator set position method (maybe from iterator constructor?). The target index provided was greater than the size of the list.");
      }
      final boolean isNotOnEnd = hasNext() && hasPrevious();
      if (isNotOnEnd && indexIsKnown()) {
        
        if (targetIndex == expectedIndex) {
          return; //do nothing because the iterator is already on the position requested.
        }
        if (targetIndex > expectedIndex) {
          //target index is toward the tail direction of the iterator
          if (targetIndex >= size/2 + expectedIndex/2) {
            //target is closer to tail
            pointer = null;
            previous(size-targetIndex);
          }
          else {
            //target is closer to iterator
            next(targetIndex-expectedIndex);
          }
        }
        else {
          //target index is towards the head direction of the iterator
          if (targetIndex < expectedIndex/2) {
            //target is closer to head
            pointer = head;
            next(targetIndex);
          }
          else {
            //target is closer to iterator
            previous(expectedIndex-targetIndex);
          }
        }

      }
      else {
        //use old fasioned way

        //index is closer to end of list
        if (targetIndex >= size/2) {
          pointer = null;
          previous(size-targetIndex);
        }
        //index is closer to start of list
        else {
          pointer = head;
          next(targetIndex);
        }
      }

      expectedIndex = targetIndex;
      expectedModCount = listModificationCount;

      removedPreviousElement = false;
      removedNextElement = false;
      incrementedYet = false;
    }

    //this version of the set position command sets the iterator's position to the one specified by the cursor pointer or by another iterator.
    //You can input an iterator or cursor pointer.
    public void setPosition(final CursorPointer cursor) {
      if (cursor.getOwningList() != this.getOwningList()) {
        throw new IllegalArgumentException("You cannot set the iterator's position to a cursor which is not from the same list as the iterator itself. Linked List iterator set position.");
      }

      this.pointer = cursor.pointer;
      this.expectedIndex = cursor.expectedIndex;
      this.expectedModCount = cursor.expectedModCount;

      removedPreviousElement = false;
      removedNextElement = false;
      incrementedYet = false;
    }

    //returns the current position of the iterator in the form of a cursor pointer.
    public CursorPointer getCurrentPosition() {
      final CursorPointer cursor = new CursorPointer();
      cursor.pointer = this.pointer;
      cursor.expectedIndex = this.expectedIndex;
      cursor.expectedModCount = this.expectedModCount;
      return cursor;
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
        throw new IllegalArgumentException("Linked list iterator previous. You cannot supply a negative step to the previous function.");
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
        throw new NoSuchElementException("linked list iterator next");
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
        throw new IllegalArgumentException("Linked list iterator next. You cannot supply a negative step to the next function.");
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
      Node<T> tempPointer = pointer;
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
      Node<T> tempPointer = pointer;
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
      
      final Node<T> prevNode = getPreviousPart(pointer); //node returned by the previous method
      final Node<T> newNode = new Node<T>(elem, prevNode, pointer);
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
      final Node<T> removeNode = getPreviousPart(pointer);
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
    private boolean cursorHasNext(final Node<T> cursor) {
      return cursor != null;
    }
    private boolean cursorHasPrevious(final Node<T> cursor) {
      return size > 0 && (cursor == null || cursor.prev != null);
    }
    //This function gets the node behind the cursor provided (cursor in the form of a pointer)
    //returns the node that is returned by the "previous" function.
    //Gets the previous part of the cursor.
    //If cursor is the head element (prev property is null), then the function returns null
    //if list size is 0, then it returns null.
    private Node<T> getPreviousPart(final Node<T> cursor) {
      return cursor == null ? tail : cursor.prev;
    }
  }

  //the prev reference in each node points towards the head. Next reference points towards tail.
  //head and tail are never null unless list size is 0. If list size is 1, head and tail point to same reference.
  // head and tail are inclusive of list
  private Node<T> head = null; //first node
  private Node<T> tail = null; //last node
  private int size = 0;
  private int listModificationCount = 0; //stores the total modifications to the list. Does not include reassigning node values (set operation), only represents changes to the structure of the list itself.

  //Terms representing beginning of list:
  //head
  //beginning
  //first
  //previous
  //left
  //index of 0
  
  //Terms representing end of list
  //tail
  //end
  //last
  //next
  //right
  //index of (size) or (size - 1)

  //First to last in list goes from head to tail. Iterator normally goes from head to tail when used as a normal iterator.

  //some notes about head, tail, and node references:
  //if list size is 0, head and tail will always be null.
  //if list size is 1, head and tail will always point to the same element.
  //head and tail are inclusive of the list. Head is first node and tail is last node.
  //the head element always has its prev (previous) pointer set to null.
  //the tail element always has its next pointer set to null.

  @Override
  public Iter iterator() {
    return new Iter();
  }
  //see list interface documentation for this method's documentation. Basically, it returns a list iterator with its index set to this index (not exactly, this is just a simplification).
  //"The specified index indicates the first element that would be returned by an initial call to next"
  public Iter iterator(final int index) {
    return new Iter(index);
  }
  public Iter iterator(final CursorPointer cursor) {
    return new Iter(cursor);
  }

  public int size() {
    return size;
  }
  public boolean isEmpty() {
    return size == 0;
  }

  //Finds a given element in the list and returns a cursor which has its next element set to the provided one.
  public CursorPointer cursorOf(final T element) {
    for (Iter it = new Iter(); it.hasNext();) {
      final T currentElem = it.next();
      if (element.equals(currentElem)) {
        return it.previousIndex();
      }
    }
    return -1;
  }

  //index of method returns the index of the first occurance of an element within the list. If the element does not exist, then it returns -1.
  public int indexOf(final T element) {
    for (Iter it = new Iter(); it.hasNext();) {
      final T currentElem = it.next();
      if (element.equals(currentElem)) {
        return it.previousIndex();
      }
    }
    return -1;
  }
  //last index of method returns the index of the last occurance of an element. If the element does not exist in the list, then it returns -1.
  public int lastIndexOf(final T element) {
    for (Iter it = new Iter(size); it.hasPrevious();) {
      final T currentElem = it.previous();
      if (element.equals(currentElem)) {
        return it.nextIndex();
      }
    }
    return -1;
  }

  public boolean contains(final T element) {
    return indexOf(element) != -1;
  }

  //returns the linked list's elements in order in the form of an array.
  public T[] toArray() {
    final T[] retArr = new T[size];
    final Iter it = new Iter();
    for (int i = 0; i < retArr.size; ++i) {
      final T nextElem = it.next();
      retArr[i] = nextElem;
    }
    return retArr;
  }

  //GET METHODS
  //all throw an exception if there is no element.
  
  public T getHead() {
    if (size == 0) {
      throw new IllegalStateException("You cannot request the head node if the size of the list is 0. linked list get head");
    }
    return head.data;
  }
  public T getTail() {
    if (size == 0) {
      throw new IllegalStateException("You cannot request the tail node if the size of the list is 0. linked list get tail");
    }
    return tail.data;
  }
  //gets the element at a certain index. If the index requested is higher than the list size, the function returns null.
  public T get(final int index) {
    try {
      final Iter it = new Iter(index);
      return it.next();
    }
    catch (RuntimeException e) {
      throw new RuntimeException("linked list get. Requested index is out of bounds.", e);
    }
  }

  //SET METHODS
  //All return the element that was at the position previously before replacement.
  //If there is no element/node to set, the function throws an exception.

  public T setHead(final T element) {
    if (size == 0) {
      throw new IllegalStateException("You cannot set the head of a linked list if the size of the list is 0. Linked list set head");
    }
    final T oldElement = head.data;
    head.data = element;
    return oldElement;
  }
  public T setTail(final T element) {
    if (size == 0) {
      throw new IllegalStateException("You cannot set the tail of a linked list if the size of the list is 0. Linked list set tail");
    }
    final T oldElement = tail.data;
    tail.data = element;
    return oldElement;
  }
  //sets the element at the index of the list.
  public T set(final int index, final T element) {
    try {
      final Iter it = new Iter(index);
      final T oldElement = it.next();
      it.set(element);
      return oldElement;
    }
    catch (RuntimeException e) {
      throw new RuntimeException("The index provided is out of bounds. Linked list set function.", e);
    }
  }

  //ITERATOR STRUCTURE MUTATING FUNCTIONS
  //Structure mutating refers to an operation that changes the structure of the list itself. As such, set operations are excluded from this category.

  //ADD METHODS
  //All add methods return the added element
  //If you add to the list while iterating, the iterator will skip the added element if it was inserted in the same position as the iterator, relative to its neighboring elements.

  //Add element to head/front of list
  //Prepend an element to the list. Similar to array unshift operation.
  public T addFront(final T elem) {
    if (size == 0) {
      head = new Node<T>(elem);
      tail = head;
    }
    else if (size == 1) {
      head = new Node<T>(elem);
      head.next = tail;
      tail.prev = head;
    }
    else {
      head.prev = new Node<T>(elem, null, head);
      head = head.prev;
    }

    ++size;
    ++listModificationCount;

    return elem;
  }

  //Add element to tail/back of list
  //Append an element to the list. Similar to array push operation.
  public T add(final T elem) {
    if (size == 0) {
      head = new Node<T>(elem);
      tail = head;
    }
    else if (size == 1) {
      tail = new Node<T>(elem);
      head.next = tail;
      tail.prev = head;
    }
    else {
      tail.next = new Node<T>(elem, tail, null);
      tail = tail.next;
    }

    ++size;
    ++listModificationCount;

    return elem;
  }

  //Add element at specific index
  //Inserts the element right before the specified index. Inserts in cursor between index-1 and index.
  //This means that the element that was at the index is now shifted to the right, along with all of the elements after it.
  //the index can be from (inclusive) 0 to (inclusive) size.
  public T add(final int index, final T elem) {
    final Iter it;
    try {
      it = new Iter(index);
    }
    catch (RuntimeException e) {
      throw new RuntimeException("The index you provided is out of bounds. Linked list add function.", e);
    }
    it.add(elem);

    return elem;
  }

  //REMOVE METHODS

  public T removeHead

  //remove for: head, tail, and specific index

  //remove(int index)
  //remove(E elem)
  //clear
}