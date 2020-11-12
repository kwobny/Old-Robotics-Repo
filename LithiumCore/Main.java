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
  public final BindingFullPile<CancellableCallback> lowMaint = new BindingFullPile<>();
  public final BindingFullPile<CancellableCallback> highMaint = new BindingFullPile<>();

  //sub systems
  public Time time;

  public Move move = new Move();
  public WaitCore wait = new WaitCore();

  public RPS rps = new RPS();
  public SCS scs = new SCS();

  //CONSTRUCTOR

  //constants object should not be modified after passing it into the main function.
  public Main(final MadHardware mhw, final ConstantsContainer constants) {
    this.mhw = mhw;
    this.constants = constants;

    //SUB OBJECT INITIALIZATION

    move.initialize(mhw, this, rps, constants);

    rps.initialize(mhw, constants);

    time = new Time(mhw, constants);
  }

  //Functions USED ONLY IN TELEOP

  //this function is called to start the whole system
  public void initialize() {

    //start the clock
    time.reset();

    //Setup system intervals
    Time.Interval lowMaintInterval = time.getInterval(constants.config.lowFreqMaintInterval, new Callback() {
      @Override
      public void run() {
        //move._motorCali();
        lowMaint.forAll(WaitCore.CCConsumer);
      }
    });

    Time.Interval highMaintInterval = time.getInterval(constants.config.highFreqMaintInterval, new Callback() {
      @Override
      public void run() {
        scs._runSCS();
        highMaint.forAll(WaitCore.CCConsumer);
      }
    });

    WaitInterval[] staticIntervals = new WaitInterval[]{
      lowMaintInterval.reset(),
      highMaintInterval.reset()
    };
    for (WaitInterval i : staticIntervals)
      wait.addInterval(i);

    //Setup the loop callbacks for the (new) loop notifiers
    if (constants.config.turnOnOPLP) {
      Callback[] beginningLoopCallbacks = new Callback[]{
        time.loop_notifier,
        rps.RPSLoopNotifiers,
        move.notifierResetter
      };
      for (Callback i : beginningLoopCallbacks)
        wait.addLoopCallback(i, WaitCore.LCRunBlock.BEGINNING);
      
      Callback[] endLoopCallbacks = new Callback[]{
        move.OPLPEndCallback
      };
      for (Callback i : endLoopCallbacks)
        wait.addLoopCallback(i, WaitCore.LCRunBlock.END);
    }

  }

  public void loopAtBeginning() {
    wait._loopBefore();
  }
  public void loopAtEnd() {
    wait._loopAfter();
  }

  //Autonomous only functions. USED ONLY IN AUTONOMOUS
  //These functions are only used in autonomous
  
  //start the autonomous
  public void startAutonomous() {
    initialize();
    wait._start();
  }

  //end the autonomous
  public void endAutonomous() {
    wait._end();
  }
  
}