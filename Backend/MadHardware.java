package org.firstinspires.ftc.teamcode.Backend;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;

public class MadHardware extends HardwareInterface {
    
    // HARDWARE DEVICE FIELDS

    public DcMotor leftFront;
    public DcMotor leftRear;
    public DcMotor rightFront;
    public DcMotor rightRear;
    
    // INITIALIZE DEVICE OBJECTS
    @Override
    void _subclassInit() {
        // Wheels.
        leftFront = initWheel("leftFront", DcMotorSimple.Direction.REVERSE);
        leftRear = initWheel("leftRear", DcMotorSimple.Direction.REVERSE);
        rightFront = initWheel("rightFront", DcMotorSimple.Direction.FORWARD);
        rightRear = initWheel("rightRear", DcMotorSimple.Direction.FORWARD);
    }

    // HELPER METHODS FOR PARTIAL PARAMETERS
    
    private DcMotor initWheel(final String name, final DcMotorSimple.Direction direction) {
        return getDcMotor(
            name, true,
            direction,
            DcMotor.RunMode.RUN_USING_ENCODER, DcMotor.ZeroPowerBehavior.BRAKE
        );
    }
}