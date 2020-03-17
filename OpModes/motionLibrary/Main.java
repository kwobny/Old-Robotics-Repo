package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Main {
  // RESOURCE OBJECTS

  //mad hardware
  public MadHardware mhw;

  //this next grouping of stuff is a group of three parts, which are all triangularly dependent on each other, and madhardware.
  public WaitConditionClass waitConditions = new WaitConditionClass();
  public WaitCallbackClass waitCallbacks = new WaitCallbackClass();

  public Move move = new Move();
  public WaitCore wait = new WaitCore();

  public RPS rps = new RPS();

  //CONSTRUCTOR

  public Main(MadHardware hmw) {
    mhw = hmw;

    //order of initialization is the wait conditions and callbacks, then wait core, then move core.
    waitConditions.initialize(mhw, move);
    waitCallbacks.initialize(mhw, move);

    wait.initialize(this, waitConditions, waitCallbacks);
    move.initialize(mhw, wait, waitConditions);
  }

  //OTHER FUNCTIONS AND STUFF

  // loop for the motions
  public void loop() {
    wait.executeIntervals();
    wait.runTimeouts();
  }

  // function that you call at the end of autonomous sequence to wait for program
  // to end. This should be the absolute last function executed in program.
  public void endProgram() {
    while (true) {
      loop();
    }
  }
}