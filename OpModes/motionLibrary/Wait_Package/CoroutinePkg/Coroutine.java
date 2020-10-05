package org.firstinspires.ftc.teamcode.OpModes.motionLibrary.Wait_Package.CoroutinePkg;

import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.Wait_Package.*;

//This is the generic/general coroutine class.

public abstract class Coroutine {

  //data members

  public Callback callback;
  public final WaitCondition condition = new WaitCondition() {
    @Override
    public boolean pollCondition() {
      return isDone;
    }
  };

  private boolean isDone;

  public Coroutine() {
    //
  }
  public Coroutine(final Callback callback) {
    this.callback = callback;
  }

  public final void resetPoll() {
    isDone = false;
  }

  //call this method ideally at the last line of the ending callback.
  protected final Callback endCallback = new Callback() {
    @Override
    public void run() {
      isDone = true;
      if (callback != null) {
        callback.run();
      }

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
public abstract class Coroutine implements Callback {

  //data members

  public Callback callback;
  public final WaitCondition condition = new WaitCondition() {
    @Override
    public boolean pollCondition() {
      return isDone;
    }
  };
  public final void setRunWhile(final Callback runWhileCallback) {
    task.runWhile = runWhileCallback;
  }

  private boolean isDone;
  private final WaitTask task = new WaitTask();
  protected WaitCore waitCore;

  {
    task.autoEndTask = false;
  }

  public Coroutine(final WaitCore waitCore) {
    this.waitCore = waitCore;
  }
  public Coroutine(final WaitCore waitCore, final Callback callback) {
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

  protected final Callback endCallback = new Callback() {
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
  protected final void setCallback(final Callback callback) {
    task.callback = callback;
  }
  protected final void setNext(final WaitCondition condition, final Callback callback) {
    task.condition = condition;
    task.callback = callback;
  }

  //these two abstract methods are meant to be private and only accessible to/used by the superclass.
  protected abstract void _start();
  protected abstract void _end();

}
*/