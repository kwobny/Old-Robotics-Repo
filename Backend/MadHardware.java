package org.firstinspires.ftc.teamcode.Backend;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import org.firstinspires.ftc.robotcontroller.external.samples.*;

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
     * @return successMessage
     */
    public String initHardware(HardwareMap hwMap) {
        this.hwMap = hwMap;

        // Initialize physical hardware devices
        leftFront = initWheel("leftFront", DcMotorSimple.Direction.REVERSE);
        leftRear = initWheel("leftRear", DcMotorSimple.Direction.REVERSE);
        rightFront = initWheel("rightFront", DcMotorSimple.Direction.FORWARD);
        rightRear = initWheel("rightRear", DcMotorSimple.Direction.FORWARD);

        //conveyorMotor = initLaunchMotor("conveyor motor", DcMotorSimple.Direction.FORWARD);
        bottomConveyorPart = initLaunchMotor("bottom conveyor part", DcMotorSimple.Direction.FORWARD);
        //leftFlywheel = initLaunchMotor("left flywheel", DcMotorSimple.Direction.FORWARD);
        //rightFlywheel = initLaunchMotor("right flywheel", DcMotorSimple.Direction.REVERSE);

        dcMotors = new DcMotor[]{
                leftFront, leftRear, rightFront, rightRear,
                bottomConveyorPart
        };

        /*
        exampleMotor = hwMap.get(DcMotor.class, "exampleMotor");
        exampleServo = hwMap.get(CRServo.class, "exampleServo");
        exampleSensor = hwMap.get(SensorREV2mDistance.class, "exampleSensor");
        */

        // Set default states of physical hardware devices
        /*
        exampleMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        exampleMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        exampleMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        exampleServo.setDirection(DcMotorSimple.Direction.FORWARD);
        */

        stopMovement();

        return "finished hardware initialization";
    }

    /**
     * Set all motors and servos to zero power.
     * @return successMessage
     */
    public String stopMovement() {

        for (DcMotor i : dcMotors) {
            i.setPower(0.0);
        }
        /*
        exampleMotor.setPower(0);
        exampleServo.setPower(0);
        */

        return "finished stopping movement";
    }

    private DcMotor initializeMotor(final String name, final DcMotorSimple.Direction direction,
                                    final DcMotor.RunMode runMode, final DcMotor.ZeroPowerBehavior zpb) {
        final DcMotor motor = hwMap.get(DcMotor.class, name);
        motor.setDirection(direction);
        motor.setMode(runMode);
        motor.setZeroPowerBehavior(zpb);

        return motor;
    }
    private DcMotor initializeMotor(final DcMotorConfig motorConfig) {
        return initializeMotor(
                motorConfig.name, motorConfig.direction,
                motorConfig.runMode, motorConfig.zpb
        );
    }

    //these method names start with init, not initialize.
    private DcMotor initWheel(final String name, final DcMotorSimple.Direction direction) {
        return initializeMotor(
                name, direction,
                DcMotor.RunMode.RUN_USING_ENCODER, DcMotor.ZeroPowerBehavior.BRAKE
        );
    }
    private DcMotor initLaunchMotor(final String name, final DcMotorSimple.Direction direction) {
        return initializeMotor(
                name, direction,
                DcMotor.RunMode.RUN_WITHOUT_ENCODER, DcMotor.ZeroPowerBehavior.FLOAT
        );
    }
}
