package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

public abstract class WaitInterval {

  protected WaitCondition cond;
  public Callback callback;

  boolean isActive = false; //boolean representing if the interval is active or not. Is only manipulated by outside wait core class.

  protected abstract void _incrementCondition();

  void run() {
    if (cond.pollCondition()) {
      callback.run();
      _incrementCondition();
    }
  }

}