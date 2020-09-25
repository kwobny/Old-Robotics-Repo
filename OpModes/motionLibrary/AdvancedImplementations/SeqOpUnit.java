package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.Wait_Package.*;
import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.SCS_Package.*;
import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.MathFunctions.*;

//Sequential SCS Op Unit
//In this class, you basically input a bunch of graph sections. Each section will run in the order according to indices in which it is provided.
//In each section, the class waits for the x value to be reached, and then continues on to the next section. For the graph functions, the point where x = 0 is always going to be where the section was activated, so no need to worry about translating it.
//This class partitions the sections using the x value only
//This class only works for x inputs which only increase.
public class SeqOpUnit extends Coroutine {

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

  private final CommonTrans.TranslateX transform = new CommonTrans.TranslateX();
  private final SCSOpUnit operation = new SCSOpUnit();
  private final SCSOpUnit.InputCond condition = new operation.InputCond();
  {
    condition.isAbove = true;
    operation.graphFunc = transform;
  }

  protected Section[] sections;
  private int index;
  private boolean isActive = false;

  protected SCS scs;

  public SeqOpUnit(final WaitCore core, final SCS scs, final InputSource input, final OutputSink output, final Section ...sections) {
    this(core, scs, input, output);
    this.sections = sections;
  }
  public SeqOpUnit(final WaitCore core, final SCS scs, final InputSource input, final OutputSink output) {
    super(core);
    this.scs = scs;

    operation.input = input;
    operation.output = output;
  }

  public void cancelOperation() {
    if (!isActive)
      throw new RuntimeException("You cannot cancel a sequential operation that is not running.");
    endCallback.run();
  }

  @Override
  protected void _start() {
    if (isActive)
      throw new RuntimeException("You cannot run a sequential operation that is already running. Call the cancel operation method to stop it.");
    isActive = true;

    index = 0;
    changeGraph.run();
    scs.addOperation(operation);
  }

  private final Callback changeGraph = new Callback() {
    @Override
    public void run() {
      if (index == sections.length) {
        endCallback.run();
        return;
      }

      transform.next = sections[index].graphFunc;
      transform.shiftX = index > 0 ? sections[index-1].endXVal : 0.0;

      condition.threshold = sections[index].endXVal;
      index += 1;
    }
  };

  @Override
  protected void _end() {
    scs.removeOperation(operation);
    isActive = false;
  }

  {
    setNext(condition, changeGraph);
  }

}