package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import com.qualcomm.robotcore.util.ElapsedTime;

public class CommonWaits {

  Main main;

  public CommonWaits(Main m) {
    main = m;
  }

  public class Time implements WaitCondition {

    private double time_delay;
    private double start_time;
    public double changeInTime;

    public Time(final double delay) {
      time_difference = delay;
      start_time = main.mhw.runtime.time();
    }

    @override
    public boolean pollCondition() {
      changeInTime = main.mhw.runtime.time() - start_time;
      return changeInTime > time_delay;
    }
  }

  public class Distance implements WaitCondition {

    private double target_distance;

    //parameter list:
    //1. distance in cm (double).
    //2. flag if measuring change in distance (true) or just plain distance (false)
    public Distance(final double distance, final boolean measure_mode) {
      if (measure_mode) {
        target_distance = distance + main.rps.getDistanceTraveled()[0];
      }
      else {
        target_distance = distance;
      }
    }

    @override
    public boolean pollCondition() {
      return main.rps.getDistanceTraveled()[0] > target_distance;
    }
  }
}

/*
//WAIT FOR DISPLACEMENT/UNTIL AT POSITION
//parameter list:
//1. x-position/displacement (in cm, double)
//2. y-position/displacement (in cm, double)
//3. flag if waiting until at position (false) or waiting until displacement traveled (true)
//4 & 5. Optional parameters. Are angle measures which indicate what sector surrounding the target position/displacement the robot has to be in for the condition to be satisfied. The sector starts at the first parameter and ends at the second parameter. Default is a sector with its bisector being parallel to the line connecting the robot position to target, its size being predefined in the constants.

//Three things needed to be done:
//1. work out the total angle unit situation (if you are using boolean to represent if using radians or some other unit, maybe have a unit to radians conversion factor constant)
//2. work out how displacement is going to work
//3. work out how wait for angle is going to work.
private Object[] generateDisplacementData(Object[] args) {
  Object quadrantX, quadrantY;
  double[] pos = main.rps.getPosition();
  if (args[2]) {
    if (args.length == 3) {
      quadrantX = (Double) args[0] > 0.0;
      quadrantY = (Double) args[1] > 0.0;
    }
    else {
      quadrantX = args[3];
      quardantY = args[4];
    }
    return new Object[]{(Double) args[0] + pos[0], (Double) args[1] + pos[1], quadrantX, quadrantY};
  }
  else {
    if (args.length == 3) {
      quadrantX = (Double) args[0] > pos[0];
      quadrantY = (Double) args[1] > pos[1];
    }
    else {
      quadrantX = args[3];
      quardantY = args[4];
    }
  }
}
*/