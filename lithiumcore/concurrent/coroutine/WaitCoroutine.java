package lithiumcore.concurrent.coroutine;

import lithiumcore.concurrent.WaitCondition;
import lithiumcore.concurrent.AsyncExecutor;
import lithiumcore.concurrent.WaitTask;

//This class is a more specific user friendly version of the Coroutine class. Is meant to be used by the user.

/*
public abstract class WaitCoroutine extends Coroutine implements Runnable {

  //data members

  public final void setRunWhile(final Runnable runWhileCallback) {
    task.runWhile = runWhileCallback;
  }

  private final WaitTask task = new WaitTask();
  protected AsyncExecutor waitCore;

  {
    task.autoEndTask = false;
  }

  public WaitCoroutine(final AsyncExecutor waitCore) {
    this.waitCore = waitCore;
  }
  public WaitCoroutine(final AsyncExecutor waitCore, final Runnable callback) {
    super(callback);
    this.waitCore = waitCore;
  }

  @Override
  public final void run() {
    _start();
    waitCore.setTimeout(task);
  }
  
  protected final Runnable endCallback = new Runnable() {
    @Override
    public void run() {
      waitCore.removeTimeout(task);
      _end();
      super.endCallback.run();
    }
  };

  protected final void setCondition(final WaitCondition condition) {
    task.condition = condition;
  }
  protected final void setCallback(final Runnable callback) {
    task.callback = callback;
  }
  protected final void setNext(final WaitCondition condition, final Runnable callback) {
    task.condition = condition;
    task.callback = callback;
  }

  //these two abstract methods are meant to be private and only accessible to/used by the superclass.
  protected abstract void _start();
  protected abstract void _end();

}
*/

public abstract class WaitCoroutine extends SACoroutine {

  //data members

  public final void setRunWhile(final Runnable runWhileCallback) {
    task.runWhile = runWhileCallback;
  }

  private final WaitTask task = new WaitTask();
  protected AsyncExecutor waitCore;

  {
    task.autoEndTask = false;
  }

  public WaitCoroutine(final AsyncExecutor waitCore) {
    this.waitCore = waitCore;
  }
  public WaitCoroutine(final AsyncExecutor waitCore, final Runnable callback) {
    super(callback);
    this.waitCore = waitCore;
  }

  @Override
  protected final void _start() {
    waitCore.scheduleTask(task);
    _start2();
  }

  @Override
  protected final void _end() {
    waitCore.removeTask(task);
    _end2();
  }

  protected final void setCondition(final WaitCondition condition) {
    task.condition = condition;
  }
  protected final void setCallback(final Runnable callback) {
    task.callback = callback;
  }
  protected final void setNext(final WaitCondition condition, final Runnable callback) {
    task.condition = condition;
    task.callback = callback;
  }

  //these two abstract methods are meant to be private and only accessible to/used by the superclass.
  protected abstract void _start2();
  protected abstract void _end2();

}


/*
Example implementation:

public class Example extends WaitCoroutine {

  public Example(final AsyncExecutor core) {
    super(core);
  }

  @Override
  protected void _start2() {
    //initialize everything (next condition and callback) and do some stuff
    setNext(condition, callback);
  }

  //some intermediate callbacks

  //last callback before end
  private final Runnable secondToLast = new Runnable() {
    @Override
    public void run() {
      //do some stuff
      setNext(condition, endCallback);
    }
  };

  @Override
  protected void _end2() {
    //clean up stuff
  }

}

*/