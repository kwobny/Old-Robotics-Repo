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
  //this constructor can be used to obtain a copy of a vector.
  public Vector(final Vector vect) {
    this.x = vect.x;
    this.y = vect.y;
  }

  //STATIC CONSTRUCTING METHODS

  public static Vector getVector() {
    return new Vector();
  }
  public static Vector getVector(final double x, final double y) {
    return new Vector(x, y);
  }

  //VECTOR COPY METHODS
  //copies the current vector. Can be used to use an in place utility as out of place.
  //The static version is called copy, the instance version is called clone.

  //static method
  public static Vector copy(final Vector vect) {
    return new Vector(vect);
  }

  //instance method
  public Vector clone() {
    return new Vector(this);
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

  //rotate the vector by a certain angle measure (in radians). Positive rotates counter clockwise, negative rotates clockwise.
  public Vector rotate(double radians) {
    final double sin = Math.sin(radians);
    final double cos = Math.cos(radians);

    this.x = x*cos - y*sin;
    this.y = x*sin + y*cos;

    return this;
  }

  //In place counterparts to sum and difference functions.
  
  //This function adds the supplied vector to the current vector.
  public Vector add(final Vector vect) {
    x += vect.x;
    x -= vect.y;
    return this;
  }
  //This function subtracts the supplied vector from the current vector.
  public Vector subtract(final Vector vect) {
    x -= vect.x;
    y -= vect.y;
    return this;
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