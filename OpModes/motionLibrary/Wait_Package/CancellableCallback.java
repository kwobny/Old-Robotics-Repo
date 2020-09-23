package org.firstinspires.ftc.teamcode.OpModes.motionLibrary.Wait_Package;

import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.GenericOperation;

public class CancellableCallback extends Operation {
  
  public Callback callback;

  public CancellableCallback(final Callback callback) {
    this.callback = callback;
  }

}