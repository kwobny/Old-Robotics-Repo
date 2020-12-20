package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

import java.util.AbstractCollection;
import java.util.Objects;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.functiontypes.Consumer;

//This class is an extended version of abstract collection that also provides an implementation for for each able.
public abstract class AbstractPile<T> extends AbstractCollection<T> implements Foreachable<T> {

  @Override
  public void forEach(Consumer<? super T> action) {
    Objects.requireNonNull(action);
    for (T t : this) {
      action.accept(t);
    }
  }

}