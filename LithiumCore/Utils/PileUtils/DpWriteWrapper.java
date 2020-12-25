package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

public class DpWriteWrapper<T> extends WriteWrapper<T> {

  public DpWriteWrapper(final DuplicatesCollection<T> collection) {
    super(collection);
  }

  public boolean removeEvery(final Object element) {
    return ((DuplicatesCollection<T>) collection).removeEvery(element);
  }

  public boolean addStrict(final T element) {
    return ((DuplicatesCollection<T>) collection).addStrict(element);
  }

}