package org.firstinspires.ftc.teamcode.lithiumcore.concurrent.coroutine;

import org.firstinspires.ftc.teamcode.lithiumcore.concurrent.DefaultSC;

//This is the generic/general coroutine class.

public abstract class Coroutine extends DefaultSC {

  //data members

  public Runnable callback;
  private boolean isDone;

  @Override
  public abstract void run();

  @Override
  public boolean pollCondition() {
    return isDone;
  }

  public Coroutine() {
    //
  }
  public Coroutine(final Runnable callback) {
    this.callback = callback;
  }

  public final void resetPoll() {
    isDone = false;
  }

  //call this method ideally at the last line of the ending callback.
  //Should be called to end the coroutine.
  protected void endCoroutine() {
    isDone = true;
    if (callback != null) {
      callback.run();
    }
  }
  //Version that can be used to provide as callback instance
  protected final Runnable endCallback = new Runnable() {
    @Override
    public void run() {
      endCoroutine();
    }
  };

}

/*

Example implementation:
public class Example extends Coroutine {

  //some random methods

}

*/

//Old version

/*
public abstract class Coroutine implements Runnable {

  //data members

  public Runnable callback;
  public final WaitCondition condition = new WaitCondition() {
    @Override
    public boolean pollCondition() {
      return isDone;
    }
  };
  public final void setRunWhile(final Runnable runWhileCallback) {
    task.runWhile = runWhileCallback;
  }

  private boolean isDone;
  private final WaitTask task = new WaitTask();
  protected AsyncExecutor waitCore;

  {
    task.autoEndTask = false;
  }

  public Coroutine(final AsyncExecutor waitCore) {
    this.waitCore = waitCore;
  }
  public Coroutine(final AsyncExecutor waitCore, final Runnable callback) {
    this.waitCore = waitCore;
    this.callback = callback;
  }

  public final void resetPoll() {
    isDone = false;
  }

  @Override
  public final void run() {
    _start();
    waitCore.setTimeout(task);
  }

  protected final Runnable endCallback = new Runnable() {
    @Override
    public void run() {
      isDone = true;
      waitCore.removeTimeout(task);
      _end();
      if (callback != null) {
        callback.run();
      }

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