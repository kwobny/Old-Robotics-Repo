package org.firstinspires.ftc.teamcode.OpModes.LithiumCore;

import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.SCS_Package.*;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Wait_Package.*;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Callback;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.SharedState.*;

//this class manages everything related to time

public class Time implements InputSource {

  private MadHardware mhw;
  LoopNotifier loop_notifier;
  private ElapsedTime runtime = new ElapsedTime();

  private final boolean turnOnOPLP;

  Time(MadHardware mhw, final ConstantsContainer constants) {
    this.mhw = mhw;
    this.turnOnOPLP = constants.config.turnOnOPLP;
    if (turnOnOPLP)
      loop_notifier = new LoopNotifier();
  }
  //Is also a start function. You need to invoke this before using the time class.
  public Time reset() {
    runtime.reset();
    return this;
  }

  public double getRawTime() {
    return runtime.time();
  }

  private double saved_time = 0.0;

  public double getTime() {
    if (!turnOnOPLP) {
      return getRawTime();
    }
    if (!loop_notifier.hasRunYet()) {
      saved_time = getRawTime();
      loop_notifier.setHasRun();
    }
    return saved_time;
  }

  //This class is immutable
  public class Wait extends CheckedSC {
    private final double time_delay;
    private double start_time;
    public double changeInTime;

    public Wait(final double delay) {
      time_delay = delay;
      //start_time = getTime();
    }

    @Override
    protected void _start() {
      start_time = getTime();
    }

    @Override
    protected boolean _pollCondition() {
      changeInTime = getTime() - start_time;
      return changeInTime > time_delay;
    }
  }

  //This class IS sort of MUTABLE.
  public class Interval extends WaitInterval {
    public double time_interval;

    public Interval(final double time_interval, final Callback callback) {
      this.time_interval = time_interval;
      this.callback = callback;
    }
    
    @Override
    protected WaitCondition _incrementCondition() {
      return (new Wait(time_interval)).start();
    }

  }

  //for the SCS as an input source
  @Override
  public double get() {
    return getTime();
  }

  //convenience methods
  public Wait getWait(final double delay) {
    return new Wait(delay);
  }

  public Interval getInterval(final double time_interval, final Callback callback) {
    return new Interval(time_interval, callback);
  }

}