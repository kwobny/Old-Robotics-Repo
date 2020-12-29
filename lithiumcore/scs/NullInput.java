package LithiumCore.SCS_Package;

public enum NullInput implements InputSource {
  obj;

  @Override
  public double get() {
    return 0.0;
  }
}