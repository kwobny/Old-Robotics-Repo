/*
 *   Created by Adam Sher on 11/6/18
 *   This class will be used to control the motors, servos, and other hardware devices.
 */


package org.firstinspires.ftc.teamcode.Other.Backend;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.Gyroscope;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotorController;

import org.firstinspires.ftc.robotcontroller.external.samples.SensorMRGyro;
import org.firstinspires.ftc.robotcontroller.external.samples.SensorMROpticalDistance;
import org.firstinspires.ftc.robotcontroller.external.samples.SensorREVColorDistance;

public class MadHardware {

//  DECLARING PHYSICAL DEVICES
    public DcMotor leftFront = null;
    public DcMotor rightFront = null;
    public DcMotor leftRear = null;
    public DcMotor rightRear = null;
    public DcMotor arm = null;
    public CRServo grabberLeft = null;
    public CRServo grabberRight = null;
    public Rev2mDistanceSensor topDist = null;
    public Rev2mDistanceSensor rearDist = null;
    public BNO055IMU imu = null;

//  Declare the hardware map.
    HardwareMap hwMap = null;

//  Enable or disable encoders (rotation sensors) on drive train motors.
    public boolean useEncoders = true;

//  Constructor. Tbh I have no idea what this does.
    public MadHardware() {

    }

//  Initialize all hardware devices & set them to their starting state.
    public void init(HardwareMap hwMap) {
//      Define motors and initialize them.
        leftFront = hwMap.get(DcMotor.class, "lf");
        rightFront = hwMap.get(DcMotor.class, "rf");
        leftRear = hwMap.get(DcMotor.class, "lb");
        rightRear = hwMap.get(DcMotor.class, "rb");
        arm = hwMap.get(DcMotor.class, "arm");
        grabberLeft = hwMap.get(CRServo.class, "lgrab");
        grabberRight = hwMap.get(CRServo.class, "rgrab");
        topDist = hwMap.get(Rev2mDistanceSensor.class, "topdist");
        rearDist = hwMap.get(Rev2mDistanceSensor.class, "reardist");

        imu = hwMap.get(BNO055IMU.class, "imu");


//      Set direction of motor movement based on their physical orientation on the robot.
        leftFront.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setDirection(DcMotor.Direction.REVERSE);
        leftRear.setDirection(DcMotor.Direction.FORWARD);
        rightRear.setDirection(DcMotor.Direction.REVERSE);
        arm.setDirection(DcMotor.Direction.FORWARD);
        grabberLeft.setDirection(CRServo.Direction.FORWARD);
        grabberRight.setDirection(CRServo.Direction.FORWARD);

//      Set all motors to zero power.
        leftFront.setPower(0);
        rightFront.setPower(0);
        leftRear.setPower(0);
        rightRear.setPower(0);
        arm.setPower(0);
        grabberRight.setPower(0);
        grabberLeft.setPower(0);

//      Set all motors to run without encoders.
        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightRear.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

//      If we want to use encoders, enable them on the drive train.
        if(useEncoders)
        {
            leftFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightRear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        // Prepare shoulder motors for encoder use.
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}