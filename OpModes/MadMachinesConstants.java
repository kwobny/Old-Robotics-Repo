package org.firstinspires.ftc.teamcode.madmachines2020.OpModes;

import org.firstinspires.ftc.teamcode.madmachines2020.LithiumCore.SharedState.ConstantsContainer;
import org.firstinspires.ftc.teamcode.madmachines2020.LithiumCore.SharedState.RobotParameters;

public class MadMachinesConstants extends ConstantsContainer {
    public MadMachinesConstants() {
        robotParameters = new RobotParameters() {
            @Override
            protected void _initialize() {
                //all in centimeters
                robotWidth = 38.5;
                robotLength = 26.5;
                wheelDiameter = 7.75;

                ticksPerRevolution = 28;
            }
        };

        defaultInitialize();
    }
}
