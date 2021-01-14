package lithiumcore.concurrent;

//This class is the default implementation of the started condition interface.
//You should ideally use this as much as possible.
//It is also a callback, which means that when it is run, it starts the wait.

public abstract class DefaultSC implements StartedCondition, Runnable {
  
  @Override
  public StartedCondition start() {
    run();
    return this;
  }

  //start function
  //This method should start the wait condition.
  @Override
  public abstract void run();

}