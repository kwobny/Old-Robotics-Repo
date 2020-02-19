/*
*   Created by Adam Sher on 11/3/18
*   This class will be used to control the robot during the teleop period.
*   It will drive in tank mode.
*
*   Revision by Yeongjin Kwon, Jacob Jordan, and Adam Sher on 12/13/19
*   This class now includes important methods for strafing and strafe-related
*   movement, in addition to controlling this year's arm.
 */


package org.firstinspires.ftc.teamcode.OpModes.Teleop;

import com.qualcomm.ftccommon.configuration.EditLegacyServoControllerActivity;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import java.lang.Override;

// TODO: Organize code and combine redundant methods. Determine if hyperStrafe is needed.
// TODO: Look into using github

@TeleOp(name = "TeleOp 1920", group = "TeleOp")   // How opMode is displayed on phones
public class MainTeleOp extends OpMode {


    // Initializing the hardware objects (motors).
    MadHardware mhw = new MadHardware();

    boolean lock = false;
    double lockPower = 0;
    boolean lockWasPressed = false;
    boolean doUS = true;
    boolean uSButtonPressed = false;

    double[] globalPos = {0,0,0,0};
    double[] globalPow = {0,0,0,0};

    // This method is run once when the "INIT" button is pressed on the phone.
    @Override
    public void init()
    {
        // addData calls are used for debugging.
        telemetry.addData("Status", "Initializing...");

        // Initialize motor controller (effectively calling MadHardware.init())
        mhw.init(hardwareMap);

        telemetry.addData("Status", "Initialized, awaiting teleop start.");
    } // END OF INIT METHOD



    // This method is run once when the play button is pressed on the phone.
    @Override
    public void start() {
        telemetry.addData("Status", "Teleop start successful.");

        mhw.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mhw.arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        mhw.arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        mhw.leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mhw.rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mhw.rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mhw.leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


//          dont delete the program breaks no idea why
//        int x = 0;
//        int y = (x==0 ? 0 : 1);
//        y = 0 if x is (2 if x is 3 else 3) else 1;
    } // END OF START METHOD



    @Override
    public void loop() {
        telemetry.addData("Status", "Teleop started, driving.");

        final double ARM_SHOULDER_POWER = 0.4;
        final double TRIGGER_THRESHOLD = 0.1;

        /* SET MOVEMENT TYPE */
        int movementType = 0; // 0 -> traditionalDrive, 1 -> ultraStrafe, 2 -> hyperStrafe
        boolean doCorrecting = false;
        /* SET MOVEMENT TYPE */

        if (!uSButtonPressed && gamepad1.left_bumper) {
            doUS = !doUS;
        }
        uSButtonPressed = gamepad1.left_bumper;

        if (doUS) {
            if (gamepad1.left_trigger > 0.1 || gamepad1.right_trigger > 0.1)
                ultraTurn();
            else
                ultraStrafe();
        }
        else
            traditionalDrive();

        /*
        if (movementType == 0)
            traditionalDrive();
        else if (movementType == 1)
            ultraStrafe();
        else if (movementType == 2)
            hyperStrafe();
        */
        if (doCorrecting)
            motorCali();

        // Shoulder movement
        if (gamepad2.left_stick_y > 0.7) {
            mhw.arm.setPower(ARM_SHOULDER_POWER);
        } else if (gamepad2.left_stick_y < -0.7) {
            mhw.arm.setPower(-0.35 * ARM_SHOULDER_POWER);
        } else {
            mhw.arm.setPower(0);
        }

          mhw.grabberLeft.setPower(-gamepad2.left_trigger);
          mhw.grabberRight.setPower(gamepad2.left_trigger);
    } // END OF LOOP METHOD



    // This method is run once when the stop button is pressed on the phone.
    @Override
    public void stop()
    {
		telemetry.addData("Status", "Teleop stop successful.");
    } // END OF STOP METHOD



    public void hyperStrafe() {
        /* CALCULATE JOYSTICK ANGLE */
        double theta = 0;
        if(gamepad1.left_stick_x != 0) { // This would be bad, dividing by 0. Can skip an iteration
                                         // in this rare case
            if (gamepad1.left_stick_x >= 0) { // Arctan outputs an angle between -90 and 90 degrees
                theta = Math.toDegrees(Math.atan(-gamepad1.left_stick_y / gamepad1.left_stick_x));
            } else { // Will output angle between 90 and 270 degrees
                theta = Math.toDegrees(Math.atan(-gamepad1.left_stick_y / gamepad1.left_stick_x))
                        + 180;
            }
        }
        // Constrain theta just in case
        if (theta < 0) {
            theta += 360;
        } if (theta >= 360) {
            theta -= 360;
        }

        /* CALCULATE DESIRED MOTOR POWERS */
        final double POWER_MULTIPLIER = 0.5;
        double powerFLBR = Math.cbrt(Math.sin(Math.toRadians(theta + 45))) * POWER_MULTIPLIER;
        double powerFRBL = Math.cbrt(Math.sin(Math.toRadians(theta - 45))) * POWER_MULTIPLIER;

        /* ASSIGN MOTORS THEIR POWERS */
        mhw.leftFront.setPower(powerFLBR);
        mhw.rightRear.setPower(powerFLBR);
        mhw.rightFront.setPower(powerFRBL);
        mhw.leftRear.setPower(powerFRBL);
    } // END OF HYPERSTRAFE METHOD

