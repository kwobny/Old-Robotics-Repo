package org.firstinspires.ftc.teamcode.OpModes.motionLibrary.CancelCallback;

//this class is mainly meant to be used internally. It is only public so that it can also be used internally in a client library. Share the abilities.
//Also, when you subclass this, you can also say that the subclass is a Callback, and implement your own personal run function if you so desire.
public abstract class CancellableCallback {
  
  //this property has protected access modifier. This means that it is normally going to be accessed only by the corresponding consumer, but it can also be accessed by any subclass.
  protected boolean isActive = false; //is the main boolean which indicates if the wait is active or not.

}