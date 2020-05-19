package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import com.qualcomm.robotcore.util.ElapsedTime;

public class WaitConditionClass
{
    //RESOURCE OBJECTS
    private MadHardware mhw;
    private Main main;
    private ElapsedTime runtime = new ElapsedTime();

    WaitConditionClass() {} //cannot be instantiated outside of package

    public void initialize(MadHardware hmw, Main main) {
      mhw = hmw;
      this.main = main;
    }

    public Object[] generateData(WaitEnum waitType, Object[] args) {
        switch (waitType) {
            case TIME:
                return generateTimeData(args);
            default:
                return new Object[0];
        }
    }
    //returning true means condition is met and break out of wait, false means that program is still in wait.
    public boolean pollCondition(WaitEnum waitType, Object[] data) {
        switch (waitType) {
            case TIME:
                return pollTime(data);
            default:
                return true;
        }
    }

    //START WAIT COMMANDS
    //parameters:
    //1. amount of time to wait for (in seconds)

    public double changeInTime = 0;
    private Object[] generateTimeData(Object[] args) {
      return new Object[]{args[0], runtime.time()};
    }
    private boolean pollTime(Object[] data) {
      changeInTime = runtime.time() - (double) (Double) data[1];
      return changeInTime > (double) (Double) data[0];
    }

    //END WAIT COMMANDS

    //WAIT FOR DISTANCE
    //parameter list:
    //1. distance in cm (double).
    //2. flag if measuring change in distance (true) or just plain distance (false)
    private Object[] generateDistanceData(Object[] args) {
      if ((Boolean) args[1]) {
        return new Object[]{(Double) args[0] + main.rps.getDistanceTraveled()[0]};
      }
      else {
        return new Object[]{args[0]};
      }
    }
    private boolean pollDistance(Object[] data) {
      return main.rps.getDistanceTraveled() > (Double) data[0];
    }

    //WAIT FOR DISPLACEMENT/UNTIL AT POSITION
    //parameter list:
    //1. x-position/displacement (in cm, double)
    //2. y-position/displacement (in cm, double)
    //3. flag if waiting until at position (false) or waiting until displacement traveled (true)
    //4 & 5. Optional. Are booleans representing which quadrant of the coordinate plane the robot has to be on relative to the point. The two parameters are x and y in that order. True represents positive direction, and false represents negative. Default is the quandrant facing away from the current robot location.
    
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
    private boolean pollDisplacement(Object[] data) {
      //
    }

    //WAIT FOR ANGLE
    private Object[] generateAngleData(Object[] args) {
      //
    }
    private boolean pollAngle(Object[] data) {
      //
    }
}