package org.firstinspires.ftc.teamcode.OpModes.motionLibrary.CancelCallback;

public abstract class Operation {

  //this property is default access
  boolean needsDelete = false;
  boolean isActive = false;

  OperationRunner currentRunner;

  //this method can either be protected (package private) or public
  protected abstract void run();

}