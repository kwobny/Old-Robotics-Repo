package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.NativeUtils;

import java.util.*;

//Buffed linked list
//This is a doubly linked list.

//Why is this linked list buffed? Why not just use the implementation provided in the standard libraries? Thats because you can mutate it (add, delete, clear, etc.) while iterating through it!!! Isn't that sick?? No more annoying concurrent modification exceptions. Yay!!!
//The whole point of making this linked list implementation is so that you can iterate through the list while mutating it.
//You should be able to mutate the list while iterating through it.

public class BuffedLinkedList<T> implements Iterable<T> {

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
  //It is recommended that you always get a cursor pointer from the iterator class' getCursor method.
  //Is immutable.
  //Well, if it is supposed to be immutable, how come the expected index property can change (from the sync indices method)? This is because it is not the index property that is immutable, but the idea of the cursor's position that is immutable. Each object of this class can only point to 1 cursor position during its lifetime, but that cursor's index relative to the start of the list can change because of list modification.
  public class CursorPointer {
    CursorPointer() { //cannot instantiate class outside of package
      //
    }

    //these properties can only be accessed by the linked list and its iterator (default access).
    //These properties have the same semantics/meanings as explained in the iterator comment documentation.
    Node<T> pointer;
    int expectedIndex;
    int expectedModCount;

    public BuffedLinkedList<T> getOwningList() {
      return BuffedLinkedList.this;
    }

    //look at the list iterator documentation for information on the next 2 functions.
    //You can only request the previous and next index if the owning list was not modified since the iterator was created. If you request one of the indices, then the function throws a concurrent modification exception.

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
    
    //Is used to discern whether or not you can use the previous and next index functions. You can't get either index if the index is not known.
    public boolean indexIsKnown() {
      return expectedModCount == listModificationCount;
    }

    //equals function
    //Tests to see if the current/original cursor is "equal" to the supplied cursor. Two cursors are equal if they are in the same position in the list, ie their pointers are equal.
    @Override
    public boolean equals(final Object o) {
      //Do reference comparisons first. If references aren't equal, then cast the provided object to a cursor pointer and compare the pointers of the current and provided cursor.
      //if the object provided is not a linked list cursor, throw a class cast exception.
      return (this == o) || (o != null && this.pointer == ((CursorPointer) o).pointer);
    }

