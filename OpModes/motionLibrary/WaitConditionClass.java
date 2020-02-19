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

    //wait example
    /*
    public PollInterface exampleWaitCode = new PollInterface() {
        public Object[] generateData(Object[] args) {
          return new Object[]{args[0]};
        }

        public boolean pollCondition(Object[] data) {
          boolean condition = (Boolean) data[0];
          return condition;
          //return true if wait is over, return false if wait is still going on
        }
    };

    public void exampleWait(boolean condition) {
        simpleWait(exampleWaitCode, new Object[]{condition});
    }
    public void exampleWait(boolean condition, boolean pauseCodeExecution, CallbackInterface onCondition) {
        if (pauseCodeExecution) {
          addWait(exampleWaitCode, new Object[]{condition}, onCondition);
        }
        else {
          setTimeout(exampleWaitCode, new Object[]{condition}, onCondition);
        }
    }

    //wait for time
    public PollInterface waitTimeCode = new PollInterface() {
        public Object[] generateData(Object[] args) {
          return new Object[]{10.0, args[0]};
        }

        public boolean pollCondition(Object[] data) {
          double condition = (Double) data[0];
          return true;
          //return true if wait is over, return false if wait is still going on
        }
    };

    public void waitTime(double time) {
        simpleWait(waitTimeCode, new Object[]{time});
    }
    public void waitTime(double time, boolean pauseCodeExecution, CallbackInterface onCondition) {
        if (pauseCodeExecution) {
          addWait(waitTimeCode, new Object[]{time}, onCondition);
        }
        else {
          setTimeout(waitTimeCode, new Object[]{time}, onCondition);
        }
    }
    */
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
                runBaseInterval();
                break;
        }
    }

    private void runBaseInterval() {
        movements.motorCali();
    }
}