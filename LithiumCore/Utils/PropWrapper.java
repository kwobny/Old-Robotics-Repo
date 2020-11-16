package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.*;

public class PropWrapper<T> {

  public T prop;

  public final Function.Producer<T> PropGetter = new Function.Producer<T>() {
    @Override
    public T get() {
      return prop;
    }
  };
  public final Function.Consumer<T> PropSetter = new Function.Consumer<T>() {
    @Override
    public void apply(final T val) {
      prop = val;
    }
  };

}