package org.firstinspires.ftc.teamcode.lithiumcore.utils.unidirectionfunc;

import org.firstinspires.ftc.teamcode.lithiumcore.utils.mathfunction.CommonTrans;

/*
Common pattern:

public class UniDirectionalTransform extends NormalTransform implements UniDirectionalFunc {
  @Override
  public double getEndThreshold() {
    return ((UniDirectionalFunc) next).getEndThreshold();
  }
  @Override
  public double getLastYValue() {
    return ((UniDirectionalFunc) next).getLastYValue();
  }
}

*/

//Common unidirectional transformations
public class UniTrans {

  public static class Translate extends CommonTrans.Translate implements UniDirectionalFunc {
    public Translate(final double shiftX, final double shiftY) {
      super(shiftX, shiftY);
    }
    public Translate() {
      //
    }
    @Override
    public double getEndThreshold() {
      return ((UniDirectionalFunc) next).getEndThreshold() + shiftX;
    }
    @Override
    public double getLastYValue() {
      return ((UniDirectionalFunc) next).getLastYValue() + shiftY;
    }
  }
  
  public static class TranslateX extends CommonTrans.TranslateX implements UniDirectionalFunc {
    public TranslateX(final double shiftX) {
      super(shiftX);
    }
    public TranslateX() {
      //
    }
    @Override
    public double getEndThreshold() {
      return ((UniDirectionalFunc) next).getEndThreshold() + shiftX;
    }
    @Override
    public double getLastYValue() {
      return ((UniDirectionalFunc) next).getLastYValue();
    }
  }

  public static class TranslateY extends CommonTrans.TranslateY implements UniDirectionalFunc {
    public TranslateY(final double shiftY) {
      super(shiftY);
    }
    public TranslateY() {
      //
    }
    @Override
    public double getEndThreshold() {
      return ((UniDirectionalFunc) next).getEndThreshold();
    }
    @Override
    public double getLastYValue() {
      return ((UniDirectionalFunc) next).getLastYValue() + shiftY;
    }
  }

  public static class Scale extends CommonTrans.Scale implements UniDirectionalFunc {
    public Scale(final double factorX, final double factorY) {
      super(factorX, factorY);
    }
    public Scale() {
      //
    }
    @Override
    public double getEndThreshold() {
      return ((UniDirectionalFunc) next).getEndThreshold() * factorX;
    }
    @Override
    public double getLastYValue() {
      return ((UniDirectionalFunc) next).getLastYValue() * factorY;
    }
  }

  public static class ScaleX extends CommonTrans.ScaleX implements UniDirectionalFunc {
    public ScaleX(final double factorX) {
      super(factorX);
    }
    public ScaleX() {
      //
    }
    @Override
    public double getEndThreshold() {
      return ((UniDirectionalFunc) next).getEndThreshold() * factorX;
    }
    @Override
    public double getLastYValue() {
      return ((UniDirectionalFunc) next).getLastYValue();
    }
  }

  public static class ScaleY extends CommonTrans.ScaleY implements UniDirectionalFunc {
    public ScaleY(final double factorY) {
      super(factorY);
    }
    public ScaleY() {
      //
    }
    @Override
    public double getEndThreshold() {
      return ((UniDirectionalFunc) next).getEndThreshold();
    }
    @Override
    public double getLastYValue() {
      return ((UniDirectionalFunc) next).getLastYValue() * factorY;
    }
  }

}