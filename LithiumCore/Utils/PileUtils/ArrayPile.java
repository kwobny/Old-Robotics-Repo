package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Iterator;

//This is a pile that is run internally using an arraylist.
//Use for cases where you want to conserve memory on small amounts of objects.

//This is a FIFI (first in first iterated) structure. The first element added gets iterated over first, last element gets iterated over last, etc.

public class ArrayPile<T> extends AbstractPile<T> implements DuplicatesCollection<T> {
  
  private ArrayList<T> internalList = new ArrayList<>();

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
    return internalList.remove(element);
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
  public boolean contains(Object element);

  //Required method
  @Override
  public Iterator<T> iterator();


  @Override
  public void clear() {
    internalList.clear();
  }

  //Required method
  @Override
  public int size() {
    return internalList.size();
  }

  @Override
  public boolean isEmpty() {
    return internalList.isEmpty();
  }

}