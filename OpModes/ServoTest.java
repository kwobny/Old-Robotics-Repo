package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Backend.MadHardware;

import org.firstinspires.ftc.teamcode.LithiumCore.Main;

@Autonomous(name = "Servo test", group = "Autos")   // How opmode is displayed on phones
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

        for (double i : new double[]{0.25, -0.5, 0.75, -1.0}) {
            mhw.tempServo.setPower(i);
            robotLib.wait.waitFor(robotLib.time.getWait(2).start());
        }
        mhw.tempServo.setPower(0.0);

        robotLib.endAutonomous();
    }
} // End of class