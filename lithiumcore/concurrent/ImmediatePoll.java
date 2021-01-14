package org.firstinspires.ftc.teamcode.lithiumcore.concurrent;

public enum ImmediatePoll implements WaitCondition {
  obj;

  @Override
  public boolean pollCondition() {
    return true;
  }
}