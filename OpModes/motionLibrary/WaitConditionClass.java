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
    public double changeInTime = 0;
    private Object[] generateTimeData(Object[] args) {
      double time = (Double) args[0];
      return new Object[]{time, runtime.time()};
    }
    private boolean pollTime(Object[] data) {
      changeInTime = runtime.time() - (double) (Double) args[1];
      return changeInTime > (double) (Double) data[0];
    }

    //END WAIT COMMANDS

    //WAIT FOR DISTANCE
    //parameter list:
    //1. distance in cm (double).
    //2. flag if measuring change in distance or just plain distance
    private Object[] generateDistanceData(Object[] args) {
      if ((Boolean) args[1]) {
        return new Object[]{(Double) args[0] + main.rps.getDistanceTraveled()};
      }
      else {
        return new Object[]{args[0]};
      }
    }
    private boolean pollDistance(Object[] data) {
      return main.rps.getDistanceTraveled() > (Double) data[0];
    }

    //WAIT FOR DISPLACEMENT
    private Object[] generateDisplacementData(Object[] args) {
      //
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