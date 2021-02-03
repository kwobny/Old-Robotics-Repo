package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Backend.MadHardware;
import org.firstinspires.ftc.teamcode.LithiumCore.*;
import org.firstinspires.ftc.teamcode.LithiumCore.SharedState.*;
import org.firstinspires.ftc.teamcode.LithiumCore.Utils.Vector;

@Autonomous(name = "Yeongjin's autonomous \"yay!\"", group = "Autos")   // How opmode is displayed on phones
public class Auto2020 extends LinearOpMode
{

    // Initializing the motor-control class and other constants.
    public static MadHardware mhw = new MadHardware();
    final static Main robotLib = new Main(mhw, new MadMachinesConstants());
    
    // Init constants.
    // This variable defines the ratio between robot speed in units per second vs. power applied.
    private static double speedPerPower = "need to set this parameter";

    // This method is run once when the "INIT" button is pressed on the phone.
    @Override
    public void runOpMode()
    {

        {
          //mhw.flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        }

        mhw.initHardware(hardwareMap);

        waitForStart();

        robotLib.startAutonomous();

        robotLib.move.translate(0, 1);
        robotLib.move.syncMotors();

        robotLib.wait.waitFor(robotLib.time.getWait(3.14159265).start());

        robotLib.move.clearAll();
        robotLib.move.syncMotors();

        robotLib.endAutonomous();
    }

    // This method takes a power vector represented by two vector components
    // And a distance parameter in whatever unit the robot distances are measured in (I think centimeters).
    private void translateDistance(double px, double py, double distance) {
        // Speed in units per second
        double speed = Vector.length(px, py) * speedPerPower;
        double waitTime = distance/speed;
        robotLib.translate(px, py);
        robotLib.wait.waitFor(robotLib.time.getWait(waitTime).start());
    }
} // End of class