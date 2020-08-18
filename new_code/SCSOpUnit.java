import MathFunctions.*;

public class SCSOpUnit {

  //data members
  public InputSource input;
  public OutputSink output;
  public MathFunction graphFunc;

  public double refInput; //reference input

  boolean isRunning;

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