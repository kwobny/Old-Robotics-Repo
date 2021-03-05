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

    // Steps to declaring a hardware device:
    // 1. Declare the field for device to be accessed.
    // 2. Initialize the device object and assign it to the field.
    // 3. If needed, reset the hardware device associated with the object.
    // This step is often done together with step 2 through the autoReset parameter.

    // Declare hardware map
    HardwareMap hwMap;
    
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

    public void initHardware(HardwareMap hwMap) {
        
    }

    private DcMotor getDcMotor (String name, boolean autoReset,
                                DcMotorSimple.Direction direction,
                                DcMotor.RunMode runMode,
                                DcMotor.ZeroPowerBehavior zpb)
    {
        DcMotor motor = hwMap.get(DcMotor.class, name);

        motor.setDirection(direction);
        motor.setMode(runMode);
        motor.setZeroPowerBehavior(zpb);

        if (autoReset) resetDcMotorSimple(motor);

        return motor;
    }
    private void resetDcMotorSimple(DcMotorSimple motor, double power) {
        motor.setPower(power);
    }
    private void resetDcMotorSimple(DcMotorSimple motor) {
        resetDcMotorSimple(motor, 0.0);
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


// Declare physical hardware devices
/*
public DcMotor exampleMotor;
public CRServo exampleServo;
public SensorREV2mDistance exampleSensor;
*/
