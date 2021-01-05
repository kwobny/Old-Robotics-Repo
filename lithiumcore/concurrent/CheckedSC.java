package lithiumcore.concurrent;

//This class is a started condition that has to be started once and can only be started once before running.
public abstract class CheckedSC extends DefaultSC {

  private boolean hasStarted = false;

  @Override
  public final boolean pollCondition() {
    if (!hasStarted) {
      throw new RuntimeException("You cannot poll a checked started condition before it is started.");
    }
    return _pollCondition();
  }

  //start condition
  @Override
  public void run() {
    if (hasStarted) {
      throw new RuntimeException("You cannot start a checked started condition more than once.");
    }
    hasStarted = true;
    _start();
  }

  protected abstract boolean _pollCondition();
  protected abstract void _start();
}