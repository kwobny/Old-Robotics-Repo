package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Wait_Package;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Callback;

public class CompoundCallback implements Callback {

  public Callback[] callbacks;

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