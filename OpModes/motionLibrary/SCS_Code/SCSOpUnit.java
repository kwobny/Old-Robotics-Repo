package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.MathFunctions.*;

//Each object of this class contains all the data necessary to represent one SCS operation.

public class SCSOpUnit implements WaitCondition {

  //data members
  public InputSource input;
  public OutputSink output;
  public MathFunction graphFunc;

  public double refInput; //reference input

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
    latestOutput = graphFunc.yValueOf(input.get() - refInput);
    output.set(latestOutput);
    if (usingAsWait && thresholdCallback != null && pollCondition())
      thresholdCallback.run();
  }

  private Callback thresholdCallback = null;
  private double latestOutput;
  private double threshold;
  private boolean isAbove;
  private boolean usingAsWait = false;

  //the is above flag signifies whether or not the output value needs to be above the threshold for the wait to be over.
  public void setOutputThreshold(final double threshold, final boolean isAbove) {
    this.threshold = threshold;
    this.isAbove = isAbove;
    usingAsWait = true;
  }
  public void removeThreshold() {
    usingAsWait = false;
  }

  public void setThresholdCallback(final Callback callback) {
    thresholdCallback = callback;
  }

  @Override
  public boolean pollCondition() throws Exception {
    if (!usingAsWait)
      throw new Exception("There is no output threshold set currently");
    return (isAbove && latestOutput > threshold) || (!isAbove && latestOutput < threshold);
  }

}