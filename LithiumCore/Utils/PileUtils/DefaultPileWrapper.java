package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

//This is the default version of a pile interface. Used primarily by the user of a client library to interact with a pile directly without messing with elements that they do not own.
//This class is immutable.
public class DefaultPileWrapper<T> implements PileInterface<T> {

  private final Pile<T> wrappedPile;

  public DefaultPileWrapper(final Pile<T> wrappedPile) {
    this.wrappedPile = wrappedPile;
  }
  
  @Override
  public T add(final T element) {
    return wrappedPile.add(element);
  }
  @Override
  public T remove(final T element) {
    return wrappedPile.remove(element);
  }
  @Override
  public boolean removeIfPresent(final T element) {
    return wrappedPile.removeIfPresent(element);
  }
  @Override
  public boolean contains(final T element) {
    return wrappedPile.contains(element);
  }

}