    //If two cursors are equal and one cursor does not know its own index, then the other cursor can be used to calibrate the index of the first. This is called synchronizing the cursor.
    //this function synchronizes indices if one of them is not known. You can only call this function if the two cursors are equal to each other. Call the equals function to determine this.
    //returns the cursor whose index was synchronized. If none were synchronized, then it returns null.
    public CursorPointer syncIndices(final CursorPointer cursor) {
      if (this.pointer != cursor.pointer) { //the equals part of the function.
        throw new IllegalArgumentException("You cannot synchronize the indices of the current cursor and the one provided if they are not on the same node.");
      }
      if (this.indexIsKnown()) {
        if (!cursor.indexIsKnown()) {
          //need to synchronize comparing/argument provided cursor.
          cursor.expectedIndex = this.expectedIndex;
          cursor.expectedModCount = this.expectedModCount;

          return cursor;
        }
      }
      else if (cursor.indexIsKnown()) {
        //need to synchronize this/current cursor
        this.expectedIndex = cursor.expectedIndex;
        this.expectedModCount = cursor.expectedModCount;

        return this;
      }

      return null;
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
    //if the list's size is 0, then the pointer should be null.

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

    //Clone iterator function
    @Override
    public Iter clone() {
      return (Iter) super.clone();
    }

    //start main iterator functions

    @Override
    public boolean hasNext() {
      readjustPosition();
      return cursorHasNext(pointer);
    }
    @Override
    public boolean hasPrevious() {
      readjustPosition();
      return cursorHasPrevious(pointer);
    }

    //sets the iterator's position to some index in the list.
    //the index parameter describes the index of the element returned by a subsequent call to next.
    //if the index is out of bounds, the function throws an exception.
    public void setPosition(final int targetIndex) {
      if (targetIndex < 0 || targetIndex > size) {
        throw new IndexOutOfBoundsException(String.format("The index you provided was out of bounds. Index: %d, list size: %d. Linked list iterator set position (maybe from the constructor?).", targetIndex, size));
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
            pointer = traverseFromTail(targetIndex);
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
            pointer = traverseFromHead(targetIndex);
          }
          else {
            //target is closer to iterator
            previous(expectedIndex-targetIndex);
          }
        }

      }
      else {
        //use old fasioned way
        pointer = smartTraverse(targetIndex);
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

    //Returns a copy of the iterator's cursor in the form of a cursor pointer.
    public CursorPointer getCursor() {
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
      //readjustPosition(); No need to do this, already done in the has previous function.

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
      //readjustPosition(); No need to readjust position, already done in the has next function.

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
      //readjustPosition(); No need to do this, already done in the has previous function.
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
      //readjustPosition(); No need to do this, already readjusted in the has next function.
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

    //Adds an element after previous element and before next element, in the same spot as the cursor. However...
    //It adds the element before the implicit cursor.
    //In the case of a unidirectional iterator, the element is added after the last element returned, and the iterator does not iterate over the added element.
    @Override
    public void add(final T elem) {
      //readjust position
      readjustPosition();
      
      linkElement(pointer, elem);

      ++expectedIndex;
      ++expectedModCount; ++listModificationCount;

    }

    //You can remove elements after adding them through iterator, contrary to what list iterator documentation says.
    //Removes the element that was returned by the last call to next or previous. In the case of a unidirectional iterator, it just removes the current element.
    //Removes the element towards the direction of the last call. If next was called last, then it removes the element towards the head direction, and vise versa. This also applies regardless of if you add an element.
    //If an element is added and the last call was next, then the new element is removed. But if the last call was previous, then the element towards the tail direction is removed.

    //You cannot use the blank remove method if you haven't called previous or next yet.

    //You can only remove the previous and next element once per cursor position. This is because you cannot remove a specific element again after it is already removed.

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

      //readjustPosition(); No need to do this, already done in the has previous function.

      //make sure there is actually an element to remove.
      if (!hasPrevious()) {
        throw new IllegalStateException("cannot remove the previous element when there is none. linked list iterator remove previous.");
      }

      unlinkNode(getPreviousPart(pointer));

      --expectedIndex;
      removedPreviousElement = true;
      ++expectedModCount; ++listModificationCount;
    }
    public void removeNext() {
      if (removedNextElement) {
        throw new IllegalStateException("You cannot remove the next element more than once. Linked list iterator remove next.");
      }

      //readjustPosition(); No need to do this, already done in the has next function.

      //make sure there is actually an element to remove.
      if (!hasNext()) {
        throw new IllegalStateException("You cannot remove the next element when there is none. link list iterator remove next.");
      }

      unlinkNode(pointer);

      removedNextElement = true;
      ++expectedModCount; ++listModificationCount;
    }

    //These functions are used to determine if the remove methods have been called. Are basically getters for the removed next and removed previous variables.
    //True means yes, the element has been removed. False means the element has not been removed.

    public boolean hasRemoved() {
      if (!incrementedYet) {
        throw new IllegalStateException("You cannot use the no parameter has removed function without calling previous or next first. linked list iterator has removed.");
      }
      if (nextCalledLast) {
        return hasRemovedPrevious();
      }
      else {
        return hasRemovedNext();
      }
    }
    public boolean hasRemovedPrevious() {
      return removedPreviousElement;
    }
    public boolean hasRemovedNext() {
      return removedNextElement;
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
      //readjustPosition(); No need to do this, already done in the has previous function.
      
      if (!hasPrevious()) {
        throw new IllegalStateException("You cannot set the previous element if there is no previous element. Linked list iterator set previous.");
      }

      getPreviousPart(pointer).data = value;
    }
    public void setNext(final T value) {
      //readjustPosition(); No need to do this, already done in the has next function.

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

  }

  //the prev reference in each node points towards the head. Next reference points towards tail.
  //head and tail are never null unless list size is 0. If list size is 1, head and tail point to same reference.
  // head and tail are inclusive of list
  private Node<T> head = null; //first node
  private Node<T> tail = null; //last node
  private int size = 0;
  private int listModificationCount = 0; //stores the total modifications to the list. Does not include reassigning node values (set operation), only represents changes to the structure of the list itself.
  //list modification count does not necessarily store the number of individual modifications. Rather, it can store the number of groups of modifications. For example, in the clear method, instead of incrementing the count for every element removed, it just increments the count by one to represent the group operation of clearing the list.

  //Terms representing beginning of list:
  //head
  //beginning
  //before
  //first
  //previous
  //left
  //behind
  //index of 0
  
  //Terms representing end of list
  //tail
  //end
  //after
  //last
  //next
  //right
  //ahead
  //index of (size) or (size - 1)

  //First to last in list goes from head to tail. Iterator normally goes from head to tail when used as a normal iterator.

  //some notes about head, tail, and node references:
  //if list size is 0, head and tail will always be null.
  //if list size is 1, head and tail will always point to the same element.
  //head and tail are inclusive of the list. Head is first node and tail is last node.
  //the head element always has its prev (previous) pointer set to null.
  //the tail element always has its next pointer set to null.

  //PRIVATE UTILITY METHODS

  //returns true if the elements are, in a sense, "equal".
  //returns false if the elements are not equal.
  //o is the original element, e is the element being compared to the original.
  private static <U> boolean areEqual(final U o, final U e) {
    //this is the formal definition of equals
    return o == null ? e == null : o.equals(e);
  }

  //This function gets the node behind the cursor provided (cursor in the form of a pointer).
  //The pointer provided is the next element of the cursor.
  //returns the node that is returned by the "previous" function.
  //Gets the previous part of the cursor.
  //If cursor is the head element (prev property is null), then the function returns null
  //if list size is 0, then it returns null.
  private Node<T> getPreviousPart(final Node<T> cursor) {
    return cursor == null ? tail : cursor.prev;
  }

  //All of the following functions are insecure functions which do not check for certain error cases.

  //this function gets the node at the index provided, traversing from the head.
  //Does not check list size
  private Node<T> traverseFromHead(int targetIndex) {
    Node<T> ptr = head;
    for (; targetIndex > 0; --targetIndex) {
      ptr = ptr.next;
    }
    return ptr;
  }
  //this function also gets the node at the index provided, but traverses from the tail.
  //Also does not check list size
  private Node<T> traverseFromTail(int targetIndex) {
    Node<T> ptr = tail;
    for (; targetIndex < size-1; ++targetIndex) {
      ptr = ptr.prev;
    }
    return ptr;
  }
  //This function gets the node at the index and automatically decides which direction to traverse from.
  private Node<T> smartTraverse(final int targetIndex) {
    //This does integer division.
    if (targetIndex >= size/2) {
      //index is closer to end of list
      return traverseFromTail(targetIndex);
    }
    else {
      //index is closer to start of list
      return traverseFromHead(targetIndex);
    }
  }

  //In the link element and unlink node methods, the provided node MUST be from the same list as the one the method is called on.

  //inserts the specified element right before the pointer provided. The provided node is set to the next element of the added node.
  //The provided node can range from being head to being null.
  //If the provided node is null, that means insert the element right after tail.
  //automatically handles the head, tail, size, and all properties in the node class.
  private void linkElement(final Node<T> node, final T element) {

    final Node<T> prevNode = getPreviousPart(node); //node before the supplied node and the element to be inserted.
    final Node<T> newNode = new Node<T>(elem, prevNode, node);
    //when prevNode == null, head == node.
    //when prevNode != null, head != node.
    if (prevNode == null) {
      //set head to the new node if there is no previous element.
      //set head to the new node if the head used to be the next node.
      head = newNode;
    }
    else {
      //else just connect the previous node to the inserted element.
      prevNode.next = newNode;
    }
    //when node == null, tail == prevNode.
    //when node != null, tail != prevNode.
    if (node == null) {
      //set tail to the new node if there is no next element.
      //set tail to new node if the tail used to be the previous node.
      tail = newNode;
    }
    else {
      //else just connect the next node to the inserted element.
      node.prev = newNode;
    }

    ++size; //only additionally adjusts this variable.
  }

  //removes/unlinks the provided node from the linked list.
  //the node provided cannot be null (obviously)
  //automatically handles head, tail, size, and all node properties.
  private void unlinkNode(final Node<T> node) {
    node.isRemoved = true;
    --size;
    
    //when node.next == null, tail == node.
    //when node.next != null, tail != node.
    if (node.next == null) {
      //next element is null. That means that the tail is set to the removed element, which is not good. Set the tail to the previous element.
      tail = node.prev;
    }
    else {
      //else, just reconnect the next element.
      node.next.prev = node.prev;
    }
    //when node.prev == null, head == node.
    //when node.prev != null, head != node.
    if (node.prev == null) {
      //previous element is null. That means that the removed element was on the edge, and that head was set to that removed element. Therefore, you need to set head to the next node.
      head = node.next;
    }
    else {
      //else, just reconnect the previous node.
      node.prev.next = node.next;
    }
  }

  //MAIN FUNCTIONS

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

  //returns the number of elements in the list.
  public int size() {
    return size;
  }
  //returns true if the list is empty. False if not empty.
  public boolean isEmpty() {
    return size == 0;
  }

  //Finds the first occurance of a given element in the list and returns a cursor which has its next element set to that first occurance.
  //If the element does not exist in the list, the function returns null.
  public CursorPointer cursorOf(final T element) {
    int ind = 0;
    for (Node<T> ptr = head; ptr != null; ptr = ptr.next, ++ind) {
      if (areEqual(element, ptr.data)) {
        final CursorPointer cursor = new CursorPointer();
        cursor.pointer = ptr;
        cursor.expectedIndex = ind;
        cursor.expectedModCount = listModificationCount;
        return cursor;
      }
    }
    return null;
  }

  //same as cursor of method except it finds the last occurance of the element in the list.
  //Long definition: Finds the last occurance of a given element in the list and returns a cursor which has its next element set to that last occurance.
  //If the element does not exist in the list, the function returns null.
  public CursorPointer lastCursorOf(final T element) {
    int ind = size-1;
    for (Node<T> ptr = tail; ptr != null; ptr = ptr.prev, --ind) {
      if (areEqual(element, ptr.data)) {
        final CursorPointer cursor = new CursorPointer();
        cursor.pointer = ptr;
        cursor.expectedIndex = ind;
        cursor.expectedModCount = listModificationCount;
        return cursor;
      }
    }
    return null;
  }

  //index of method returns the index of the first occurance of an element within the list. If the element does not exist, then it returns -1.
  public int indexOf(final T element) {
    int ind = 0;
    for (Node<T> ptr = head; ptr != null; ptr = ptr.next, ++ind) {
      if (areEqual(element, ptr.data)) {
        return ind;
      }
    }
    return -1;
  }
  //last index of method returns the index of the last occurance of an element. If the element does not exist in the list, then it returns -1.
  public int lastIndexOf(final T element) {
    int ind = size-1;
    for (Node<T> ptr = tail; ptr != null; ptr = ptr.prev, --ind) {
      if (areEqual(element, ptr.data)) {
        return ind;
      }
    }
    return -1;
  }

  //returns true if the element is in the list.
  //returns false if the element is not in the list.
  public boolean contains(final T element) {
    return indexOf(element) != -1;
  }

  //returns the linked list's elements in order in the form of an array.
  public T[] toArray() {
    final T[] retArr = new T[size];
    int i = 0;
    for (Node<T> ptr = head; ptr != null; ptr = ptr.next, ++i) {
      retArr[i] = ptr.data;
    }
    return retArr;
  }

  //GET METHODS
  //all throw an exception if there is no element.
  
  public T getHead() {
    if (isEmpty()) {
      throw new IllegalStateException("You cannot request the head node if the list is empty. linked list get head");
    }
    return head.data;
  }
  public T getTail() {
    if (isEmpty()) {
      throw new IllegalStateException("You cannot request the tail node if the list is empty. linked list get tail");
    }
    return tail.data;
  }
  //gets the element at a certain index. If the index requested is out of the bounds of the list, the function throws an error.
  public T get(final int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException(String.format("The index you provided was out of bounds. Index: %d, list size: %d. Linked list get at index.", index, size));
    }
    return smartTraverse(index).data;
  }

