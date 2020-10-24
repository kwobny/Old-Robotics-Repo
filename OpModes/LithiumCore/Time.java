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
    if (Constants.turnOnOPLP) {
      return getRawTime();
    }
    if (!loop_notifier.hasRunYet()) {
      saved_time = getRawTime();
      loop_notifier.setHasRun();
    }
    return saved_time;
  }

  public class Wait implements WaitCondition {
    private double time_delay;
    private double start_time;
    public double changeInTime;

    public Wait(final double delay) {
      time_difference = delay;
      start_time = getTime();
    }

    @Override
    public boolean pollCondition() {
      changeInTime = getTime() - start_time;
      return changeInTime > time_delay;
    }
  }

  public class Interval extends WaitInterval {
    public double time_interval;

    public Interval(final double time_interval, final Callback callback) {
      this.time_interval = time_interval;
      this.callback = callback;
    }
    
    @Override
    protected void _incrementCondition() {
      cond = new Wait(time_interval);
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