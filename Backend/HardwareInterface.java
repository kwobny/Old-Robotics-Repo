package org.firstinspires.ftc.teamcode.Backend;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;

// Base class for mad hardware. Has default access.

abstract class HardwareInterface {

    // Steps to declaring a hardware device:
    // 1. Declare the field for device to be accessed.
    // 2. Initialize the device object and assign it to the field.
    // 3. If needed, reset the hardware device associated with the object.
    // This step is often done together with step 2 through the autoReset parameter.

    // Properties are declared with default access so that they can only be accessed by mad hardware.

    abstract void _subclassInit();

    // Declare hardware map
    HardwareMap hwMap;

    public void initHardware(HardwareMap hwMap) {
        this.hwMap = hwMap;
        _subclassInit();
    }

    DcMotor getDcMotor (
        String name, boolean autoReset,
        DcMotorSimple.Direction direction,
        DcMotor.RunMode runMode,
        DcMotor.ZeroPowerBehavior zpb
    ) {
        DcMotor motor = hwMap.get(DcMotor.class, name);

        motor.setDirection(direction);
        motor.setMode(runMode);
        motor.setZeroPowerBehavior(zpb);

        if (autoReset) resetDcMotorSimple(motor);

        return motor;
    }
    void resetDcMotorSimple(DcMotorSimple motor, double power) {
        motor.setPower(power);
    }
    void resetDcMotorSimple(DcMotorSimple motor) {
        resetDcMotorSimple(motor, 0.0);
    }
}

/*
Examples of new way:

Declaring fields
public DcMotor exampleMotor;
public CRServo exampleServo;
public SensorREV2mDistance exampleSensor;

Use provided get methods to initialize, configure, and maybe reset device objects

Use provided reset methods to explicitely reset devices
*/

/*
Examples from old way of doing madhardware:

Declaring fields
public DcMotor exampleMotor;
public CRServo exampleServo;
public SensorREV2mDistance exampleSensor;

Initialize devices
exampleMotor = hwMap.get(DcMotor.class, "exampleMotor");
exampleServo = hwMap.get(CRServo.class, "exampleServo");
exampleSensor = hwMap.get(SensorREV2mDistance.class, "exampleSensor");

Configure devices
exampleMotor.setDirection(DcMotorSimple.Direction.FORWARD);
exampleMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
exampleMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
exampleServo.setDirection(DcMotorSimple.Direction.FORWARD);
*/