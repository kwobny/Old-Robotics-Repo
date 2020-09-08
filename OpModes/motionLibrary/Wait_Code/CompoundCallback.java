package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

public class CompoundCallback implements Callback {

  public Callback callbacks;

  public CompoundCallback(final Callback ...callbacks) {
    this.callbacks = callbacks;
  }

  @Override
  public void run() {
    for (Callback i : callbacks) {
      i.run();
    }
  }

}