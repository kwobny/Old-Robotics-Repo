package org.firstinspires.ftc.teamcode.madmachines2020.OpModes;

import java.lang.Override;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.madmachines2020.Backend.MadHardware;
import org.firstinspires.ftc.teamcode.madmachines2020.Backend.Notifier;
import org.firstinspires.ftc.teamcode.madmachines2020.LithiumCore.*;
import org.firstinspires.ftc.teamcode.madmachines2020.LithiumCore.SharedState.*;

@TeleOp(name = "Yeongjin's Teleop \"Yayyyy!\"", group = "TeleOp")
public class MainTeleOp extends OpMode {

    MadHardware mhw = new MadHardware();
    Notifier noto = new Notifier(telemetry);
    ElapsedTime runtime = new ElapsedTime();

    final double maxDrivePower = 1.0;
    final double maxMotorPower = 1.0;

    final Main robotLib;
    {
        robotLib = new Main(mhw, new MadMachinesConstants());
    }

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
        robotLib.start();

        noto.message("Teleop start complete.");
    }

    private boolean usingAdvancedDrive = false;
    private boolean switchButtonPressed = false;
    @Override
    public void loop() {
        //noto.cMessage("Main teleop loop running.");
        // loop method code

        robotLib.loop();

        if (usingAdvancedDrive) {
            robotLib.move.translateRel(gamepad1.left_stick_x, gamepad1.left_stick_y);
            _setRotate(gamepad2, false);
        }
        else {
            robotLib.move.translate(gamepad1.left_stick_x, gamepad1.left_stick_y);
            _setRotate(gamepad1, gamepad1.left_stick_y > 0.0);
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
    }

    private void _setRotate(final Gamepad gamepad, final boolean invert) {
        if (gamepad.left_trigger > 0.01) {
            robotLib.move.rotate(invert ? -1.0 : 1.0, false);
        }
        else if (gamepad.right_trigger > 0.01) {
            robotLib.move.rotate(invert ? 1.0 : -1.0, false);
        }
        else {
            robotLib.move.rotate(0.0, false);
        }
    }
}
