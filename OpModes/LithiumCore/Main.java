package org.firstinspires.ftc.teamcode.OpModes.LithiumCore;

import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.SCS_Package.*;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Wait_Package.*;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Callback;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils.*;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.SharedState.*;

public class Main {

  // RESOURCE OBJECTS

  //constants object
  public final ConstantsContainer constants;

  //mad hardware
  public MadHardware mhw;

  //low and high maintenence interval piles
  public final BindingFullPile<CancellableCallback> lowMaint;
  public final BindingFullPile<CancellableCallback> highMaint;

  //sub systems
  public Time time;

  public Move move = new Move();
  public WaitCore wait = new WaitCore();

  public RPS rps = new RPS();
  public SCS scs = new SCS();

  //CONSTRUCTOR

  public Main(final MadHardware mhw, final ConstantsContainer constants) {
    this.mhw = mhw;
    this.constants = constants;

    lowMaint = new BindingFullPile<>();
    highMaint = new BindingFullPile<>();

    //SUB OBJECT INITIALIZATION

    move.initialize(mhw, rps, wait, new LoopNotifier());

    rps.initialize(mhw, move);

    time = new Time(mhw);
  }

  //this function is called to start the whole system
  public void start() {
    //Setup system intervals
    Time.Interval lowMaintCallback = time.getInterval(constants.config.lowFreqMaintInterval, new Callback() {
      @Override
      public void run() {
        //move._motorCali();
        lowMaint.forAll(WaitCore.CCConsumer);
      }
    });

    Time.Interval highMaintCallback = time.getInterval(constants.config.highFreqMaintInterval, new Callback() {
      @Override
      public void run() {
        move.motorSyncNotifier.run();
        scs._runSCS();
        highMaint.forAll(WaitCore.CCConsumer);
        if (move.motorSyncNotifier.hasRunYet()) {
          move.syncMotors();
        }
      }
    });

    wait._setStaticIntervals(lowMaintCallback.calibrate(), highMaintCallback.calibrate());

    //Setup the loop callbacks for the (new) loop notifiers
    if (Constants.turnOnOPLP)
      wait._setStaticCallbacks(time.loop_notifer);
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
  
}