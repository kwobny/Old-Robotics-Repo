package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.MathFunctions.*;

//Each object of this class contains all the data necessary to represent one SCS operation.

public class SCSOpUnit {

  //data members
  public InputSource input;
  public OutputSink output;
  public MathFunction graphFunc;

  public double refInput; //reference input
  public WaitTask waitTask;
  private double latestOutput;
  private double latestInput;

  boolean isRunning = false;

  public SCSOpUnit(final InputSource input, final OutputSink output, final MathFunction graphFunc) {
    if (input == null || output == null || graphFunc == null)
      throw new Exception("One or more of the arguments provided to the SCS Op Unit constructor was null");

    this.input = input;
    this.output = output;
    this.graphFunc = graphFunc;
  }

  public void calibrate() {
    this.refInput = input.get();
  }

  void update() {
    latestInput = input.get() - refInput;
    latestOutput = graphFunc.yValueOf(latestInput);
    output.set(latestOutput);
    if (waitTask != null)
      waitTask.run();
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