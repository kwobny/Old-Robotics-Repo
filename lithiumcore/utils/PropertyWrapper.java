package LithiumCore.Utils;

import LithiumCore.Utils.functiontypes.Consumer;
import LithiumCore.Utils.functiontypes.Supplier;

public class PropertyWrapper<T> {

  public T data;

  public class Getter implements Supplier<T> {
    @Override
    public T get() {
      return data;
    }
  }
  public class Setter implements Consumer<T> {
    @Override
    public void accept(final T value) {
      data = value;
    }
  }

  public Getter getGetter() {
    return new Getter();
  }
  public Setter getSetter() {
    return new Setter();
  }

}