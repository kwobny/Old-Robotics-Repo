package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;

//this class manages everything related to time

public class Time implements InputSource {

  MadHardware mhw;

  Time(MadHardware mhw) {
    this.mhw = mhw;
  }

  public double getTime() {
    return mhw.runtime.time();
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

    public Interval(final double time_interval, final WaitCallback callback) {
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

  public Interval getInterval(final double time_interval, final WaitCallback callback) {
    return new Interval(time_interval, callback);
  }

}