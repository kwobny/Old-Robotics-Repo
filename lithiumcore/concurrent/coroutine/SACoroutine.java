package org.firstinspires.ftc.teamcode.lithiumcore.concurrent.coroutine;

//Single active coroutine: is a coroutine which can only have 1 running instance at a time.
public abstract class SACoroutine extends Coroutine {

  private boolean isActive = false;

  public SACoroutine() {
    //
  }
  public SACoroutine(final Runnable callback) {
    super(callback);
  }
  
  @Override
  public void run() {
    if (isActive) {
      throw new RuntimeException("You cannot start a Single active coroutine that is already running");
    }
    isActive = true;
    resetPoll();
    _start();
  }
  
  @Override
  protected void endCoroutine() {
    if (!isActive)
      throw new RuntimeException("You cannot end a single active coroutine that is not running");
    isActive = false;
    _end();
    super.endCoroutine();
  }

  //these two abstract methods are meant to be private and only accessible to/used by the superclass.
  protected abstract void _start();
  protected abstract void _end();

}