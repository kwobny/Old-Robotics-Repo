package org.firstinspires.ftc.teamcode.madmachines2020.Backend;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import org.firstinspires.ftc.robotcontroller.external.samples.*;

public class MadHardware {

    // Declare hardware map
    HardwareMap hwMap;

    // Declare physical hardware devices
    /*
    public DcMotor exampleMotor;
    public CRServo exampleServo;
    public SensorREV2mDistance exampleSensor;
    */

    public DcMotor leftFront;
    public DcMotor leftRear;
    public DcMotor rightFront;
    public DcMotor rightRear;

    // Constructor
    public MadHardware() {
        // Currently a default constructor
    }

    /**
     * Initialize all hardware devices & set their default states.
     * @param hwMap
     * @return successMessage
     */
    public String initHardware(HardwareMap hwMap) {

        // Initialize physical hardware devices
        /*
        exampleMotor = hwMap.get(DcMotor.class, "exampleMotor");
        exampleServo = hwMap.get(CRServo.class, "exampleServo");
        exampleSensor = hwMap.get(SensorREV2mDistance.class, "exampleSensor");
        */
        leftFront = hwMap.get(DcMotor.class, "leftFront");
        leftRear = hwMap.get(DcMotor.class, "leftRear");
        rightFront = hwMap.get(DcMotor.class, "rightFront");
        rightRear = hwMap.get(DcMotor.class, "rightRear");

        // Set default states of physical hardware devices
        /*
        exampleMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        exampleMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        exampleMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        exampleServo.setDirection(DcMotorSimple.Direction.FORWARD);
        */

        leftFront.setDirection(DcMotorSimple.Direction.FORWARD);
        leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightFront.setDirection(DcMotorSimple.Direction.REVERSE);
        rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftRear.setDirection(DcMotorSimple.Direction.FORWARD);
        leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightRear.setDirection(DcMotorSimple.Direction.REVERSE);
        rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        stopMovement();

        return "finished hardware initialization";
    }

    /**
     * Set all motors and servos to zero power.
     * @return successMessage
     */
    public String stopMovement() {

        /*
        exampleMotor.setPower(0);
        exampleServo.setPower(0);
        */
        leftFront.setPower(0.0);
        leftRear.setPower(0.0);
        rightFront.setPower(0.0);
        rightRear.setPower(0.0);

        return "finished stopping movement";
    }
}
