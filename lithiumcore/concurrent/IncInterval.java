package org.firstinspires.ftc.teamcode.lithiumcore.concurrent;

//Incrementing interval
//Is a type of wait interval which works by incrementing a wait condition.

public abstract class IncInterval extends WaitInterval {

  protected WaitCondition condition;

  //Increment condition. Runs when the current condition has ended.
  //This function returns the new wait condition. Or it can mutate the existing one and return it.
  protected abstract WaitCondition _incrementCondition();

  //Can be overrided to implement custom reset behavior
  @Override
  protected void _reset() {
    condition = _incrementCondition();
  }

  @Override
  protected final void _run() {
    if (condition.pollCondition()) {
      callback.run();
      condition = _incrementCondition();
    }
  }

}