package org.firstinspires.ftc.teamcode.OpModes.motionLibrary.MathFunctions;

public class CommonTrans {
  
  public static class TranslateX extends Transformation {

    public double shiftX;

    public TranslateX(final double shiftX) {
      this.shiftX = shiftX;
    }
    public TranslateX() {
      //
    }

    @Override
    public double alteredX(final double input) {
      return input - shiftX;
    }
  }

  public static class TranslateY extends Transformation {

    public double shiftY;

    public TranslateY(final double shiftY) {
      this.shiftY = shiftY;
    }
    public TranslateY() {
      //
    }

    @Override
    public double alteredY(final double input) {
      return input + shiftY;
    }
  }

  public static class ScaleX extends Transformation {

    public double factorX;

    public ScaleX(final double factorX) {
      this.factorX = factorX;
    }
    public ScaleX() {
      //
    }

    @Override
    public double alteredX(final double input) {
      return input/factorX;
    }
  }

  public static class ScaleY extends Transformation {

    public double factorY;

    public ScaleY(final double factorY) {
      this.factorY = factorY;
    }
    public ScaleY() {
      //
    }

    @Override
    public double alteredY(final double input) {
      return input * factorY;
    }
  }

}