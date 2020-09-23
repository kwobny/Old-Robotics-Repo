package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.SCS_Package.*;
import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.Wait_Package.*;

public class Main {
  //SETTING VARIABLES
  public final double lowFreqMaintInterval = 0.2; //period in which low frequency periodic functions like motor cali run (in seconds)
  public final double highFreqMaintInterval = 0.05; //period in which very high frequency accurate functions run (in seconds)

  // RESOURCE OBJECTS

  //mad hardware
  public MadHardware mhw;

  //sub systems
  public Time time;

  public Move move = new Move();
  public WaitCore wait = new WaitCore();

  public RPS rps = new RPS();
  public SCS scs = new SCS();

  //CONSTRUCTOR

  public Main(MadHardware mhw) {
    //SUB OBJECT INITIALIZATION
    this.mhw = mhw;

    move.initialize(mhw, rps);

    rps.initialize(mhw, move);

    time = new Time(mhw);
  }

  //this function is called to start the whole system
  public void start() {
    //Setup system intervals
    Time.Interval lowMaint = time.getInterval(lowFreqMaintInterval, new Callback() {
      @Override
      public void run() {
        runLowInterval();
      }
    });

    Time.Interval highMaint = time.getInterval(highFreqMaintInterval, new Callback() {
      @Override
      public void run() {
        runHighInterval();
      }
    });

    wait.setStaticIntervals(lowMaint, highMaint);

    //Setup the loop callbacks for the (new) loop notifiers
    if (Constants.turnOnOPLP)
      wait.setStaticCallbacks(time.loop_notifer);
  }

  // this function is called at the end of the program.
  // ONLY USED FOR AUTONOMOUS
  public void end() {
    while (true) {
      wait.loop();
    }
  }

  // loop for the motions
  //In teleop, it is best to run this method at the beginning of each loop
  public void loop() {
    wait.loop();
  }

  void runLowInterval() {
    move.motorCali();
  }
  void runHighInterval() {
    scs.runSCS();
  }
}