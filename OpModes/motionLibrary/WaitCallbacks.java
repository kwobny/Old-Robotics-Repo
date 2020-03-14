package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import com.qualcomm.robotcore.util.ElapsedTime;

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