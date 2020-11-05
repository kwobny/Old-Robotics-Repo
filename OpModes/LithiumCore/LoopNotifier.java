package org.firstinspires.ftc.teamcode.OpModes.LithiumCore;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Callback;

//Is the class that helps implement the OPLP (Once Per Loop Processing) functionality
//Is a class that notifies a user when there is a new loop (used every time loop method is called for waits)
//the notifier is run as a callback every time a new loop begins, which signifies that the new loop has begun.

//notice how this class is not public. This means that the class is only used inside this package
class LoopNotifier implements Callback {

  //Run in current loop. Signifies whether or not the user has run in the current loop yet
  private boolean runInCurrLoop = false;
  
  //Sets the run in curr loop to false
  @Override
  public void run() {
    runInCurrLoop = false;
  }

  //is a method which sets the run in curr loop to true
  public void setHasRun() {
    runInCurrLoop = true;
  }

  //returns whether or not the user has run in the current loop yet
  public boolean hasRunYet() {
    return runInCurrLoop;
  }

}