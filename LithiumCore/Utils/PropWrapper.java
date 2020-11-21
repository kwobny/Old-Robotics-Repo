package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.*;

public class PropWrapper<T> {

  public T prop;

  private class Getter implements Function.Producer<T> {
    @Override
    public T get() {
      return prop;
    }
  }
  private class Setter implements Function.Consumer<T> {
    @Override
    public void apply(final T val) {
      prop = val;
    }
  }

  public Function.Producer<T> getGetter() {
    return new Getter();
  }
  public Function.Consumer<T> getSetter() {
    return new Setter();
  }

}