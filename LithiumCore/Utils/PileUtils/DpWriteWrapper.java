package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

public class DpWriteWrapper<T> extends WriteWrapper<T> {

  public DpWriteWrapper(final DuplicatesCollection<T> collection) {
    super(collection);
  }

  public boolean removeAll(Object element) {
    return ((DuplicatesCollection) collection).removeAll(element);
  }

}