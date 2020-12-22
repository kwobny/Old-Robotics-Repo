package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

public class DpWriteWrapper<T> extends WriteWrapper<T> {

  public DpWriteWrapper(final DuplicatesCollection<T> collection) {
    super(collection);
  }

  public boolean removeAll(final Object element) {
    return ((DuplicatesCollection) collection).removeAll(element);
  }

  public boolean addStrict(final T element) {
    return ((DuplicatesCollection) collection).addStrict(element);
  }

}