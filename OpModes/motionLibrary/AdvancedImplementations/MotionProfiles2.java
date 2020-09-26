package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.Wait_Package.*;
import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.SCS_Package.*;
import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.MathFunctions.*;

public class MotionProfiles2 {

  public Main main;

  public MotionProfiles2(final Main main) {
    this.main = main;
  }

  public class SubSCurve extends SeqOpUnit {

    public SubSCurve(final InputSource input, final OutputSink output, final Callback opCallback, final double ...curveArgs) {
      this(input, output, curveArgs);
      this.callback = opCallback;
    }
    //All curve arguments have to be as accurate as possible, or the whole curve falls apart.
    public SubSCurve(final InputSource input, final OutputSink output, final double ...curveArgs) {
      super(main.wait, main.scs, input, output);

      //the argument list is jerk, peak acceleration, time spent on jerk, time spent on const accel, change in velocity in jerk, change in velocity on const accel, initial velocity.
      //0: jerk
      //1: peak/most magnitude accel
      //2: time on jerk
      //3: time on const accel
      //4: change in velocity on jerk
      //5: change in velocity on accel
      //6: initial velocity

      //if the time spent on const accel is 0.0, then it is a triangular acceleration profile.
      //change in velocity on accel has to also be 0.0 if time spent on it is 0.0.
      //time on jerk is the time spent on each individual jerk curve. 2 times this number gives the total time spent on jerk.
      //change in velocity on jerk is the change in velocity for each jerk curve. Multiply this by 2 to get the total change in velocity on both jerk curves.

      //checking if argument length is valid
      if (curveArgs.length != 7) {
        throw new RuntimeException("Incorrect number of arguments given.");
      }

      MathFunction constJerk1 = new CommonOps.ConstJerk(curveArgs[0], 0.0, curveArgs[6]);
      MathFunction constJerk2 = new CommonOps.ConstJerk(-curveArgs[0], curveArgs[1], curveArgs[6] + curveArgs[4] + curveArgs[5]);

      final Section sect1 = new Section(curveArgs[2], constJerk1);
      final Section sect3 = new Section(2 * curveArgs[2] + curveArgs[3], constJerk2);

      if (curveArgs[3] == 0.0) {
        sections = new Section[]{sect1, sect3};
      }
      else {
        MathFunction constAccel = new CommonOps.ConstAccel(curveArgs[1], curveArgs[6] + curveArgs[4]);
        final Section sect2 = new Section(curveArgs[2] + curveArgs[3], constAccel);
        sections = new Section[]{sect1, sect2, sect3};
      }
    }

  }

