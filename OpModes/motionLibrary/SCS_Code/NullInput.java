package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

public enum NullInput implements InputSource {
  obj;

  @Override
  public double get() {
    return 0.0;
  }
}