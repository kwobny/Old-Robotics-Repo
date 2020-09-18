package org.firstinspires.ftc.teamcode.OpModes.motionLibrary.CancelCallback;

//There are 6 methods total. 3 need to be overridden, and 3 are the ones that are to be used.

public abstract class CCConsumer<T extends CancellableCallback> {
  
  //these methods should be PRIVATE, but are protected because they need to be accessible and overridable from any subclass
  protected abstract void _actualRun(T obj);

  protected abstract void _removeCallback(T obj);

  //protected abstract void _addCallback(T obj);

  //these methods are public because the consumer object itself is likely to be private.
  public final void run(CancellableCallback obj) {
    if (obj.isActive)
      _actualRun(obj);
    else {
      markAsRemoved(obj);
      _removeCallback(obj);
    }
  }

  public final void markAsAdded(CancellableCallback obj) throws Exception {
    if (obj.isActive)
      throw new Exception("You cannot add a cancellable callback that is already added.");
    obj.isActive = true;
  }

  public final void markAsRemoved(CancellableCallback obj) throws Exception {
    if (!obj.isActive)
      throw new Exception("You cannot delete a cancellable callback that has not been added yet.");
    obj.isActive = false;
  }

}

/*

Example:
public class ExampleData extends CancellableCallback {
  //your own stuff, data, etc.
}

public class ExampleConsumer extends CCConsumer<ExampleData> {

  @Override
  protected void _actualRun(ExampleData obj) {
    //do whatever
  }

  @Override
  protected void _addCallback(ExampleData obj) {
    //do whatever
  }

  @Override
  protected void _deleteCallback(ExampleData obj) {
    //do whatever
  }

}

or as a class member

private CCConsumer<ExampleData> consumer = new CCConsumer<>() {
  
  @Override
  protected void _actualRun(ExampleData obj) {
    //do whatever
  }

  @Override
  protected void _addCallback(ExampleData obj) {
    //do whatever
  }

  @Override
  protected void _deleteCallback(ExampleData obj) {
    //do whatever
  }

};

*/