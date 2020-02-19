/*
 *   Created by Adam Sher on 11/3/18
 *   This class will be used to control the robot during the teleop period.
 *   It will drive in tank mode.
 */


package org.firstinspires.ftc.teamcode.OldCode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import java.lang.Override;


@Disabled
@TeleOp(name = "Test_TeleOp", group = "TeleOp")   // How opMode is displayed on phones
public class Test_TeleOp extends OpMode {


    // Initializing the hardware objects (motors).
    Test_Hardware mhw = new Test_Hardware();


    // This method is run once when the "INIT" button is pressed on the phone.
    @Override
    public void init()
    {
        // addData calls are used for debugging.
        telemetry.addData("Status", "Initializing...");

        // Initialize motor controller (effectively calling MadHardware.init())
        mhw.init(hardwareMap);

        telemetry.addData("Status", "Initialized, awaiting teleOp start.");
    }



    // This method is run once when the play button is pressed on the phone.
    @Override
    public void start() {
        telemetry.addData("Status", "Teleop start successful.");

//          dont delete the program breaks no idea why
//        int x = 0;
//        int y = (x==0 ? 0 : 1);
//        y = 0 if x is (2 if x is 3 else 3) else 1;
    }


    // This method is run repeatedly after the play button is pressed on the phone.
    @Override
    public void loop() {
        telemetry.addData("Status", "Teleop started, driving.");


        // These variables describe the power to each motor. Initialize them to 0 power.
        double leftPower = 0;
        double rightPower = 0;


        // Assign the power of each motor based on the joystick position.
        // Multiply each by -1 to flip orientation of sticks.
        // Driver: gamepad1. Gunner: gamepad2
        leftPower = gamepad1.left_stick_y;
        rightPower = gamepad1.right_stick_y;

        if (!gamepad1.right_bumper) // Normal driving mode: Set drive motor powers to 33%
        {
            mhw.leftDrive.setPower(0.25 * leftPower);
            mhw.rightDrive.setPower(0.25 * rightPower);
            telemetry.addData("LeftDrive: ", 0.25 * leftPower);
            telemetry.addData("RightDrive: ", 0.25 * rightPower);
        }
        else // TURBO mode activate! Set drive motor powers to 100%
        {
            mhw.leftDrive.setPower(0.75 * leftPower);
            mhw.rightDrive.setPower(0.75 * rightPower);
            telemetry.addData("LeftDrive: ", 0.75 * leftPower);
            telemetry.addData("RightDrive: ", 0.75 * rightPower);
        }
    }


    // This method is run once when the stop button is pressed on the phone.
    @Override
    public void stop()
    {
        telemetry.addData("Status", "Teleop stop successful.");
    }
} // End of class