    // This method lets the robot turn
    public void ultraTurn(boolean gamepadControl) {
        final double gamepadTriggerValues = 0.75;
        if (gamepadControl) {
            if (gamepad1.left_trigger > 0.1) {
                mhw.leftFront.setPower(-0.2 * gamepadTriggerValues);
                mhw.leftRear.setPower(-0.2 * gamepadTriggerValues);
                mhw.rightFront.setPower(0.2 * gamepadTriggerValues);
                mhw.rightRear.setPower(0.2 * gamepadTriggerValues);
                double power = 0.2 * gamepadTriggerValues;
                globalPow[0] = -power;
                globalPow[1] = power;
                globalPow[2] = power;
                globalPow[3] = -power;
            } else if (gamepad1.right_trigger > 0.1) {
                mhw.leftFront.setPower(0.2 * gamepadTriggerValues);
                mhw.leftRear.setPower(0.2 * gamepadTriggerValues);
                mhw.rightFront.setPower(-0.2 * gamepadTriggerValues);
                mhw.rightRear.setPower(-0.2 * gamepadTriggerValues);
                double power = 0.2 * gamepadTriggerValues;
                globalPow[0] = power;
                globalPow[1] = -power;
                globalPow[2] = -power;
                globalPow[3] = power;
            }
        }
    }
    public void ultraTurn() {
        ultraTurn(true);
    }

    // This method lets the robot strafe!
    public void ultraStrafe(boolean gamepadControl, double rx, double ry, boolean boostOverride) {

        double BOOOOOST = boostOverride || (gamepadControl && gamepad1.right_bumper) ? 2.0 : 1.0;


        //using encoder to find magnitude of joystick x and y
        if (gamepadControl) {
            rx = gamepad1.right_stick_x;
            ry = -gamepad1.left_stick_y;
        }

        //calculating motor powers
        double a = rx + ry;
        double b = ry - rx;

        //compensating for errors (a>1 or b<-1)
        double maxNumber = Math.max(Math.abs(a), Math.abs(b));
        if (maxNumber > 1) {
            a /= maxNumber;
            b /= maxNumber;
        }

        //sets powers according to compensation
        mhw.leftFront.setPower(0.1 * BOOOOOST * a);
        globalPow[0] = 0.1 * BOOOOOST * a;
        mhw.rightFront.setPower(0.1 * BOOOOOST * b);
        globalPow[1] = 0.1 * BOOOOOST * b;
        mhw.leftRear.setPower(0.1 * BOOOOOST * b);
        globalPow[3] = 0.1 * BOOOOOST * b;
        mhw.rightRear.setPower(0.1 * BOOOOOST * a);
        globalPow[2] = 0.1 * BOOOOOST * a;
    } // END OF ULTRASTRAFE METHOD


    // ultraStrafe pass-through
    public void ultraStrafe() {
        ultraStrafe(true, 0.0, 0.0, false);
    }

    // ultraStrafe in autonomous
    public void ultraStrafe(double rx, double ry) {
        ultraStrafe(false, rx, ry, false);
    }


    // TODO: Complete distanceTravel and setGrabber
    public void distanceTravel(double rx, double ry, double distanceCM) {}
    public void setGrabber(boolean grabberOpen) {}


    public void ultraStrafeDpad() {
        //sets powers according to compensation
        if (gamepad1.dpad_right) {
            mhw.leftFront.setPower(-0.2);
            mhw.leftRear.setPower(0.2);
            mhw.rightFront.setPower(0.2);
            mhw.rightRear.setPower(-0.2);
        }
        else if (gamepad1.dpad_left) {
            mhw.leftFront.setPower(0.2);
            mhw.leftRear.setPower(-0.2);
            mhw.rightFront.setPower(-0.2);
            mhw.rightRear.setPower(0.2);
        }
        else if (gamepad1.dpad_down) {
            mhw.leftFront.setPower(0.2);
            mhw.leftRear.setPower(0.2);
            mhw.rightFront.setPower(0.2);
            mhw.rightRear.setPower(0.2);
        }
        else if (gamepad1.dpad_up) {
            mhw.leftFront.setPower(-0.2);
            mhw.leftRear.setPower(-0.2);
            mhw.rightFront.setPower(-0.2);
            mhw.rightRear.setPower(-0.2);
        }
        else if (gamepad1.left_trigger > 0.1) {
            mhw.leftFront.setPower(-0.2);
            mhw.leftRear.setPower(-0.2);
            mhw.rightFront.setPower(0.2);
            mhw.rightRear.setPower(0.2);
        }
        else if (gamepad1.right_trigger > 0.1) {
            mhw.leftFront.setPower(0.2);
            mhw.leftRear.setPower(0.2);
            mhw.rightFront.setPower(-0.2);
            mhw.rightRear.setPower(-0.2);
        }
        else{
            mhw.leftFront.setPower(0);
            mhw.rightFront.setPower(0);
            mhw.leftRear.setPower(0);
            mhw.rightRear.setPower(0);
        }

    } // END OF ULTRASTRAFETWO METHOD





