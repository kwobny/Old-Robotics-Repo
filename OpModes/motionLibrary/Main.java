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
  public CommonWaits common_waits = new CommonWaits(this);
  public Time time;

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

    time = new Time(mhw);

    //Setup system intervals
    Time.Interval lowMaint = time.getInterval(lowFreqMaintInterval, new WaitCallback() {
      @Override
      public void run(WaitCondition cond) {
        runLowInterval(cond.changeInTime);
      }
    });

    Time.Interval highMaint = time.getInterval(highFreqMaintInterval, new WaitCallback() {
      @Override
      public void run(WaitCondition cond) {
        runHighInterval(cond.changeInTime);
      }
    });

    wait.setStaticIntervals(lowMaint, highMaint);
  }

  //OTHER FUNCTIONS AND STUFF

  // loop for the motions
  public void loop() {
    wait.runLoopCallbacks();
    wait.runIntervals();
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