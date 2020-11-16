package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils;

public interface Function<ParameterType, ReturnType> {
  public ReturnType apply(ParameterType val);

  public static interface Consumer<ParameterType> {
    public void apply(ParameterType val);
  }
  public static interface Producer<ReturnType> {
    public ReturnType get();
  }
}