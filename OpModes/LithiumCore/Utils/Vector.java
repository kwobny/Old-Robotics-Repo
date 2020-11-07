package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils;

//Is a class which specifies a direction and provides a bunch of utilities.
//Can also be used as a point class.
public class Vector {
  public double x;
  public double y;

  //CONSTRUCTORS

  public Vector() {
    //
  }
  public Vector(final double x, final double y) {
    this.x = x;
    this.y = y;
  }
  public static Vector getVector(final double x, final double y) {
    return new Vector(x, y);
  }

  //IN PLACE UTILITIES

  //scales the vector by a certain factor
  public Vector scale(final double factor) {
    x *= factor;
    y *= factor;
    return this;
  }
  //scales the length of the vector (vector's hypotenuse) to the supplied length.
  public Vector fitLength(final double length) {
    final double oldLength = Math.sqrt(x*x + y*y);
    return scale(length/oldLength);
  }
  public Vector fitLength() {
    return fitLength(1.0);
  }

  //OUT OF PLACE UTILITIES

  //returns a vector containing the difference between this vector and the supplied vector. Effectively "this - vect"
  public Vector difference(final Vector vect) {
    return new Vector(x - vect.x, y - vect.y);
  }
  //returns a vector containing sum of this vector and supplied vector. Effectively "this + vect"
  public Vector sum(final Vector vect) {
    return new Vector(x + vect.x, y + vect.y);
  }
}