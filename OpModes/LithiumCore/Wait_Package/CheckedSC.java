package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Wait_Package;

//This class is a started condition that has to be started once and only once before running.
public abstract class CheckedSC implements StartedCond {
  private boolean hasStarted = false;

  @Override
  public boolean pollCondition() {
    if (!hasStarted) {
      throw new RuntimeException("You cannot poll a checked started condition before it is started.");
    }
    return _pollCondition();
  }

  @Override
  public StartedCond start() {
    if (hasStarted) {
      throw new RuntimeException("You cannot start a checked started condition more than once.");
    }
    hasStarted = true;
    _start();
    return this;
  }

  protected abstract boolean _pollCondition();
  protected abstract void _start();
}