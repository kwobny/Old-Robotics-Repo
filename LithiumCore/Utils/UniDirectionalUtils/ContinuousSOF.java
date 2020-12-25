package LithiumCore.Utils.UniDirectionalUtils;

//is a sequential operation function where y 0 on each graph is level with the last point on the previous graph.
//The funcs array cannot have null elements, cannot have a length of 0, and cannot be null.
//This class is immutable except when extended.
public class ContinuousSOF extends BasicSOF {

  //constructors
  protected ContinuousSOF() {
    //
  }
  public ContinuousSOF(final UniDirectionalFunc ...funcs) {
    super(funcs);
  }

  @Override
  protected void setFuncs(final UniDirectionalFunc ...funcs) {
    this.funcs = funcs;
    reset();
  }

  @Override
  boolean increment() {
    lastYVal += funcs[index].getLastYValue();
    return super.increment();
  }

  @Override
  public void reset() {
    lastYVal = 0;
    super.reset();
  }

  @Override
  public double yValueOf(final double x) {
    if (index >= funcs.length || (x > xThreshold && increment())) {
      return lastYVal;
    }
    
    return funcs[index].yValueOf(x - xTranslateVal) + lastYVal;
  }

  @Override
  public double getLastYValue() {
    double sum = 0;
    for (UniDirectionalFunc i : funcs) {
      sum += i.getLastYValue();
    }
    return sum;
  }

}