package org.firstinspires.ftc.teamcode.Backend;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;

class DcMotorConfig {
    public String name;
    public DcMotorSimple.Direction direction;
    public DcMotor.RunMode runMode;
    public DcMotor.ZeroPowerBehavior zpb; //zero power behavior
}

public class MadHardware {

    // Constructor
    public MadHardware() {
        // Currently a default constructor
    }

    // Declare hardware map
    HardwareMap hwMap;

    // Declare physical hardware devices
    /*
    public DcMotor exampleMotor;
    public CRServo exampleServo;
    public SensorREV2mDistance exampleSensor;
    */

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

    /**
     * Initialize all hardware devices & set their default states.
     * @param hwMap
     * @return void
     */
    public void initHardware(HardwareMap hwMap) {

    }

    /**
     * Set all motors and servos to zero power.
     * @return void
     */
    public void stopMovement() {

    }

    private DcMotor getDcMotor(final String name, final DcMotorSimple.Direction direction,
                               final DcMotor.RunMode runMode, final DcMotor.ZeroPowerBehavior zpb) {
        final DcMotor motor = hwMap.get(DcMotor.class, name);

        motor.setDirection(direction);
        motor.setMode(runMode);
        motor.setZeroPowerBehavior(zpb);

        return motor;
    }
    private DcMotor getDcMotor(final DcMotorConfig motorConfig) {
        return getDcMotor(
                motorConfig.name, motorConfig.direction,
                motorConfig.runMode, motorConfig.zpb
        );
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
