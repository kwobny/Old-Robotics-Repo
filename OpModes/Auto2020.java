package org.firstinspires.ftc.teamcode.madmachines2020.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.madmachines2020.Backend.MadHardware;
import org.firstinspires.ftc.teamcode.madmachines2020.lithiumcore.*;
import org.firstinspires.ftc.teamcode.madmachines2020.lithiumcore.sharedstate.*;

@Autonomous(name = "Yeongjin's autonomous \"yay!\"", group = "Autos")   // How opmode is displayed on phones
public class Auto2020 extends LinearOpMode
{

    // Initializing the motor-control class.
    public static MadHardware mhw = new MadHardware();
    //public static ElapsedTime runtime = new ElapsedTime();

    // This method is run once when the "INIT" button is pressed on the phone.
    @Override
    public void runOpMode()
    {

        {
          //mhw.flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }

        mhw.initHardware(hardwareMap);

        final Main robotLib = new Main(mhw, new MadMachinesConstants());

        waitForStart();

        robotLib.startAutonomous();

        robotLib.move.translate(0, 1);
        robotLib.move.syncMotors();

        robotLib.wait.waitFor(robotLib.time.getWait(2).start());

        robotLib.move.clearAll();
        robotLib.move.syncMotors();

        robotLib.endAutonomous();
    }
} // End of class