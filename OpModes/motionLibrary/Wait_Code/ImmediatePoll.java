package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

public enum ImmediatePoll implements WaitCondition {
  obj;

  @Override
  public boolean pollCondition() {
    return true;
  }
}