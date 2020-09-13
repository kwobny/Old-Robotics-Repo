package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

public abstract class CancellableCallback {
  
  //I think I am going to make this variable private later on.
  boolean isActive = false;

  //when you subclass this, you can also say that the subclass is a Callback, and implement your own personal run function if you so desire.

  //this method should be PRIVATE, but is protected because it needs to be accessible and overridable from any subclass
  protected abstract void _actualRun();

}