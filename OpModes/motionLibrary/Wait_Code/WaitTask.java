package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

//is a class that contains all the information necessary to complete one wait with callbacks.
//implements the command pattern

public class WaitTask {
  public WaitCondition condition;
  public Callback callback;

  WaitTask(final WaitCondition condition, final Callback callback) {
    this.condition = condition;
    this.callback = callback;
  }
}