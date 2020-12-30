package lithiumcore.scsfunction;

import lithiumcore.utils.unidirectionfunc.ContinuousSOF;
import lithiumcore.utils.unidirectionfunc.UniTrans;
import lithiumcore.utils.unidirectionfunc.UniWrapper;

import lithiumcore.scs.CommonOps;

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