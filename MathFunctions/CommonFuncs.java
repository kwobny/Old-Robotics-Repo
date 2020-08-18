package MathFunctions;

public class CommonFuncs {

  public static class Linear extends MathFunction {

    public double m;
    public double b;

    public Linear(final double m, final double b) {
      this.m = m;
      this.b = b;
    }
    
    @Override
    public double yValueOf(double x) {
      return m * x + b; //y = mx + b
    }

  }

}