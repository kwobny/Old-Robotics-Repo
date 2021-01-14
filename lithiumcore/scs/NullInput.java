package org.firstinspires.ftc.teamcode.lithiumcore.scs;

public enum NullInput implements InputSource {
  obj;

  @Override
  public double get() {
    return 0.0;
  }
}