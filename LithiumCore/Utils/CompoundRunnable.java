package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils;

//This class doesn't null check the runnable array or each individual runnable. So if either set is null, then you be screwed.
//This class is MUTABLE.
public class CompoundRunnable implements Runnable {

  public Runnable[] callbacks;

  public CompoundCallback(final Runnable ...callbacks) {
    this.callbacks = callbacks;
  }

  @Override
  public void run() {
    for (Runnable i : callbacks) {
      i.run();
    }
  }

}