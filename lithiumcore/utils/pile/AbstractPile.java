package lithiumcore.utils.pile;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.Objects;

import lithiumcore.utils.functiontypes.Consumer;
import lithiumcore.utils.functiontypes.Predicate;

//This class is an extended version of abstract collection that also provides an implementation for for each able.
public abstract class AbstractPile<T> extends AbstractCollection<T> implements Pile<T> {

  //These methods below do not need to be modified, unless for specific reasons.

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

}