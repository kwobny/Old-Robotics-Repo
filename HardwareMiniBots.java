/*
 *   Created by Adam Sher on 2-26-19
 *   This class will be used to control the drive train of our mini-bots.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class HardwareMiniBots {

//  DECLARING PHYSICAL DEVICES

//  Declare Motors
    public DcMotor leftFront = null;
    public DcMotor leftBack = null;
    public DcMotor rightFront = null;
    public DcMotor rightBack = null;
    public DcMotor leftElbow = null;
    public DcMotor leftShoulder = null;
    public DcMotor rightElbow = null;
    public DcMotor rightShoulder = null;



    //  Declare the hardware map.
    HardwareMap hwMap = null;

//  Constructor. Tbh I have no idea what this does.
    public HardwareMiniBots() {

    }



//  Initialize all hardware devices & set them to their starting state.
    public void init(HardwareMap hwMap) {

//      Initialize Motors
        leftFront = hwMap.get(DcMotor.class, "lf");
        leftFront.setDirection(DcMotor.Direction.REVERSE);
        leftFront.setPower(0);
        leftFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftBack = hwMap.get(DcMotor.class, "lb");
        leftBack.setDirection(DcMotor.Direction.REVERSE);
        leftBack.setPower(0);
        leftBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightFront = hwMap.get(DcMotor.class, "rf");
        rightFront.setDirection(DcMotor.Direction.FORWARD);
        rightFront.setPower(0);
        rightFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightBack = hwMap.get(DcMotor.class, "rb");
        rightBack.setDirection(DcMotor.Direction.FORWARD);
        rightBack.setPower(0);
        rightBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);



        leftElbow = hwMap.get(DcMotor.class, "rb");
        leftElbow.setDirection(DcMotor.Direction.REVERSE);
        leftElbow.setPower(0);
        leftElbow.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        leftShoulder = hwMap.get(DcMotor.class, "rb");
        leftShoulder.setDirection(DcMotor.Direction.REVERSE);
        leftShoulder.setPower(0);
        leftShoulder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightElbow = hwMap.get(DcMotor.class, "rb");
        rightElbow.setDirection(DcMotor.Direction.FORWARD);
        rightElbow.setPower(0);
        rightElbow.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        rightShoulder = hwMap.get(DcMotor.class, "rb");
        rightShoulder.setDirection(DcMotor.Direction.FORWARD);
        rightShoulder.setPower(0);
        rightShoulder.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}