  //SET METHODS
  //All return the element that was at the position previously before replacement.
  //If there is no element/node to set, the function throws an exception.

  public T setHead(final T element) {
    if (isEmpty()) {
      throw new IllegalStateException("You cannot set the head of a linked list if the list is empty. Linked list set head");
    }
    final T oldElement = head.data;
    head.data = element;
    return oldElement;
  }
  public T setTail(final T element) {
    if (isEmpty()) {
      throw new IllegalStateException("You cannot set the tail of a linked list if the list is empty. Linked list set tail");
    }
    final T oldElement = tail.data;
    tail.data = element;
    return oldElement;
  }
  //sets the element at the index of the list.
  //if the index is out of bounds, the function throws an error.
  public T set(final int index, final T element) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException(String.format("The index you provided was out of bounds. Index: %d, list size: %d. Linked list set at index.", index, size));
    }
    final Node<T> node = smartTraverse(index);
    final T oldElement = node.data;
    node.data = element;
    return oldElement;
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
  //Inserts the element right before the specified index. Inserts in cursor between elements at index-1 and index.
  //The element originally at the specified index would be the new element's next element.
  //After insertion, the new element's index would be the index provided.
  //This means that the element that was at the index is now shifted to the right, along with all of the elements after it.
  //the index can be from (inclusive) 0 to (inclusive) size.
  public T add(final int index, final T elem) {
    if (index < 0 || index > size) {
      throw new IndexOutOfBoundsException(String.format("The index you provided was out of bounds. Index: %d, list size: %d. Linked list add at index.", index, size));
    }
    final Node<T> myPreciousNode = index == size ? null : smartTraverse(index);
    linkElement(myPreciousNode, elem);
    ++listModificationCount;
    return elem;
  }

