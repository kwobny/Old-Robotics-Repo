package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Other.Backend.MadHardware;


@Autonomous(name = "Auto: BLUE 1", group = "Autos")   // How opmode is displayed on phones
public class AutoBlue1 extends LinearOpMode
{


    // Initializing the motor-control class.
    public static MadHardware mhw = new MadHardware();
    public static ElapsedTime runtime = new ElapsedTime();

    public static float robotAngle;
    public static String text;


    // This method is run once when the "INIT" button is pressed on the phone.
    @Override
    public void runOpMode()
    {
        {
            // addData calls are used for debugging.
            telemetry.addData("Status", "Initializing...");
            telemetry.update();

            // Initialize hardware & drive train
            mhw.init(hardwareMap);
            mhw.leftFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            mhw.rightFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            mhw.rightRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            mhw.leftRear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            text = "";

            // Initialize Gyro
            BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
            parameters.mode = BNO055IMU.SensorMode.IMU;
            parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
            parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
            parameters.loggingEnabled = false;
            mhw.imu.initialize(parameters);
            while(!mhw.imu.isGyroCalibrated()) {
                telemetry.addData("Status", "Initializing gyro. Do not move...");
                telemetry.update();
            }
            updateRobotAngle();

            telemetry.addData("Status", "Initialized, awaiting autonomous start.");
            telemetry.update();

            waitForStart();

            telemetry.addData("Status", "Autonomous start successful.");
            telemetry.update();
        } // Plenty of code here!

        /** [DRIVING CODE START] */
        doLinearMovementSensor("forwards", 0.03f, "distRear", 5, true);
        doLinearMovementTime("stop", 0, 2);
        doLinearMovementSensor("left", 0.03f, "distTop", 30, false);
        /** [DRIVING CODE STOP] */

        sleep(1000);
    }

    public static void pause(double seconds) {
        doLinearMovementTime("f", 0, seconds);
    }

    /** Rotates the robot.
     *  doingRelativeTurn is true -> rotate by given number of degrees
     *  doingRelativeTurn is false -> rotate to given angle */
    public static void doRotation(boolean doingRelativeTurn, float degrees, float speed) {
        // FIND TARGET ANGLE
        float targetAngle;
        if (doingRelativeTurn) {
            updateRobotAngle();
            targetAngle = getCleanAngle(robotAngle + degrees);
        } else {
            targetAngle = getCleanAngle(degrees);
        }

        // FIND MOST EFFICIENT PATH & DO INCREMENTAL LINEAR INTERPOLATION
        float rotationAngle;
        updateRobotAngle();
        if (Math.abs(targetAngle - robotAngle) <= 180)
            rotationAngle = targetAngle - robotAngle;
        else
            rotationAngle = -360 - (targetAngle - robotAngle);
        float remainingRotation = rotationAngle;
        while (Math.abs(targetAngle - robotAngle) > 1.5 && remainingRotation * rotationAngle >= 0) {
            updateRobotAngle();
            if (Math.abs(targetAngle - robotAngle) <= 180)
                remainingRotation = targetAngle - robotAngle;
            else
                remainingRotation = 360 - (targetAngle - robotAngle);
            float power = speed * (remainingRotation / Math.abs(rotationAngle)) * Math.abs(remainingRotation / rotationAngle);
            mhw.leftFront.setPower(-power);
            mhw.rightFront.setPower(power);
            mhw.rightRear.setPower(power);
            mhw.leftRear.setPower(-power);
            updateRobotAngle();
            text += " " + remainingRotation;
        }
        mhw.leftFront.setPower(0);
        mhw.rightFront.setPower(0);
        mhw.rightRear.setPower(0);
        mhw.leftRear.setPower(0);
    }

    public static void doSimpleRotation(float degrees, float speed) {
        degrees = getCleanAngle(degrees);
        updateRobotAngle();
        float rotationAngle = degrees - robotAngle;
        while(Math.abs(degrees - robotAngle) > 10) {
            float remainingRotation = degrees - robotAngle;
            float power = speed * (remainingRotation / Math.abs(remainingRotation));
            mhw.leftFront.setPower(-power);
            mhw.rightFront.setPower(power);
            mhw.rightRear.setPower(power);
            mhw.leftRear.setPower(-power);
            updateRobotAngle();
            text += "  " + remainingRotation;
        }
        mhw.leftFront.setPower(0);
        mhw.rightFront.setPower(0);
        mhw.rightRear.setPower(0);
        mhw.leftRear.setPower(0);
    }

