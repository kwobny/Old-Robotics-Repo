package MathFunctions;

public class CommonTrans {
  
  public static class Translate extends Transformation {

    public double shiftX;
    public double shiftY;

    public Translate(final double shiftX, final double shiftY) {
      this.shiftX = shiftX;
      this.shiftY = shiftY;
    }

    public double alteredX(final double input) {
      return input - shiftX;
    }

    public double alteredY(final double output) {
      return output + shiftY;
    }
  }

  public static class Scale extends Transformation {

    public double scaleX;
    public double scaleY;

    public Scale(final double scaleX, final double scaleY) {
      this.scaleX = scaleX;
      this.scaleY = scaleY;
    }

    public double alteredX(final double input) {
      return input/scaleX;
    }

    public double alteredY(final double output) {
      return output * scaleY;
    }
  }

}