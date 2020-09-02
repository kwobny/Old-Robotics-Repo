package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

public abstract class WaitInterval {

  protected WaitCondition cond;
  public Callback callback;

  boolean isActive = false; //boolean representing if the interval is active or not. Is only manipulated by outside wait core class.

  public WaitInterval() {
    _incrementCondition();
  }

  protected abstract void _incrementCondition();

  void run() {
    if (cond.pollCondition()) {
      callback.run();
      _incrementCondition();
    }
  }

  public WaitCondition getCond() {
    return cond;
  }

}