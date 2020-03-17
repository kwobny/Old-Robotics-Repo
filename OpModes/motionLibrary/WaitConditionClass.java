package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import com.qualcomm.robotcore.util.ElapsedTime;

public class WaitConditionClass
{
    //RESOURCE OBJECTS
    private MadHardware mhw;
    private Move movements;
    private ElapsedTime runtime = new ElapsedTime();

    public WaitConditionClass() {
      
    }

    public void initialize(MadHardware hmw, Move mot) {
      mhw = hmw;
      movements = mot;
    }

    public Object[] generateData(WaitEnum waitType, Object[] args) {
        switch (waitType) {
            case TIME:
                return generateTimeData(args);
            default:
                return new Object[0];
        }
    }
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
}