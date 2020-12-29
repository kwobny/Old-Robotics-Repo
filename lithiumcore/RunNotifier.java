package LithiumCore;

import LithiumCore.Utils.Callback;

//Is the class that helps implement the OPLP (Once Per Loop Processing) functionality
//Is a class that can be used to notify a user when there is a new loop (used every time loop method is called for waits)
//the notifier is run as a callback every time a new loop begins, which signifies that the new loop has begun.

//This class is used to determine if something has executed from the last time the notifier was reset.

//notice how this class is not public. This means that the class is only used inside this package
class RunNotifier implements Callback {

  //Run in current loop. Signifies whether or not the user has run in the current loop yet
  private boolean runInCurrLoop = false;
  
  //Reset loop notifier
  @Override
  public void run() {
    reset();
  }

  //Resets the loop notifier.
  public void reset() {
    runInCurrLoop = false;
  }

  //specify that the operation has run.
  public void setHasRun() {
    runInCurrLoop = true;
  }

  //returns whether or not the user has run in the current loop yet
  public boolean hasRunYet() {
    return runInCurrLoop;
  }

}