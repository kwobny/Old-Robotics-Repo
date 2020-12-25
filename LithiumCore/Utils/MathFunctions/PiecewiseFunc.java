package LithiumCore.Utils.MathFunctions;

public class PiecewiseFunc implements MathFunction {

  //domain includes lower bound and excludes upper bound.
  //To specify no bound, use positive or negative infinity. Double.POSITIVE_INFINITY and Double.NEGATIVE_INFINITY. Both Double constants are primitive double values, so no need to worry.
  //lower bound can equal upper bound (but why?).
  public static class Section {
    final double lowBound;
    final double highBound;
    final MathFunction func;

    public Section(final double lowBound, final double highBound, final MathFunction func) {
      if (lowBound > highBound)
        throw new RuntimeException("Sub section lower bound cannot be greater than the upper bound.");
      this.lowBound = lowBound;
      this.highBound = highBound;
      this.func = func;
    }
  }
  public static Section getSection(final double lowBound, final double highBound, final MathFunction func) {
    return new Section(lowBound, highBound, func);
  }

  private Section[] subSections;

  public PiecewiseFunc(Section ...subSections) {
    this.subSections = subSections;
  }

  @Override
  public double yValueOf(final double input) {
    for (Section i : subSections) {
      if (input >= i.lowBound && input < i.highBound)
        return i.func.yValueOf(input);
    }
    throw new RuntimeException("There is no y value for the inputted x value.");
  }

}