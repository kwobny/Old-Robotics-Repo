package org.firstinspires.ftc.teamcode.OldCode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;
import java.lang.Override;


@Disabled
@Autonomous(name = "AutoBlue", group = "Autos")   // How opmode is displayed on phones
public class BasicAutoBlue extends LinearOpMode
{


    // Initializing the motor-control class.
    MadHardware mhw = new MadHardware();
    public ElapsedTime runtime = new ElapsedTime();

    public boolean yeet = false;


    // This method is run once when the "INIT" button is pressed on the phone.
    @Override
    public void runOpMode()
    {
        // addData calls are used for debugging.
        telemetry.addData("Status", "Initializing...");
        telemetry.update();

        mhw.init(hardwareMap);
        mhw.leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mhw.rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mhw.rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mhw.leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        telemetry.addData("Status", "Initialized, awaiting autonomous start.");
        telemetry.update();

        waitForStart();

        telemetry.addData("Status", "Autonomous start successful.");
        telemetry.update();


        // [DRIVING CODE START]
        while(opModeIsActive() && mhw.topDist.getDistance(DistanceUnit.CM) > 30) { // Go forward until under bridge
            mhw.leftRear.setPower(-0.05);
            mhw.rightFront.setPower(-0.05);
            mhw.leftFront.setPower(-0.05);
            mhw.rightRear.setPower(-0.05);
        }
        mhw.leftRear.setPower(0);
        mhw.rightFront.setPower(0);
        mhw.leftFront.setPower(0);
        mhw.rightRear.setPower(0);
        // [DRIVING CODE STOP]


        telemetry.addData("Status", "Autonomous complete.");
        telemetry.update();

        telemetry.addData("Status", "Autonomous complete, awaiting autonomous stop.");
        telemetry.update();

        sleep(1000);
    }


} // End of class