package lithiumcore;

import other.Backend.MadHardware;

import lithiumcore.utils.Callback;
import lithiumcore.utils.pile.BindingFullPile;

import lithiumcore.scs.SCS;
import lithiumcore.executor.WaitCore;
import lithiumcore.executor.WaitInterval;

import lithiumcore.sharedstate.ConstantsContainer;

//Main class
//Is a hub for all library subsystems. It is not a parent, and more like a hub with a few preattached and preinitialized systems. You should be able to add your own subsystems to this hub.
//You should always start the main system before accessing any of the subsystems. However, there is nothing preventing you from not doing so. It is assumed that the user is aware of this rule.

//Some vocab:
//initialize: setup the thing
//start: start the timer/clock on the system.

//These functions from the Wait Core class must not be used. Instead, the variants in the Main class must be used.
//start and end -> startAutonomous and endAutonomous
//loopBefore and loopAfter -> loopAtBeginning and loopAtEnd

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

  //this function is called to start the whole system (start the clock metaphorically)
  //Even though its labeled start tele op, thats just to avoid confusion. Its basically a method which starts all of the subsystems.
  public void startTeleOp() {

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
    wait.loopBefore();
  }
  public void loopAtEnd() {
    wait.loopAfter();
  }

  //Autonomous only functions. USED ONLY IN AUTONOMOUS
  //These functions are only used in autonomous
  
  //start the autonomous
  public void startAutonomous() {
    startTeleOp();
    wait.start();
  }

  //end the autonomous
  public void endAutonomous() {
    wait.end();
  }
  
}