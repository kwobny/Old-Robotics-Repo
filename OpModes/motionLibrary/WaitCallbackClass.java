package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import com.qualcomm.robotcore.util.ElapsedTime;

public class WaitCallbackClass
{
    //RESOURCE OBJECTS
    private MadHardware mhw;
    private Move movements;
    private ElapsedTime runtime = new ElapsedTime();

    public WaitCallbackClass() {
      
    }

    public void initialize(MadHardware hmw, Move mot) {
      mhw = hmw;
      movements = mot;
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