package LithiumCore.SCS_Package;

import LithiumCore.Utils.MathFunctions.MathFunction;
import LithiumCore.Utils.PileUtils.BoundedElem;

import LithiumCore.Wait_Package.WaitTask;

//Each object of this class contains all the data necessary to represent one SCS operation.

public class SCSOpUnit extends BoundedElem {

  //data members

  //these three things always need to be defined.
  public InputSource input;
  public OutputSink output;
  public MathFunction graphFunc;

  private WaitTask waitTask;

  double refInput; //reference input
  private double latestOutput;
  private double latestInput;

  boolean isPaused = false; //default access

  //boolean isRunning = false;

  public SCSOpUnit(final InputSource input, final OutputSink output, final MathFunction graphFunc) {
    /*if (input == null || output == null || graphFunc == null)
      throw new Exception("One or more of the arguments provided to the SCS Op Unit constructor was null")
    */

    this.input = input;
    this.output = output;
    this.graphFunc = graphFunc;
  }

  public SCSOpUnit() {
    //
  }

  public void setWaitTask(final WaitTask waitTask) {
    this.waitTask = waitTask;
    waitTask._markAsAdd();
  }

  //this auto calibrates the function (sets reference value to current input). Always needs to be called before doing an operation.
  public void calibrate() {
    refInput = input.get();
  }
  //this is an overload that is able to be used by all types of functions. You are able to manually set the reference value.
  public void calibrate(final double refVal) {
    refInput = refVal;
  }

  //these things are default access
  private double stateSave;
  void saveState() {
    //latestInput = input.get() - refInput;
    stateSave = input.get();
  }
  void restoreState() {
    //refInput = input.get() - latestInput;
    //refInput = newInput - oldInput + oldRefInput

    refInput += input.get() - stateSave;
  }

  void run() {
    latestInput = input.get() - refInput;
    latestOutput = graphFunc.yValueOf(latestInput);
    output.set(latestOutput);
    if (waitTask != null) {
      if (waitTask._isRunning()) {
        waitTask._run();
      }
      else {
        waitTask = null;
      }
    }
  }

  public class OutputCond extends ThresholdWait {

    public OutputCond(final double threshold, final boolean isAbove) {
      super(threshold, isAbove);
    }

    @Override
    protected double getCompVal() {
      return latestOutput;
    }

  }

  public class LastInputCond extends ThresholdWait {

    public LastInputCond(final double threshold, final boolean isAbove) {
      super(threshold, isAbove);
    }

    @Override
    protected double getCompVal() {
      return latestInput;
    }

  }

  //the difference between this and the last input condition is that the last input condition tests using the input when the operation was last run. This condition uses the real time input.
  //This condition only runs when the actual operation is running.
  public class InputCond extends ThresholdWait {

    public InputCond(final double threshold, final boolean isAbove) {
      super(threshold, isAbove);
    }
    public InputCond() {
      //
    }

    @Override
    public boolean pollCondition() {
      return getIsInPile() && super.pollCondition();
    }

    @Override
    protected double getCompVal() {
      return input.get() - refInput;
    }

  }

  public OutputCond getOutputCond(final double threshold, final boolean isAbove) {
    return new OutputCond(threshold, isAbove);
  }

  public LastInputCond getInputCond(final double threshold, final boolean isAbove) {
    return new LastInputCond(threshold, isAbove);
  }

  //alternative output cond wait
  /*
  public class OutputCond implements WaitCondition {

    public double threshold;
    public boolean isAbove;

    //the is above flag signifies whether or not the output value needs to be above the threshold for the wait to be over.
    public OutputCond(final double threshold, final boolean isAbove) {
      this.threshold = threshold;
      this.isAbove = isAbove;
    }
    
    @Override
    public boolean pollCondition() throws Exception {
      return (isAbove && latestOutput > threshold) || (!isAbove && latestOutput < threshold);
    }

  }
  */

  public static SCSOpUnit getOpUnit(final InputSource input, final OutputSink output, final MathFunction graphFunc) {
    return new SCSOpUnit(input, output, graphFunc);
  }

}