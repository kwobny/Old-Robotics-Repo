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
      //time on jerk is the time spent on each individual jerk curve. 2 times this number gives the total time spent on jerk.

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

}