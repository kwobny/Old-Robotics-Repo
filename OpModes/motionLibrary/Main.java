package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import com.qualcomm.robotcore.util.ElapsedTime;

class SystemCallbacks {

  Main main;

  public SystemCallbacks(Main m) {
    main = m;
  }

  public final WaitCallback LowInterval = new WaitCallback() {
    @override
    public void run(WaitCondition cond) {
      main.runLowInterval(( (CommonWaits.Time) cond ).changeInTime);
      main.wait.setTimeout(new main.common_waits.Time(), this);
    }
  };

  public final WaitCallback HighInterval = new WaitCallback() {
    @override
    public void run(WaitCondition cond) {
      main.runHighInterval(( (CommonWaits.Time) cond ).changeInTime);
      main.wait.setTimeout(new main.common_waits.Time(), this);
    }
  };

}

public class Main {
  //SETTING VARIABLES
  public final double lowFreqMaintInterval = 0.2; //period in which low frequency periodic functions like motor cali run (in seconds)
  public final double highFreqMaintInterval = 0.05; //period in which very high frequency accurate functions run (in seconds)

  // RESOURCE OBJECTS

  //mad hardware
  public MadHardware mhw;

  //this next grouping of stuff is a group of three parts, which are all triangularly dependent on each other, and madhardware.
  public CommonWaits common_waits = new CommonWaits(this);
  private SystemCallbacks system_callbacks = new SystemCallbacks(this);

  public Move move = new Move();
  public WaitCore wait = new WaitCore();

  public RPS rps = new RPS();
  public SCS scs = new SCS();

  //CONSTRUCTOR

  public Main(MadHardware hmw, WaitCallbackClass waitCallbacks) {
    //SUB OBJECT INITIALIZATION
    mhw = hmw;

    //order of initialization is the wait conditions and callbacks, then wait core, then move core.
    waitConditions.initialize(mhw, this);

    wait.initialize(this, waitConditions, waitCallbacks);
    move.initialize(mhw, rps);

    rps.initialize(mhw, move);

    //SETUP
    wait.setTimeout(new common_waits.Time(lowFreqMaintInterval), system_callbacks.LowInterval);
    wait.setTimeout(new common_waits.Time(highFreqMaintInterval), system_callbacks.HighInterval);
  }

  //OTHER FUNCTIONS AND STUFF

  // loop for the motions
  public void loop() {
    wait.runLoopCallbacks();
    wait.runTimeouts();
  }

  void runLowInterval(final double change_in_time) {
    move.motorCali();
  }
  void runHighInterval(final double change_in_time) {
    move.runCommonAccelerationSystem(change_in_time);
  }

  // function that you call at the end of autonomous sequence to wait for program
  // to end. This should be the absolute last function executed in program.
  public void endProgram() {
    while (true) {
      loop();
    }
  }
}