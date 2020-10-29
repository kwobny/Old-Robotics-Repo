package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.*;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.SCS_Package.*;

@Autonomous(name = "hi kian", group = "Autos")   // How opmode is displayed on phones
public class Auto extends LinearOpMode
{


    // Initializing the motor-control class.
    public static MadHardware mhw = new MadHardware();
    public static ElapsedTime runtime = new ElapsedTime();

    // This method is run once when the "INIT" button is pressed on the phone.
    @Override
    public void runOpMode()
    {

        {
          mhw.flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        }
        //This code is for when you are starting on the left line
        final Main robotLib = new Main(mhw);

        //robotLib.rps.() initialize robot's position state

        //final double[] targetPos = {blah, blah};

        //robotLib.move.(linTransRel)(robotLib.move.getPowerTo(1.0, targetPos)) //do some operations to find power towards a specific coordinate.
        robotLib.move.rotate(some right power, true);
        //blah blah blah initialize an s curve for the translating
        final SCSOpUnit transOperation = new SCSOpUnit(robotLib.rps.distanceinputblah, robotLib.move.linTransBuffer, sCurve);
        //repeat same stuff but for rotate s curve
        robotLib.scs.addOperation(transOperation);
        robotLib.scs.addOperation(rotateOperation);
        robotLib.move.syncMotors();

        robotLib.wait.simpleWait(new robotLib.rps.distanceWait(blah blah));
        
        robotLib.scs.removeOperation(transOperation);
        robotLib.scs.removeOperation(rotateOperation);

        //Now you are at position 2. Do a bunch of stuff.

        //position 3:
        robotLib.move.linTransRel();
    }
} // End of class