package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.MathFunctions.*;

//this class is an immutable class
//this class only works when x value always increase.
//the domain of each graph func includes its corresponding endXVal.
//always manually reset the speed factors and set the output after this is done.
public class SeqOpFunc implements MathFunction {

  public static class Section {
    public final double endXVal;
    public final MathFunction graphFunc;
    public Section(final double endXVal, final MathFunction graphFunc) {
      this.endXVal = endXVal;
      this.graphFunc = graphFunc;
    }
  }
  public static Section getSection(final double endXVal, final MathFunction graphFunc) {
    return new Section(endXVal, graphFunc);
  }

  //this method gets the x value where the domain of the whole seq op math function ends. This is the threshold you should wait for if you are doing an input wait.
  public double getEndThreshold() {
    return sections[sections.length-1].endXVal;
  }
  
  protected Section[] sections;
  private int index = 0;
  private double translateVal = 0.0;

  private boolean isDone = false;

  protected SeqOpFunc() {
    //
  }
  public SeqOpFunc(final Section ...sections) {
    this.sections = sections;
  }

  public void reset() {
    isDone = false;
    index = 0;
    translateVal = 0.0;
  }
  
  @Override
  public double yValueOf(final double input) {

    if (input > sections[index].endXVal) {
      if (index >= sections.length-1) {
        if (isDone)
          throw new RuntimeException("You cannot get the y-value of a seq op function that has already ended");
        isDone = true;
      }
      else {
        translateVal += sections[index].endXVal;
        index += 1;
      }
    }
    
    return sections[index].graphFunc.yValueOf(input - translateVal);
  }

}