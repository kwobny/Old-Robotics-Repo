package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import com.qualcomm.robotcore.util.ElapsedTime;

public class WaitConditionClass
{
    private MadHardware mhw;
    private Motions movements;
    private ElapsedTime runtime;

    WaitConditionClass(MadHardware hmw, Motions mot, ElapsedTime r) {
        mhw = hmw;
        movements = mot;
        runtime = r;
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
    private Object[] generateTimeData(Object[] args) {
      double time = (Double) args[0];
      return new Object[]{time + runtime.time()};
    }
    private boolean pollTime(Object[] data) {
      return (double) (Double) data[0] < runtime.time();
    }

    //END WAIT COMMANDS
}

public class WaitCallbacks
{
    private MadHardware mhw;
    private Motions movements;
    private ElapsedTime runtime;

    WaitCallbacks(MadHardware hmw, Motions mot, ElapsedTime r) {
        mhw = hmw;
        movements = mot;
        runtime = r;
    }

    public void callback(int which) {
        switch (which) {
            case 1:
              movements.runBaseLowInterval();
              break;
            case 2:
              movements.runBaseHighInterval();
              break;
        }
    }
}