    public void doTurn(boolean turnClockwise, float finalPos, float speed) {
        finalPos = getCleanAngle(finalPos);
        updateRobotAngle();
        if (turnClockwise) {
            mhw.leftFront.setPower(speed);
            mhw.rightFront.setPower(-speed);
            mhw.rightRear.setPower(-speed);
            mhw.leftRear.setPower(speed);
            while (Math.abs(finalPos - robotAngle) > 10) {
                updateRobotAngle();
            }
        } else {
            mhw.leftFront.setPower(-speed);
            mhw.rightFront.setPower(speed);
            mhw.rightRear.setPower(speed);
            mhw.leftRear.setPower(-speed);
            while (Math.abs(finalPos - robotAngle) > 10) {
                updateRobotAngle();
            }
        }
        telemetry.addData("Done with first part: ", robotAngle);
        telemetry.update();
        float lastSpeed = 0.00001f;
        while (Math.abs(finalPos - robotAngle) > 1) {
            float driftSpeed = speed * (float)Math.min(Math.pow((0.09 * (robotAngle - finalPos) + 0.1), 2),10) * (robotAngle - finalPos > 0 ? 1 : -1);
            if (lastSpeed == 0 || Math.abs(driftSpeed / lastSpeed) > 1.01 || Math.abs(driftSpeed / lastSpeed) < 0.99) {
                mhw.leftFront.setPower(driftSpeed);
                mhw.rightFront.setPower(-driftSpeed);
                mhw.rightRear.setPower(-driftSpeed);
                mhw.leftRear.setPower(driftSpeed);
                lastSpeed = driftSpeed;
            }
            updateRobotAngle();
        }
        mhw.leftFront.setPower(0);
        mhw.rightFront.setPower(0);
        mhw.rightRear.setPower(0);
        mhw.leftRear.setPower(0);
        updateRobotAngle();
        telemetry.addData("Done with second part: ", robotAngle);
        telemetry.update();
    }

    public static void executeLinearMovement(String direction, float speed) {
        if (direction.equalsIgnoreCase("f") || direction.equalsIgnoreCase("forwards")) {
            mhw.leftFront.setPower(speed);
            mhw.rightFront.setPower(speed);
            mhw.rightRear.setPower(speed);
            mhw.leftRear.setPower(speed);
        } else if (direction.equalsIgnoreCase("b") || direction.equalsIgnoreCase("backwards")) {
            mhw.leftFront.setPower(-speed);
            mhw.rightFront.setPower(-speed);
            mhw.rightRear.setPower(-speed);
            mhw.leftRear.setPower(-speed);
        } else if (direction.equalsIgnoreCase("l") || direction.equalsIgnoreCase("left")) {
            mhw.leftFront.setPower(-speed);
            mhw.rightFront.setPower(speed);
            mhw.rightRear.setPower(-speed);
            mhw.leftRear.setPower(speed);
        } else {
            mhw.leftFront.setPower(speed);
            mhw.rightFront.setPower(-speed);
            mhw.rightRear.setPower(speed);
            mhw.leftRear.setPower(-speed);
        }
    }

    public static void doLinearMovementTime(String direction, float speed, double seconds) {
        ElapsedTime timer = new ElapsedTime();
        runtime.reset();
        while (runtime.time() < seconds) {
            executeLinearMovement(direction, speed);
        }
        mhw.leftFront.setPower(0);
        mhw.rightFront.setPower(0);
        mhw.rightRear.setPower(0);
        mhw.leftRear.setPower(0);
    }

    // sensorDirection = true -> stops when greater than or equal to sensorCondition
    public static void doLinearMovementSensor(String direction, float speed, String sensor, double sensorCondition, boolean sensorDirection) {
        if (sensor.equalsIgnoreCase("distTop")) {
            if (sensorDirection) {
                while (mhw.topDist.getDistance(DistanceUnit.CM) < sensorCondition) {
                    executeLinearMovement(direction, speed);
                }
            } else {
                while (mhw.topDist.getDistance(DistanceUnit.CM) >= sensorCondition) {
                    executeLinearMovement(direction, speed);
                }
            }
        } else if (sensor.equalsIgnoreCase("distRear")) {
            if (sensorDirection) {
                while (mhw.rearDist.getDistance(DistanceUnit.CM) < sensorCondition) {
                    executeLinearMovement(direction, speed);
                }
            } else {
                while (mhw.rearDist.getDistance(DistanceUnit.CM) >= sensorCondition) {
                    executeLinearMovement(direction, speed);
                }
            }
        }
        mhw.leftFront.setPower(0);
        mhw.rightFront.setPower(0);
        mhw.rightRear.setPower(0);
        mhw.leftRear.setPower(0);
    }

    /** Updates the global variable robotAngle to the robot's current
     *  angular position in the z-axis, rotated appropriately. */
    public static void updateRobotAngle() {
        robotAngle = transformAngle(mhw.imu.getAngularOrientation(AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES).thirdAngle);
    }

    /** Returns an appropriately transformed angle to be used in the
     *  remainder of the code. Angles are transformed to the correct
     *  polar coordinate grid and then rotated as necessary to be
     *  inside [0,360) degrees. */
    public static float transformAngle(float angle) {
        if (angle >= -90)
            angle += 90;
        else
            angle += 450;

        if (angle >= 360)
            angle -= 360;
        else if (angle < 0)
            angle += 360;

        return angle;
    }

    /** Returns an appropriately rotated angle to be inside of
     *  [0,360] degrees to use in the remainder of the code. */
    public static float getCleanAngle(float angle) {
        if (angle >= 360)
            angle -= 360;
        else if (angle < 0)
            angle += 360;
        return angle;
    }
} // End of class