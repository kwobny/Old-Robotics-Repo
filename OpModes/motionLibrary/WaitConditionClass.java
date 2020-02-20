package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;

public class WaitConditionClass
{
    private MadHardware mhw;
    private Motions movements;

    WaitConditionClass(MadHardware hmw, Motions mot) {
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
    private Object[] generateTimeData(Object[] args) {
        return new Object[]{};
    }
    private boolean pollTime(Object[] data) {
        return true;
    }

    //END WAIT COMMANDS
}

class WaitCallbacks
{
    private MadHardware mhw;
    private Motions movements;

    WaitCallbacks(MadHardware hmw, Motions mot) {
        mhw = hmw;
        movements = mot;
    }

    void callback(int which) {
        switch (which) {
            case 1:
              runBaseLowInterval();
              break;
            case 2:
              runBaseHighInterval();
              break;
        }
    }

    private void runBaseLowInterval() {
      movements.motorCali();
    }
    private void runBaseHighInterval() {
      //
    }
}