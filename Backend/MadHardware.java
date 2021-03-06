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

    //public DcMotor conveyorMotor;
    public DcMotor bottomConveyorPart;
    //public DcMotor leftFlywheel;
    //public DcMotor rightFlywheel;
    
    // INITIALIZE DEVICE OBJECTS
    @Override
    void _subclassInit() {
        // Initialize physical hardware devices
        leftFront = initWheel("leftFront", DcMotorSimple.Direction.REVERSE);
        leftRear = initWheel("leftRear", DcMotorSimple.Direction.REVERSE);
        rightFront = initWheel("rightFront", DcMotorSimple.Direction.FORWARD);
        rightRear = initWheel("rightRear", DcMotorSimple.Direction.FORWARD);

        //conveyorMotor = initLaunchMotor("conveyor motor", DcMotorSimple.Direction.FORWARD);
        bottomConveyorPart = initLaunchMotor("bottom conveyor part", DcMotorSimple.Direction.FORWARD);
        //leftFlywheel = initLaunchMotor("left flywheel", DcMotorSimple.Direction.FORWARD);
        //rightFlywheel = initLaunchMotor("right flywheel", DcMotorSimple.Direction.REVERSE);
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
    
    //positive power launches the rings.
    //0 power stops the launcher wheels.
    //negative power, idk what that does. Spits the ring out from collection side i guess.
    public void setLauncherPower(final double power) {
        bottomConveyorPart.setPower(power);
    }
}