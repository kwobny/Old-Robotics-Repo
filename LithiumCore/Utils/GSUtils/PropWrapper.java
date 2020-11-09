package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.GSUtils;

public class PropWrapper<T> {

  public T prop;

  public final Getter<T> PropGetter = new Getter<>() {
    @Override
    public T get() {
      return prop;
    }
  };
  public final Setter<T> PropSetter = new Setter<>() {
    @Override
    public void set(final T val) {
      prop = val;
    }
  };

}