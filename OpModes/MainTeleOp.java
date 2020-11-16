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
import org.firstinspires.ftc.teamcode.LithiumCore.Utils.Vector;

@TeleOp(name = "Yeongjin's Teleop \"Yayyyy!\"", group = "TeleOp")
public class MainTeleOp extends OpMode {

    MadHardware mhw = new MadHardware();
    Notifier noto = new Notifier(telemetry);
    //ElapsedTime runtime = new ElapsedTime();

    final double maxDrivePower = 1.0;
    final double maxMotorPower = 1.0;

    final Main robotLib = new Main(mhw, new MadMachinesConstants());

    @Override
    public void init() {
        noto.cMessage("Initialization starting.");
        mhw.initHardware(hardwareMap);
        if (maxDrivePower > maxMotorPower) {
            noto.message("ERROR: MAX DRIVE POWER EXCEEDS MAX MOTOR POWER");
        }
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
    private boolean switchButtonPressed = false;

    //The main driving code should never directly interface with controllers
    @Override
    public void loop() {
        //noto.cMessage("Main teleop loop running.");
        // loop method code

        robotLib.loopAtBeginning();

        if (usingAdvancedDrive) {
            robotLib.move.translateRel(getTranslateDirection(gamepad1));
            handleRotate(gamepad2, false);
        }
        else {
            robotLib.move.translate(getTranslateDirection(gamepad1));
            handleRotate(gamepad1, getTranslateY(gamepad1) < 0.0);
        }
        robotLib.move.syncMotors();

        //Check to see if you should switch the driving mode
        //switch driving mode button is y button on gamepad 1
        if (switchButtonPressed != gamepad1.y) { //see if button has been pressed or released
            switchButtonPressed = !switchButtonPressed; //set switch button pressed to correct state
            if (!switchButtonPressed) { //check if switch button is not pressed (switched from pressed to not pressed)
                //switch driving modes
                usingAdvancedDrive = !usingAdvancedDrive;
            }
        }

        robotLib.loopAtEnd();
    }

    //CONTROLLER INTERFACE METHODS
    //methods to interface with controller
    //get user's wishes based off of controller
    //The main driving code should never directly interface with controllers

    //gets whether or not the user wants the robot to rotate clockwise,
    //based on the gamepad controller provided.
    private boolean getRotateClockwise(final Gamepad gamepad) {
        return gamepad.right_trigger > 0.01;
    }
    //same but for counterclockwise
    private boolean getRotateCounterclockwise(final Gamepad gamepad) {
        return gamepad.left_trigger > 0.01;
    }
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
    private void handleRotate(final Gamepad gamepad, final boolean invert) {
        if (getRotateClockwise(gamepad)) {
            robotLib.move.rotate(invert ? -1.0 : 1.0, false);
        }
        else if (getRotateCounterclockwise(gamepad)) {
            robotLib.move.rotate(invert ? 1.0 : -1.0, false);
        }
        //no rotation
        else {
            robotLib.move.rotate(0.0, false);
        }
    }

}
