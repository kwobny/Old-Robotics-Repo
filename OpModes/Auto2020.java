package org.firstinspires.ftc.teamcode.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Backend.MadHardware;
import org.firstinspires.ftc.teamcode.LithiumCore.*;
import org.firstinspires.ftc.teamcode.LithiumCore.SharedState.*;
import org.firstinspires.ftc.teamcode.LithiumCore.Utils.Vector;
import org.firstinspires.ftc.teamcode.LithiumCore.Wait_Package.WaitCondition;

@Autonomous(name = "Yeongjin's autonomous \"yay!\"", group = "Autos")   // How opmode is displayed on phones
public class Auto2020 extends LinearOpMode
{

    // Initializing the motor-control class and other constants.
    private static final MadHardware mhw = new MadHardware();
    private static final MadMachinesConstants constants = new MadMachinesConstants();
    private static final Main robotLib = new Main(mhw, constants);

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

        // Input distance in centimeters.
        driveForward(1.0, distance);

        robotLib.move.clearAll();
        robotLib.move.syncMotors();

        robotLib.endAutonomous();
    }

    // Drives forward or backward at the provided power for a certain distance. Does not stop after drive is over.
    // This is a blocking method.
    // This method takes a power vector. Forward is positive, backward is negative.
    // And a distance parameter in whatever unit the robot distances are measured in (I think centimeters).
    // Power can be positive or negative,
    // distance is always positive.
    private void driveForward(double power, double distance) {
        final double distanceInTicks = distance/constants.robotParameters.distancePerTick;
        // Left front, right front, left rear, right rear
        final int[] initialPositions = {
            mhw.leftFront.getCurrentPosition(),
            mhw.rightFront.getCurrentPosition(),
            mhw.leftRear.getCurrentPosition(),
            mhw.rightRear.getCurrentPosition()
        };

        robotLib.move.translate(0.0, power);
        robotLib.move.syncMotors();

        robotLib.wait.waitFor(new WaitCondition() {
            @Override
            public boolean pollCondition() {
                final int leftFrontDelta = mhw.leftFront.getCurrentPosition() - initialPositions[0];
                final int rightFrontDelta = mhw.rightFront.getCurrentPosition() - initialPositions[1];
                final int leftRearDelta = mhw.leftRear.getCurrentPosition() - initialPositions[2];
                final int rightRearDelta = mhw.rightRear.getCurrentPosition() - initialPositions[3];

                final double averageTicksTraveled
                = Math.abs(
                    leftFrontDelta/4.0
                    + rightFrontDelta/4.0
                    + leftRearDelta/4.0
                    + rightRearDelta/4.0
                );

                return averageTicksTraveled >= distanceInTicks;
            }
        });
    }
} // End of class
