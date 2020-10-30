//Speed (/speedfactor) Control System

package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.SCS_Package;

import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.PileUtils.*;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Callback;
import org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils.Consumer;

//Every acceleration or automatic change in an output (motor buffer speedfactors mainly) is called an operation

//Each SCS operation consists of four main things:
//1: An input (usually time, distance, sensors, etc. but can be anything)
//2: An output (usually motor buffers, but can also be anything)
//3: A threshold which specifies when to stop running the operation
//4: A graph function (provided by the MathFunctions sub library)

public class SCS {

  //variables
  private final BindingFullPile<SCSOpUnit> pile = new BindingFullPile<>();
  
  //class ideally is used only inside motion library
  public SCS() {
    //
  }

  private final static Consumer<SCSOpUnit> pileConsumer = new Consumer<>() {
    @Override
    public void run(final SCSOpUnit unit) {
      unit.run();
    }
  };
  
  //this is the function that should be called in the high frequency interval method. Ideally should be used only inside motion library
  public void _runSCS() {
    pile.forAll(pileConsumer);
  }

  //adds, calibrates, and starts a new operation. It hard resets the operation.
  public SCSOpUnit addOperation(final SCSOpUnit op) {
    pile.add(op);
    op.isPaused = false;
    if (op.graphFunc instanceof CalibratedFunc)
      op.privateCalibrate();
    return op;
  }

  //Used to pause an operation to be resumed later.
  public void pauseOperation(final SCSOpUnit op) {
    if (!(op instanceof UniDirectionalFunc))
      throw new RuntimeException("You cannot use the pausing mechanism on a function that is not unidirectional.");
    pile.remove(op);
    op.isPaused = true;
    op.saveState();
  }

  //resumes the operation from the point when it was paused.
  public void resumeOperation(final SCSOpUnit op) {
    if (!(op instanceof UniDirectionalFunc))
      throw new RuntimeException("You cannot use the pausing mechanism on a function that is not unidirectional.");
    if (!op.isPaused)
      throw new RuntimeException("You cannot resume an scs op unit that was not paused before.");
    pile.add(op);
    op.isPaused = false;
    op.restoreState();
  }

  //Hard remove an operation
  public void removeOperation(SCSOpUnit op) {
    pile.remove(op);
    op.isPaused = false;
  }

  public class AddOpCallback implements Callback {
    private SCSOpUnit operation;

    public AddOpCallback(SCSOpUnit operation) {
      this.operation = operation;
    }

    @Override
    public void run() {
      addOperation(operation);
    }
  }

  public class RemoveOpCallback implements Callback {
    private SCSOpUnit operation;

    public RemoveOpCallback(SCSOpUnit operation) {
      this.operation = operation;
    }

    @Override
    public void run() {
      removeOperation(operation);
    }
  }

}