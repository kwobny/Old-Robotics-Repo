package org.firstinspires.ftc.teamcode.OpModes.LithiumCore;

import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.SCS_Package.*;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Wait_Package.*;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Callback;

//this class manages everything related to time

public class Time implements InputSource {

  private MadHardware mhw;
  LoopNotifer loop_notifier;

  Time(MadHardware mhw) {
    this.mhw = mhw;
    if (Constants.turnOnOPLP)
      loop_notifier = new LoopNotifer();
  }

  public double getRawTime() {
    return mhw.runtime.time();
  }

  private double saved_time = 0.0;

  public double getTime() {
    if (!Constants.turnOnOPLP) {
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