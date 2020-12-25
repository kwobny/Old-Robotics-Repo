package LithiumCore.Utils.MathFunctions;

public class CommonTrans {

  public static class Translate extends Transformation {

    public double shiftX;
    public double shiftY;

    public Translate(final double shiftX, final double shiftY) {
      this.shiftX = shiftX;
      this.shiftY = shiftY;
    }
    public Translate() {
      //
    }

    @Override
    protected double alteredX(final double input) {
      return input - shiftX;
    }
    @Override
    protected double alteredY(final double input) {
      return input + shiftY;
    }
  }
  
  public static class TranslateX extends Transformation {

    public double shiftX;

    public TranslateX(final double shiftX) {
      this.shiftX = shiftX;
    }
    public TranslateX() {
      //
    }

    @Override
    protected double alteredX(final double input) {
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
    protected double alteredY(final double input) {
      return input + shiftY;
    }
  }

  public static class Scale extends Transformation {

    public double factorX;
    public double factorY;

    public Scale(final double factorX, final double factorY) {
      this.factorX = factorX;
      this.factorY = factorY;
    }
    public Scale() {
      //
    }

    @Override
    protected double alteredX(final double input) {
      return input/factorX;
    }
    @Override
    protected double alteredY(final double input) {
      return input * factorY;
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
    protected double alteredX(final double input) {
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
    protected double alteredY(final double input) {
      return input * factorY;
    }
  }

}