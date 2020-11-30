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

@TeleOp(name = "Test Controller Input", group = "TeleOp")
public class ControllerTestTeleOp extends OpMode {

    MadHardware mhw = new MadHardware();
    Notifier noto = new Notifier(telemetry);
    //ElapsedTime runtime = new ElapsedTime();

    final double maxDrivePower = 1.0;
    final double maxMotorPower = 1.0;

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

        noto.message("Teleop start complete.");
    }

    private boolean usingAdvancedDrive = false;
    private boolean switchButtonPressed = false;

    //The main driving code should never directly interface with controllers
    @Override
    public void loop() {
        //noto.cMessage("Main teleop loop running.");
        // loop method code

        noto.cMessage(String.format("left stick x: %d\nleft stick y: %d", gamepad1.left_stick_x, gamepad1.left_stick_y));
    }

}
