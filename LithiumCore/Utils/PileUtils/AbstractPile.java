package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils;

import java.util.AbstractCollection;
import java.util.Objects;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.functiontypes.Consumer;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.functiontypes.Predicate;

//This class is an extended version of abstract collection that also provides an implementation for for each able.
public abstract class AbstractPile<T> extends AbstractCollection<T> implements Pile<T> {

  //These methods below do not need to be modified.

  @Override
  public void forEach(Consumer<? super T> action) {
    Objects.requireNonNull(action);
    for (T t : this) {
      action.accept(t);
    }
  }

  @Override
  public boolean removeIf(Predicate<? super T> filter) {
    Objects.requireNonNull(filter);
    boolean removed = false;
    final Iterator<T> each = iterator();
    while (each.hasNext()) {
      if (filter.test(each.next())) {
        each.remove();
        removed = true;
      }
    }
    return removed;
  }

  //These methods below should be modified if there is a more efficient implementation.

  @Override
  public boolean addIf(final T element, final boolean present) {
    if (present == contains(element)) {
      return add(element);
    }
    return false;
  }

}