  //the using ref as final output is self explanatory. If false (in most cases), the reference will be counted as the initial output. If true, the reference will be counted as the final output.
  //the reference output is required for obvious reasons
  public SubSCurve getSubSCurve(final InputSource input, final OutputSink output, final Double maxJerk, final Double maxAcceleration, final Double changeInTime, final Double changeInOutput, double refOutput, final boolean using_ref_as_final_output) {
    //0: jerk
    //1: peak/most magnitude accel
    //2: time on jerk
    //3: time on const accel
    //4: change in velocity on jerk
    //5: change in velocity on accel
    //6: initial velocity

    //defining the output variables needed
    //dV stands for change in velocity
    //as stated in other constructor, time on jerk is the time spent on each individual jerk curve.
    double jerk, peakAccel, timeOnJerk, timeOnAccel, dVJerk, dVAccel;

    //find how many inputs are missing
    int missing = 0;
    if (changeInTime == null)
      i++;
    if (changeInOutput == null)
      i++;
    if (maxJerk == null)
      i++;
    if (maxAcceleration == null)
      i++;
    
    if (missing > 2)
      throw new RuntimeException("There were more than 2 missing arguments out of 4.");
    
    else if (missing == 2) {

      if (maxAcceleration == null) {
        if (maxJerk == null) {
          //jerk
        }
        else if (changeInTime == null) {
          //change in time
        }
        else {
          //change in velocity
        }
      }
      else if (maxJerk == null) {
        //jerk and either time or velocity is missing
        throw new RuntimeException("Change in time or change in velocity cannot be null if maxJerk is also null. Look at scs notes for explanation as to why.");
      }
      else {
        //time and velocity is missing, which is impossible
        throw new RuntimeException("Change in time and change in velocity cannot be null together.");
      }

    }
    else if (missing == 1) {
      if (maxJerk == null) {
        //jerk
        //You don't need to have the peak acceleration be the max acceleration, because its just a max acceleration. The real dependencies are the change in velocity and time.
        
        //first, you find the jerk and peak acceleration, pretending that there is no max acceleration. If the peak acceleration is greater than the max, than you use scenario 2.

        //for testing if scenario 1

        //dV = dt/2 * (dt/2 * jerk)
        //general equation for scenario 1
        //changeInOutput = (changeInTime/2)^2 * jerk

        //dV = dt^2/4 * jerk
        //jerk = dV/(dt^2/4)
        //jerk = 4 * dV / dt^2

        //peakAccel = j * (dt/2)
        //peakAccel = 4 * dV/dt^2 * dt/2
        //peakAccel = 2 * dV/dt
        //jerk = peakAccel/(dt/2) = 2 * peakAccel/dt

        //for finding values if using scenario 2

        //peakAccel = maxAcceleration

        //general equation for scenario 2
        //peakAccel * (changeInTime - timeOnJerk) = changeInOutput

        //changeInVelocity/peakAccel = changeInTime - timeOnJerk
        //timeOnJerk = changeInTime - changeInVelocity/peakAccel

        //timeOnAccel = changeInTime - 2 * timeOnJerk
        //timeOnAccel = changeInTime - 2 * (changeInTime - changeInVelocity/peakAccel)
        //timeOnAccel = changeInTime - 2 * changeInTime - 2 * changeInVelocity/peakAccel
        //timeOnAccel = -changeInTime - 2 * changeInVelocity/peakAccel

        peakAccel = 2.0 * changeInOutput/changeInTime;
        if (peakAccel < maxAcceleration) {
          //all good, use scenario 1
          jerk = 2.0 * peakAccel/changeInTime;
          timeOnJerk = changeInTime/2.0;
          timeOnAccel = 0.0;

          dVJerk = changeInOutput/2.0;
          dVAccel = 0.0;
        }
        else {
          //not great, use scenario 2
          peakAccel = maxAcceleration;
          timeOnJerk = changeInTime - changeInOutput/peakAccel;
          tineOnAccel = changeInTime - 2 * timeOnJerk;
          jerk = peakAccel/timeOnJerk;

          dVJerk = peakAccel * timeOnJerk/2;
          dVAccel = peakAccel * timeOnAccel;
        }
      }
      else if (changeInTime == null) {
        //change in time is missing

        //aim for the lowest possible change in time.

        //-----first, test if using scenario 1

        //jerk = maxJerk
        //timeOnJerk = changeInTime/2

        //using standard scenario 1 equation:
        //changeInOutput = (timeOnJerk)^2 * jerk

        //timeOnJerk^2 = changeInOutput/jerk
        //timeOnJerk = sqrt(changeInOutput/jerk)

        //peakAccel = jerk * timeOnJerk
        //peakAccel = jerk * sqrt(changeInOutput/jerk)
        //peakAccel = sqrt(changeInOutput * jerk)

        //-----get values for scenario 1

        //peakAccel = timeOnJerk * jerk
        //timeOnJerk = peakAccel/jerk

        //timeOnAccel = 0.0

        //-----if scenario 2, then get values

        //jerk = maxJerk
        //peakAccel = maxAcceleration

        //general equation for scenario 2:
        //peakAccel * (changeInTime - timeOnJerk) = changeInOutput
        //peakAccel * (timeOnJerk + timeOnAccel) = changeInOutput

        //timeOnJerk + timeOnAccel = changeInOutput/peakAccel
        //timeOnAccel = changeInOutput/peakAccel - timeOnJerk

        //timeOnJerk = peakAccel/jerk

        jerk = maxJerk;
        
        peakAccel = Math.sqrt(changeInOutput * jerk);
        if (peakAccel < maxAcceleration) {
          //all good, use scenario 1
          timeOnAccel = 0.0;
          timeOnJerk = peakAccel/jerk;

          dVJerk = changeInOutput/2;
          dVAccel = 0.0;
        }
        else {
          //not good, use scenario 2
          peakAccel = maxAcceleration;
          timeOnJerk = peakAccel/jerk;
          timeOnAccel = changeInOutput/peakAccel - timeOnJerk;

          dVJerk = (timeOnJerk * peakAccel)/2;
          dVAccel = peakAccel * timeOnAccel;
        }

      }
      else if (changeInOutput == null) {
        //change in velocity
      }
      else {
        //max acceleration
      }
    }
    else {
      //nothing is missing

      //basically using the procedure for when jerk is missing, but with one extra check
      peakAccel = 2.0 * changeInOutput/changeInTime;
      if (peakAccel < maxAcceleration) {
        //all good, use scenario 1
        jerk = 2.0 * peakAccel/changeInTime;
        timeOnJerk = changeInTime/2.0;
        timeOnAccel = 0.0;

        dVJerk = changeInOutput/2.0;
        dVAccel = 0.0;
      }
      else {
        //not great, use scenario 2
        peakAccel = maxAcceleration;
        timeOnJerk = changeInTime - changeInOutput/peakAccel;
        tineOnAccel = changeInTime - 2 * timeOnJerk;
        jerk = peakAccel/timeOnJerk;

        dVJerk = peakAccel * timeOnJerk/2;
        dVAccel = peakAccel * timeOnAccel;
      }
      if (jerk > maxJerk)
        throw new RuntimeException("The jerk needed for the curve is greater than the maximum jerk.");
      
    }

    if (using_ref_as_final_output) {
      refOutput -= 2 * dVJerk + dVAccel;
    }

  }

}