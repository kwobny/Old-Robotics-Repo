package org.firstinspires.ftc.teamcode.OpModes.LithiumCore;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.MathFunctions.*;

//This class is immutable
//This class can be extended. Only subclasses have access to a bare constructor. This is so that the subclass can set function properties in somewhere other than the first line. The subclass has to call setParams sometime.
public class SigmoidFunc implements MathFunction {

  protected double L; //curve's maximum value
  private double kNeg; //negative of the logistic growth rate
  protected double x0; //midpoint of curve

  //accessor methods for k.
  protected double getK() {
    return -kNeg;
  }
  protected void setK(final double val) {
    kNeg = -val;
  }

  //constructors
  public SigmoidFunc(final double L, final double k, final double x0) {
    setParams(L, k, x0);
  }
  public SigmoidFunc(final ParamContainer params) {
    setParams(params);
  }
  protected SigmoidFunc() {
    //
  }

  //set parameters function
  protected void setParams(final double L, final double k, final double x0) {
    this.L = L;
    this.kNeg = -k;
    this.x0 = x0;
  }
  protected void setParams(final ParamContainer p) {
    setParams(p.L, p.k, p.x0);
  }

  @Override
  public double yValueOf(final double x) {
    return L/(1 + Math.exp(kNeg * (x - x0)));
  }

  //Return the parameters of the sigmoid func in the form of a parameter container.
  public ParamContainer getParams() {
    return new ParamContainer(L, -kNeg, x0);
  }

  //This is basically a container that contains all of the parameters for the sigmoid function.
  public static class ParamContainer {
    public double L;
    public double k;
    public double x0;

    public ParamContainer() {
      //
    }
    public ParamContainer(final double L, final double k, final double x0) {
      this.L = L;
      this.k = k;
      this.x0 = x0;
    }
  }

}