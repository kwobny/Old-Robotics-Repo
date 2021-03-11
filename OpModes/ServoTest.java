package org.firstinspires.ftc.teamcode.OpModes;

import android.widget.Button;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Backend.MadHardware;
import org.firstinspires.ftc.teamcode.Backend.Notifier;
import org.firstinspires.ftc.teamcode.LithiumCore.Main;
import org.firstinspires.ftc.teamcode.LithiumCore.Utils.Vector;

@TeleOp(name = "Servo test", group = "TeleOp")
public class ServoTest extends OpMode {

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
        mhw.tempServo.setPower(0.5);

        noto.message("Teleop start complete.");
    }

    private ButtonListener listener = new ButtonListener();
    private double[] powerList = {0.0, 0.25, 0.5, 0.75, 1.0};
    private int index = 0;

    //The main driving code should never directly interface with controllers
    @Override
    public void loop() {
        //noto.cMessage("Main teleop loop running.");
        // loop method code
        //gamepad1 is the main driver, gamepad2 is auxiliary driver.

        robotLib.loopAtBeginning();

        if (listener.set(gamepad1.x)) {
            mhw.tempServo.setPower(powerList[index++]);
        }

        robotLib.loopAtEnd();
    }

}
