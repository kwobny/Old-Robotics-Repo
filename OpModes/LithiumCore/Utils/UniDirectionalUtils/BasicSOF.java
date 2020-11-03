package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.UniDirectionalUtils;

//This class is the basic / base sequential operation function.
//The funcs array cannot have null elements, cannot have a length of 0, and cannot be null.
//this class is immutable except when extended.
public class BasicSOF implements UniDirectionalFunc {

  //constructors
  protected BasicSOF() {
    //
  }
  public BasicSOF(final UniDirectionalFunc ...funcs) {
    setFuncs(funcs);
  }

  private UniDirectionalFunc[] funcs;
  private int index;
  //this variable is used to store the y value of the function at the right most edge of the domain, to be returned when the function exits.
  double lastYVal; //default access

  private double xTranslateVal;
  private double xThreshold;

  //used by extending classes to set the funcs array.
  //resets the function automatically as well.
  protected void setFuncs(final UniDirectionalFunc ...funcs) {
    this.funcs = funcs;
    reset();
  }

  //reset function
  public void reset() {
    index = 0;
    lastYVal = 0;
    
    xTranslateVal = 0;
    xThreshold = funcs[0].getEndThreshold();
  }

  //increments index and condition variables.
  //return if the index has overflowed (true for overflowed, false for not overflowed)
  boolean increment() { //default access
    ++index;

    if (index < funcs.length) {
      xTranslateVal = xThreshold;
      xThreshold += funcs[index].getEndThreshold();
      return false;
    }
    else {
      lastYVal = funcs[funcs.length-1].yValueOf(xThreshold - xTranslateVal);
      return true;
    }
  }

  //If you get the y value of the function after it has ended, it automatically returns the absolutely last value. No error is thrown.
  @Override
  public double yValueOf(final double x) {
    if (index >= funcs.length) {
      return lastYVal;
    }
    //check if x is higher than threshold. If so, then increment. If index overflows, return last y value.
    if (x > xThreshold && increment()) {
      return lastYVal;
    }
    return funcs[index].yValueOf(x - xTranslateVal);
  }

  @Override
  public double getEndThreshold() {
    double sum = 0;
    for (UniDirectionalFunc i : funcs) {
      sum += i.getEndThreshold();
    }
    return sum;
  }

}