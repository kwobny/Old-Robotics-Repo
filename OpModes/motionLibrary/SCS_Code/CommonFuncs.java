package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

public class CommonOps {

  //constant acceleration
  public static class ConstAccel extends MathFunction {

    public double acceleration;
    public double initialVelocity;

    public ConstAccel(final double accel, final double initialVelocity) {
      this.acceleration = accel;
      this.initialVelocity = initialVelocity;
    }
    
    @Override
    public double yValueOf(final double x) {
      return acceleration * x + initialVelocity; //y = mx + b
    }

  }

  //used for a section of speed that has a constant jerk
  public static class ConstJerk extends MathFunction {

    public double jerk;
    public double initialAcceleration;
    public double initialVelocity;

    public ConstJerk(final double jerk, final double initialAcceleration, final double initialVelocity) {
      this.jerk = jerk;
      this.initialAcceleration = initialAcceleration;
      this.initialVelocity = initialVelocity;
    }
    
    @Override
    public double yValueOf(final double x) {
      return 1/2 * jerk * (x*x) + initialAcceleration * x + initialVelocity;
      //1/2 ax^2 + vx + d
    }

  }

}