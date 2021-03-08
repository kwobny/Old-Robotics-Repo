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
    
    private DcMotor[] ringIntakeMotors;
    private DcMotor[] ringLaunchMotors;
    public DcMotor ringIntakeBottom;
    public DcMotor conveyor;
    public DcMotor leftFlywheel;
    public DcMotor rightFlywheel;
    
    // INITIALIZE DEVICE OBJECTS
    @Override
    void _subclassInit() {
        // Wheels.
        leftFront = initWheel("leftFront", DcMotorSimple.Direction.REVERSE);
        leftRear = initWheel("leftRear", DcMotorSimple.Direction.REVERSE);
        rightFront = initWheel("rightFront", DcMotorSimple.Direction.FORWARD);
        rightRear = initWheel("rightRear", DcMotorSimple.Direction.FORWARD);
        
        // Ring launching system.
        ringIntakeBottom = initLaunchMotor("bottom conveyor part", DcMotorSimple.Direction.FORWARD);
        conveyor = initLaunchMotor("conveyor", DcMotorSimple.Direction.FORWARD);
        leftFlywheel = initLaunchMotor("leftFlywheel", DcMotorSimple.Direction.FORWARD);
        rightFlywheel = initLaunchMotor("rightFlywheel", DcMotorSimple.Direction.FORWARD);

        ringIntakeMotors = new DcMotor[]{ringIntakeBottom, conveyor};
        ringLaunchMotors = new DcMotor[]{leftFlywheel, rightFlywheel};
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

    // Ring intake.
    public void setIntakePower(double power) {
        for (DcMotor motor : ringIntakeMotors) {
            motor.setPower(power);
        }
    }
    // Ring launcher.
    public void setLauncherPower(double power) {
        for (DcMotor motor : ringLaunchMotors) {
            motor.setPower(power);
        }
    }
}