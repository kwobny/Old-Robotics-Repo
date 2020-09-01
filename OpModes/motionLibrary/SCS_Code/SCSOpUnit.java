package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.MathFunctions.*;

//Each object of this class contains all the data necessary to represent one SCS operation.

public class SCSOpUnit {

  //data members
  public InputSource input;
  public OutputSink output;
  public MathFunction graphFunc;

  public double refInput; //reference input

  boolean isRunning = false;

  public SCSOpUnit(final InputSource input, final OutputSink output, final MathFunction graphFunc) {
    this.input = input;
    this.output = output;
    this.graphFunc = graphFunc;
  }

  void calibrate() {
    this.refInput = input.get();
  }

  void update() {
    output.set(graphFunc.yValueOf(input.get() - refInput));
  }

}