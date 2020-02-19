/*
*   Created by Adam Sher on 11/3/18
*   This class will be used to control the robot during the teleop period.
*   It will drive in tank mode.
 */


package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.HardwareMiniBots;

import java.lang.Override;

@Disabled
@TeleOp(name = "TeleOp RUNME", group = "TeleOp")   // How opMode is displayed on phones
public class TeleOpMiniBots extends OpMode {


    // Initializing the hardware objects (motors).
    HardwareMiniBots hw = new HardwareMiniBots();
    // ElapsedTime runtime = new ElapsedTime();


    // This method is run once when the "INIT" button is pressed on the phone.
    @Override
    public void init()
    {
        // addData calls are used for debugging.
        telemetry.addData("Status", "Initializing...");

        // Initialize motor controller (effectively calling HardwareMiniBots.init())
        hw.init(hardwareMap);

        telemetry.addData("Status", "Initialized, awaiting for start.");
    }



    // This method is run repeatedly after the play button is pressed on the phone.
    @Override
    public void loop() {

        telemetry.addData("Status", "Teleop started, driving.");

        double linearPowerMultiplier = 1;
        double turnPowerMultiplier = 0.15;

        double linearPower = -gamepad1.left_stick_y * Math.abs(gamepad1.left_stick_y) * linearPowerMultiplier;
        double turnPower = gamepad1.left_stick_x * Math.abs(gamepad1.left_stick_x) * turnPowerMultiplier;

        double leftPower = linearPower + turnPower;
        double rightPower = linearPower - turnPower;

        if (leftPower > 1)
            leftPower = 1;
        if (leftPower < -1)
            leftPower = -1;
        if (rightPower > 1)
            rightPower = 1;
        if (rightPower < -1)
            rightPower = -1;

        hw.leftFront.setPower(leftPower);
        hw.leftBack.setPower(leftPower);
        hw.rightFront.setPower(rightPower);
        hw.rightBack.setPower(rightPower);

        hw.leftShoulder.setPower(gamepad1.left_trigger);
        hw.rightShoulder.setPower(gamepad1.left_trigger);

        hw.leftElbow.setPower(gamepad1.right_trigger > 0.1 ? 0.3 : 0.0);
        hw.rightElbow.setPower(gamepad1.right_trigger > 0.1 ? 0.3 : 0.0);

        hw.leftElbow.setPower(gamepad1.right_bumper ? -0.3 : hw.leftElbow.getPower());
        hw.rightElbow.setPower(gamepad1.right_bumper ? -0.3 : hw.rightElbow.getPower());


    }



    // This method is run once when the stop button is pressed on the phone.
    @Override
    public void stop()
    {
		telemetry.addData("Status", "Teleop stop successful.");

    }
} // End of class