import MathFunctions.*

public class SCSOpUnit {

  //data members
  public InputSource input;
  public OutputSink output;
  public MathFunction graphFunc;

  public double refInput; //reference input

  public SCSOpUnit(final InputSource input, final OutputSink output, final MathFunction graphFunc) {
    this.input = input;
    this.output = output;
    this.graphFunc = graphFunc;
  }

  public void calibrate(final double refInput) {
    this.refInput = refInput;
  }

  public void update() {
    output.set(graphFunc.yValueOf(input.get() - refInput));
  }

}