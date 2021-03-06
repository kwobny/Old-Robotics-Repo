package org.firstinspires.ftc.teamcode.Backend;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;

public class MadHardware extends HardwareInterface {
    
    // HARDWARE DEVICE FIELDS
    
    private DcMotor[] dcMotors;

    public DcMotor leftFront;
    public DcMotor leftRear;
    public DcMotor rightFront;
    public DcMotor rightRear;

    //public DcMotor conveyorMotor;
    public DcMotor bottomConveyorPart;
    //public DcMotor leftFlywheel;
    //public DcMotor rightFlywheel;

    //positive power launches the rings.
    //0 power stops the launcher wheels.
    //negative power, idk what that does. Spits the ring out from collection side i guess.
    public void setLauncherPower(final double power) {
        bottomConveyorPart.setPower(power);
    }

    @Override
    void _subclassInit() {
        //
    }

    //these method names start with init, not initialize.
    private DcMotor initWheel(final String name, final DcMotorSimple.Direction direction) {
        return getDcMotor(
                name, direction,
                DcMotor.RunMode.RUN_USING_ENCODER, DcMotor.ZeroPowerBehavior.BRAKE
        );
    }
    private DcMotor initLaunchMotor(final String name, final DcMotorSimple.Direction direction) {
        return getDcMotor(
                name, direction,
                DcMotor.RunMode.RUN_WITHOUT_ENCODER, DcMotor.ZeroPowerBehavior.FLOAT
        );
    }
}