    public void traditionalDrive() {
        final double POWER_MULTIPLIER = 0.4;
        final double STRAFE_POWER_DEFAULT = 0.5;
        final double TRIGGER_THRESHOLD = 0.1;

        // Linear motion
        double leftPower = gamepad1.left_stick_y;
        double rightPower = gamepad1.right_stick_y;
        double boostMultiplier = gamepad1.right_bumper ? 1.0 : 0.6;
        mhw.leftFront.setPower(leftPower * POWER_MULTIPLIER * boostMultiplier);
        mhw.leftRear.setPower(leftPower * POWER_MULTIPLIER * boostMultiplier);
        mhw.rightFront.setPower(rightPower * POWER_MULTIPLIER * boostMultiplier);
        mhw.rightRear.setPower(rightPower * POWER_MULTIPLIER * boostMultiplier);

        // Strafing
        if(gamepad1.right_trigger > TRIGGER_THRESHOLD) { // Strafe right
            mhw.leftRear.setPower(STRAFE_POWER_DEFAULT);
            mhw.rightFront.setPower(STRAFE_POWER_DEFAULT);
            mhw.leftFront.setPower(-STRAFE_POWER_DEFAULT);
            mhw.rightRear.setPower(-STRAFE_POWER_DEFAULT);
        } else if(gamepad1.left_trigger > TRIGGER_THRESHOLD) { // Strafe left
            mhw.leftRear.setPower(-STRAFE_POWER_DEFAULT);
            mhw.rightFront.setPower(-STRAFE_POWER_DEFAULT);
            mhw.leftFront.setPower(STRAFE_POWER_DEFAULT);
            mhw.rightRear.setPower(STRAFE_POWER_DEFAULT);
        }
    } // END OF TRADITIONALDRIVE METHOD



    public void motorCali(){
        /* CALCULATE DISTANCES TRAVELLED */
        double d0 = mhw.leftFront.getCurrentPosition() - globalPos[0];
        double d1 = mhw.rightFront.getCurrentPosition() - globalPos[1];
        double d2 = mhw.rightRear.getCurrentPosition() - globalPos[2];
        double d3 = mhw.leftRear.getCurrentPosition() - globalPos[3];

        /* GET MOTOR POWERS */
        double p0 = globalPow[0];
        double p1 = globalPow[1];
        double p2 = globalPow[2];
        double p3 = globalPow[3];

        /* ACCOUNT FOR ZEROS IN DENOMINATORS */
        d0 += d0 == 0 ? 0.01 : 0;
        d1 += d1 == 0 ? 0.01 : 0;
        d2 += d2 == 0 ? 0.01 : 0;
        d3 += d3 == 0 ? 0.01 : 0;
        p0 += p0 == 0 ? 0.01 : 0;
        p1 += p1 == 0 ? 0.01 : 0;
        p2 += p2 == 0 ? 0.01 : 0;
        p3 += p3 == 0 ? 0.01 : 0;

        /* CALCULATE PERFORMANCE RATIOS */
        double r0 = Math.abs(d0 / p0);
        double r1 = Math.abs(d1 / p1);
        double r2 = Math.abs(d2 / p2);
        double r3 = Math.abs(d3 / p3);
        double rAVG = (r0 + r1 + r2 + r3) / 4;

        /* CHANGE MOTOR POWERS */
        mhw.leftFront.setPower(0.999 * p0 * rAVG / r0);
        mhw.rightFront.setPower(0.999 * p1 * rAVG / r1);
        mhw.rightRear.setPower(0.999 * p2 * rAVG / r2);
        mhw.leftRear.setPower(0.999 * p3 * rAVG / r3);

        /* SAVE MOTOR POSITIONS */
        globalPos[0] = mhw.leftFront.getCurrentPosition();
        globalPos[1] = mhw.rightFront.getCurrentPosition();
        globalPos[2] = mhw.rightRear.getCurrentPosition();
        globalPos[3] = mhw.leftRear.getCurrentPosition();
    } // END OF MOTORCALI METHOD
} // End of class