  //REMOVE METHODS
  //returns the element removed.
  //throws an error if the element to be removed does not exist.
  
  public T removeHead() {
    if (isEmpty()) {
      throw new IllegalStateException("You cannot remove the head element if the list is empty. Linked list remove head");
    }
    final T oldElem = head.data;

    head.isRemoved = true;
    if (size == 1) {
      head = null;
      tail = null;
    }
    else {
      head = head.next;
      head.prev = null;
    }

    --size;
    ++listModificationCount;

    return oldElem;
  }
  public T removeTail() {
    if (isEmpty()) {
      throw new IllegalStateException("You cannot remove the tail element if the list is empty. Linked list remove tail");
    }
    final T oldElem = tail.data;

    tail.isRemoved = true;
    if (size == 1) {
      head = null;
      tail = null;
    }
    else {
      tail = tail.prev;
      tail.next = null;
    }

    --size;
    ++listModificationCount;

    return oldElem;
  }

  //removes the element at the specified index.
  //index can be from (inclusive) 0 to (inclusive) size-1.
  public T remove(final int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException(String.format("The index you provided was out of bounds. Index: %d, list size: %d. Linked list remove at index.", index, size));
    }
    final Node<T> nodeToRemove = smartTraverse(index);
    unlinkNode(nodeToRemove);
    ++listModificationCount;
    return nodeToRemove.data;
  }

  //removes the first occurance of the element in the list.
  //Returns whether or not the element was removed. If the element did not exist, then it returns false. Else, if it did exist, then it returns true.
  //Returns true if the removal was successful. If the element did not exist, then it returns false.
  public boolean remove(final T elem) {
    for (Node<T> ptr = head; ptr != null; ptr = ptr.next) {
      if (areEqual(elem, ptr.data)) {
        unlinkNode(ptr);
        ++listModificationCount;
        return true;
      }
    }
    return false;
  }

  //CLEAR LIST

  //removes all elements from this list.
  public void clear() {
    if (isEmpty()) {
      return;
    }

    Node<T> mainPtr = head;
    Node<T> auxPtr = null;
    while (mainPtr != null) {
      mainPtr.prev = null;
      mainPtr.isRemoved = true;

      auxPtr = mainPtr;
      mainPtr = mainPtr.next;

      auxPtr.next = null;
    }

    head = null;
    tail = null;
    size = 0;
    ++listModificationCount;
  }

}