package lithiumcore.utils;

import java.util.Objects;

//Is a class which specifies a direction and provides a bunch of utilities.
//Can also be used as a point class.
public class Vector implements Cloneable {

  public double x;
  public double y;

  //CONSTRUCTORS

  public Vector() {
    //
  }
  public Vector(final double x, final double y) {
    setBoth(x, y);
  }
  //this constructor can be used to obtain a copy of a vector.
  public Vector(final Vector vect) {
    this(vect.x, vect.y);
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
    return vect.clone();
  }

  //instance method
  @Override
  public Vector clone() {
    return (Vector) super.clone();
  }

  //VECTOR EQUALS
  public static boolean equals(final Vector a, final Vector b) {
    return Objects.equals(a, b);
  }
  @Override
  public boolean equals(final Object o) {
    if (o instanceof Vector) {
      return equals((Vector) o);
    }
    return false;
  }
  public boolean equals(final Vector vect) {
    return vect == this
    ||
    (vect != null && StaticUtils.equals(x, vect.x) && StaticUtils.equals(y, vect.y));
  }

  //Functions to set x and y variables.
  //Although you can set and access the x and y variables directly, it is recommended to set them using these methods because they check for NaN.
  public Vector setX(final double value) {
    if (Double.isNaN(value)) {
      throw new RuntimeException("The x value that you provided into the setX function (or constructor, or setBoth) was NaN. That is invalid.");
    }
    this.x = value;
    return this;
  }
  public Vector setY(final double value) {
    if (Double.isNaN(value)) {
      throw new RuntimeException("The y value that you provided into the setY function (or constructor, or setBoth) was NaN. That is invalid.");
    }
    this.y = value;
    return this;
  }
  public Vector setBoth(final double x, final double y) {
    setX(x);
    return setY(y);
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