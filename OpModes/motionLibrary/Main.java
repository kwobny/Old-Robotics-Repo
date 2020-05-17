package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Main {
  //SETTING VARIABLES
  public final double lowFreqMaintInterval = 0.2; //period in which low frequency periodic functions like motor cali run (in seconds)
  public final double highFreqMaintInterval = 0.05; //period in which very high frequency accurate functions run (in seconds)

  // RESOURCE OBJECTS

  //mad hardware
  public MadHardware mhw;

  //this next grouping of stuff is a group of three parts, which are all triangularly dependent on each other, and madhardware.
  public WaitConditionClass waitConditions = new WaitConditionClass();

  public Move move = new Move();
  public WaitCore wait = new WaitCore();

  public RPS rps = new RPS();

  //CONSTRUCTOR

  public Main(MadHardware hmw, WaitCallbackClass waitCallbacks) {
    //SUB OBJECT INITIALIZATION
    mhw = hmw;

    //order of initialization is the wait conditions and callbacks, then wait core, then move core.
    waitConditions.initialize(mhw, move);

    wait.initialize(this, waitConditions, waitCallbacks);
    move.initialize(mhw, rps);

    rps.initialize(mhw, move);

    //SETUP
    wait.addInterval(WaitEnum.TIME, -1, lowFreqMaintInterval);
    wait.addInterval(WaitEnum.TIME, -2, highFreqMaintInterval);
  }

  //OTHER FUNCTIONS AND STUFF

  // loop for the motions
  public void loop() {
    wait.executeIntervals();
    wait.runTimeouts();
  }

  void runLowInterval() {
    move.motorCali();
  }
  void runHighInterval() {
    move.runCommonAccelerationSystem(waitConditions.changeInTime);
  }

  // function that you call at the end of autonomous sequence to wait for program
  // to end. This should be the absolute last function executed in program.
  public void endProgram() {
    while (true) {
      loop();
    }
  }
}