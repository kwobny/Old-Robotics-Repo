package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Wait_Package;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Callback;

//This class doesn't null check the callback array or each individual callback. So if either set is null, then you be screwed.
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