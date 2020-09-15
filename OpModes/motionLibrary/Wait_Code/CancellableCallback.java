package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

//this class is mainly meant to be used internally. It is only public so that it can also be used internally in a client library. Share the abilities.
//Also, when you subclass this, you can also say that the subclass is a Callback, and implement your own personal run function if you so desire.
public abstract class CancellableCallback {
  
  private boolean isActive = false; //is the main boolean which indicates if the wait is active or not.
  private boolean needsDeletion = false; //is just a helper/auxiliary boolean.

  //this method should be PRIVATE, but is protected because it needs to be accessible and overridable from any subclass
  protected abstract void _actualRun();

  //this method is PRIVATE
  protected abstract void _deleteCallback();

  protected abstract void _addCallback();

  //these methods can be protected (essentially default/package private) or public
  protected final void run() {
    if (isActive)
      _actualRun();
    else {
      needsDeletion = false;
      _deleteCallback();
    }
  }

  protected void add() throws Exception {
    if (isActive)
      throw new Exception("You cannot add a cancellable callback that is already added.");
    isActive = true;
    if (needsDeletion) {
      needsDeletion = false;
    }
    else {
      _addCallback();
    }
  }

  protected void markForDelete() throws Exception {
    if (!isActive)
      throw new Exception("You cannot delete a cancellable callback that has not been added yet.");
    isActive = false;
    needsDeletion = true;
  }

}