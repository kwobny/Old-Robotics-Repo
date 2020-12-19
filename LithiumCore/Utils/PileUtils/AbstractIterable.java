package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

import java.util.Objects;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.functiontypes.Consumer;

//This class provides a default implementation for the foreachable interface when applied to iterables.
//Should be used when creating a class that extends both iterable and foreachable.
public abstract class AbstractIterable<T> implements Iterable<T>, Foreachable<T> {

  @Override
  public void forEach(Consumer<? super T> action) {
    Objects.requireNonNull(action);
    for (T t : this) {
      action.accept(t);
    }
  }

}