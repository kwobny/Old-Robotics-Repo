package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Backend.MadHardware;

import org.firstinspires.ftc.teamcode.LithiumCore.Main;
package org.firstinspires.ftc.teamcode.LithiumCore.Utils.Vector;

@Autonomous(name = "Yeongjin's autonomous \"yay!\"", group = "Autos")   // How opmode is displayed on phones
public class Auto2020 extends LinearOpMode
{

    // Initializing the motor-control class.
    public static MadHardware mhw = new MadHardware();
    
    // The velocity (meters/second) traveled per power (no units).
    // dx/(dt*power)
    final double velocityPerPower = 0.3;
    private void driveDistance(double rx, double ry, double distance) {
        robotLib.move.translate(rx, ry);
        robotLib.move.syncMotors();

        robotLib.wait.waitFor(robotLib.time.getWait(distance/(velocityPerPower*Vector.length(rx, ry))).start());

        robotLib.move.clearAll();
        robotLib.move.syncMotors();
    }
    
    // This method is run once when the "INIT" button is pressed on the phone.
    @Override
    public void runOpMode()
    {
        mhw.initHardware(hardwareMap);

        final Main robotLib = new Main(mhw, new MadMachinesConstants());

        waitForStart();

        robotLib.startAutonomous();

        robotLib.move.translate(0, 1);
        robotLib.move.syncMotors();

        robotLib.wait.waitFor(robotLib.time.getWait(5.0).start());

        robotLib.move.clearAll();
        robotLib.move.syncMotors();

        robotLib.endAutonomous();
    }
} // End of class