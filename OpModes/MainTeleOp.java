package org.firstinspires.ftc.teamcode.OpModes;

import java.lang.Override;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Backend.MadHardware;
import org.firstinspires.ftc.teamcode.Backend.Notifier;
import org.firstinspires.ftc.teamcode.LithiumCore.*;
import org.firstinspires.ftc.teamcode.LithiumCore.SharedState.*;
import org.firstinspires.ftc.teamcode.LithiumCore.Utils.BooleanConsumer;
import org.firstinspires.ftc.teamcode.LithiumCore.Utils.Vector;

@TeleOp(name = "Yeongjin's Teleop \"Yayyyy!\"", group = "TeleOp")
public class MainTeleOp extends OpMode {

    MadHardware mhw = new MadHardware();
    Notifier noto = new Notifier(telemetry);
    //ElapsedTime runtime = new ElapsedTime();

    final Main robotLib = new Main(mhw, new MadMachinesConstants());

    @Override
    public void init() {
        noto.cMessage("Initialization starting.");
        mhw.initHardware(hardwareMap);
        noto.message("Initialization complete.");
    }

    @Override
    public void start() {
        noto.message("Teleop starting.");

        //start stuff
        robotLib.startTeleOp();

        noto.message("Teleop start complete.");
    }

    private boolean usingAdvancedDrive = false;
    private ToggleListener switchDrivingListener = new ToggleListener(new BooleanConsumer() {
        @Override
        public void accept(final boolean value) {
            if (!value) { // Check if switch button is not pressed (switched from pressed to not pressed).
                usingAdvancedDrive = !usingAdvancedDrive; // Switch driving modes.
                if (!usingAdvancedDrive) {
                    // Stop rotational translate if switching to normal driving mode.
                    robotLib.move.clearRT();
                }
            }
        }
    });

    //The main driving code should never directly interface with controllers
    @Override
    public void loop() {
        //noto.cMessage("Main teleop loop running.");
        // loop method code
        //gamepad1 is the main driver, gamepad2 is auxiliary driver.

        robotLib.loopAtBeginning();

        // The "b" button is the "slow down" button.
        // If it is pressed, the translation is slowed down by a factor of
        // 0.5.
        if (gamepad1.b) {
            robotLib.move.translateBuffer.set(0.25);
        }
        else {
            robotLib.move.translateBuffer.set(1.0);
        }

        // Check to see if you should switch the driving mode.
        // Switch driving mode when y button is toggled on gamepad 1
        switchDrivingListener.set(gamepad1.y);

        // For now, right trigger makes the rings go up the ramp,
        // left trigger makes the rings go down the ramp.
        // right trigger (intake) has higher precedence than left trigger (spit out).

        if (gamepad1.right_trigger > 0.1) {
            mhw.setLauncherPower(0.5);
        }
        else if (gamepad1.left_trigger > 0.1) {
            mhw.setLauncherPower(-0.5);
        }
        else {
            mhw.setLauncherPower(0.0);
        }

        if (usingAdvancedDrive) {
            robotLib.move.translateRel(getTranslateDirection(gamepad1));
            handleRotate(gamepad2);
        }
        else {
            robotLib.move.translate(getTranslateDirection(gamepad1));
            handleRotate(gamepad1);
        }
        robotLib.move.syncMotors();

        robotLib.loopAtEnd();
    }

    //CONTROLLER INTERFACE METHODS
    //methods to interface with controller
    //get user's wishes based off of controller
    //The main driving code should never directly interface with controllers

    //returns the direction the user wants the robot to translate.
    //positive x -> right
    //positive y -> forward
    private double getTranslateX(final Gamepad gamepad) {
        return gamepad.left_stick_x;
    }
    private double getTranslateY(final Gamepad gamepad) {
        return -1 * gamepad.left_stick_y;
    }
    private Vector getTranslateDirection(final Gamepad gamepad) {
        return Vector.getVector(getTranslateX(gamepad), getTranslateY(gamepad));
    }

    //UTILITY FUNCTIONS

    //Rotate the robot based on the gamepad control that is given.
    //Handles rotating the robot
    //Invert parameter specifies whether to rotate counterclockwise when normally in clockwise rotation, etc.
    private void handleRotate(final Gamepad gamepad) {
        /*if (getRotateClockwise(gamepad)) {
            robotLib.move.rotate(invert ? -1.0 : 1.0, false);
        }
        else if (getRotateCounterclockwise(gamepad)) {
            robotLib.move.rotate(invert ? 1.0 : -1.0, false);
        }
        //no rotation
        else {
            robotLib.move.rotate(0.0, false);
        }*/
        robotLib.move.rotate(gamepad.right_stick_x, false);
    }

}
