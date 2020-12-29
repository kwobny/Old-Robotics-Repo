package LithiumCore.AdvancedStuff;

import LithiumCore.Utils.UniDirectionalUtils.ContinuousSOF;
import LithiumCore.Utils.UniDirectionalUtils.UniTrans;
import LithiumCore.Utils.UniDirectionalUtils.UniWrapper;

import LithiumCore.SCS_Package.CommonOps;

//This is a double s curve. It includes two sub s curve sections separated by an arbitrary constant velocity section.
public class DoubleSCurve extends ContinuousSOF {

  //Parameters:
  //Max s curve acceleration or time on s curves (supply either one, or both). Both values must be positive.
  //Max output deviation. Can be positive or negative.
  //time on constant velocity. Must be positive.
  public DoubleSCurve(final Double peakAccel, final Double timeOnSCurve, final double maxOutputDeviation, final double timeOnConstVelocity, final double initialYValue) {
    SCurve curve = new SCurve(maxOutputDeviation, timeOnSCurve, peakAccel, initialYValue);

    UniTrans.ScaleY oppositeCurve = new UniTrans.ScaleY(-1.0);
    oppositeCurve.next = curve;
    
    if (timeOnConstVelocity == 0.0) {
      setFuncs(curve, oppositeCurve);
    }
    else {
      UniWrapper constantVel = new UniWrapper(timeOnSCurve,
        new CommonOps.ConstVelocity(0.0)
      );
      setFuncs(curve, constantVel, oppositeCurve);
    }
  }

}