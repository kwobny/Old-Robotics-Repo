package org.firstinspires.ftc.teamcode.OpModes.motionLibrary.SCS_Package;
import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.Wait_Package.*;

public abstract class ThresholdWait implements WaitCondition {

  public double threshold;
  public boolean isAbove;

  //the is above flag signifies whether or not the output value needs to be above the threshold for the wait to be over.
  protected ThresholdWait(final double threshold, final boolean isAbove) {
    this.threshold = threshold;
    this.isAbove = isAbove;
  }

  protected ThresholdWait() {
    //
  }

  @Override
  public boolean pollCondition() {
    return (isAbove && getCompVal() > threshold) || (!isAbove && getCompVal() < threshold);
  }

  //get value to compare function
  protected abstract double getCompVal();

}

//example implementation

/*
public class Example extends ThresholdWait {

  public Example(final double threshold, final boolean isAbove) {
    super(threshold, isAbove);
  }

  @Override
  protected double getCompVal() {
    return (blah blah blah);
  }

}
*/


//alternative threshold wait

/*public abstract class ThresholdWait implements WaitCondition {

  public double threshold;
  private WaitCondition cond;

  protected ThresholdWait(final double threshold, final boolean isAbove) {
    this.threshold = threshold;
    
    if (isAbove) {
      cond = new WaitCondition() {
        
        @Override
        public boolean pollCondition() {
          return getCompVal() > threshold;
        }

      };
    }
    else {
      cond = new WaitCondition() {
        
        @Override
        public boolean pollCondition() {
          return getCompVal() < threshold;
        }

      };
    }
  }

  @Override
  public boolean pollCondition() {
    return cond.pollCondition();
  }

  protected abstract double getCompVal();

}*/