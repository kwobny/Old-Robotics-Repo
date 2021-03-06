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
    
    private DcMotor[] ringLaunchingMotors;
    public DcMotor ringIntakeBottom;
    
    // INITIALIZE DEVICE OBJECTS
    @Override
    void _subclassInit() {
        // Wheels.
        leftFront = initWheel("leftFront", DcMotorSimple.Direction.REVERSE);
        leftRear = initWheel("leftRear", DcMotorSimple.Direction.REVERSE);
        rightFront = initWheel("rightFront", DcMotorSimple.Direction.FORWARD);
        rightRear = initWheel("rightRear", DcMotorSimple.Direction.FORWARD);
        
        // Ring launching system.
        ringIntakeBottom = initLaunchMotor("RIB", DcMotorSimple.Direction.FORWARD);
        ringLaunchingMotors = new DcMotor[]{ringIntakeBottom};
    }

    // HELPER METHODS FOR PARTIAL PARAMETERS
    
    private DcMotor initWheel(final String name, final DcMotorSimple.Direction direction) {
        return getDcMotor(
            name, true,
            direction,
            DcMotor.RunMode.RUN_USING_ENCODER, DcMotor.ZeroPowerBehavior.BRAKE
        );
    }
    private DcMotor initLaunchMotor(final String name, final DcMotorSimple.Direction direction) {
        return getDcMotor(
            name, true,
            direction,
            DcMotor.RunMode.RUN_WITHOUT_ENCODER, DcMotor.ZeroPowerBehavior.FLOAT
        );
    }
    
    // USER METHODS
    
    // Positive power launches the rings.
    // 0 power stops the launcher wheels.
    // Negative power spits the ring out from collection side.
    public void setLauncherPower(final double power) {
        for (DcMotor motor : ringLaunchingMotors) {
            motor.setPower(power);
        }
    }
}