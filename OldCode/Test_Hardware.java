/*
 *   Created by Adam Sher on 11/6/18
 *   This class will be used to control the motors, servos, and other hardware devices.
 */


package org.firstinspires.ftc.teamcode.OldCode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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

public class Test_Hardware {

//  DECLARING PHYSICAL DEVICES

    //  Motors
    public DcMotor leftDrive = null;
    public DcMotor rightDrive = null;


    //  Declare the hardware map.
    HardwareMap hwMap = null;

    //  Enable or disable encoders (rotation sensors) on drive train motors.




    //  Constructor. Tbh I have no idea what this does.
    public Test_Hardware() {

    }

    //  Initialize all hardware devices & set them to their starting state.
    public void init(HardwareMap hwMap) {


//      Define motors and initialize them.
        leftDrive = hwMap.get(DcMotor.class, "left_drive");
        rightDrive = hwMap.get(DcMotor.class, "right_drive");

//      Set direction of motor movement based on their physical orientation on the robot.
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);

//      Set all motors to zero power.
        leftDrive.setPower(0);
        rightDrive.setPower(0);

//      Set all motors to run without encoders.
        leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

//      If we want to use encoders, enable them on the drive train.
            leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


/*
//      Initialize Sensors
        colorSensor = hwMap.colorSensor.get("colorSensor");
//      oxygenDepletionSensor = hwMap.opticalDistanceSensor.get("ods_sensor");
//      gyro = hwMap.gyroSensor.get("gyro");
*/
    }
}