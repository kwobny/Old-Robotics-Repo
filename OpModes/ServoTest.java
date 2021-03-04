package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Backend.MadHardware;

import org.firstinspires.ftc.teamcode.LithiumCore.Main;

@Autonomous(name = "Yeongjin's autonomous \"yay!\"", group = "Autos")   // How opmode is displayed on phones
public class Auto2020 extends LinearOpMode
{

    // Initializing the motor-control class.
    public static MadHardware mhw = new MadHardware();
    
    // This method is run once when the "INIT" button is pressed on the phone.
    @Override
    public void runOpMode()
    {
        mhw.initHardware(hardwareMap);

        final Main robotLib = new Main(mhw, new MadMachinesConstants());

        waitForStart();

        robotLib.startAutonomous();

        // robotLib.move.translate(0, 1);
        // robotLib.move.syncMotors();

        // robotLib.wait.waitFor(robotLib.time.getWait(3.14159265).start());

        // robotLib.move.clearAll();
        // robotLib.move.syncMotors();
        
        

        robotLib.endAutonomous();
    }
} // End of class