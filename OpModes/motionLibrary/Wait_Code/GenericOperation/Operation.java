package org.firstinspires.ftc.teamcode.OpModes.motionLibrary.CancelCallback;

//this class is mainly meant to be subclassed, but it can also be used just as is.
public class Operation {

  //this property is default access
  boolean needsDelete = false;
  boolean isActive = false;

  OperationRunner currentRunner;

  //a utility method accessible to any subclass
  protected boolean getIsActive() {
    return isActive;
  }

}