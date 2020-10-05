//Speed (/speedfactor) Control System

package org.firstinspires.ftc.teamcode.OpModes.motionLibrary.SCS_Package;

import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.GenericOperation.*;

//Every acceleration or automatic change in an output (motor buffer speedfactors mainly) is called an operation

//Each SCS operation consists of four main things:
//1: An input (usually time, distance, sensors, etc. but can be anything)
//2: An output (usually motor buffers, but can also be anything)
//3: A threshold which specifies when to stop running the operation
//4: A graph function (provided by the MathFunctions sub library)

public class SCS {

  //variables
  private final OperationRunner<SCSOpUnit> opRunner = new OperationRunner<>() {
    
    @Override
    protected void runOp(SCSOpUnit op) {
      op.run();
    }

  };
  
  //class ideally is used only inside motion library
  public SCS() {
    //
  }

  //this is the function that should be called in the high frequency interval method. Ideally should be used only inside motion library
  public void _runSCS() {
    opRunner.runAll();
  }

  //adds, calibrates, and starts a new operation. Can be used to restart an operation that was running before.
  public SCSOpUnit addOperation(final SCSOpUnit op) {
    opRunner.add(op);
    op.calibrate();
    return op;
  }

  //resumes the operation from the point when it was removed/paused.
  public void resumeOperation(final SCSOpUnit op) {
    opRunner.add(op);
    op.restoreState();
  }

  //The remove operation also serves as a pause operation function.
  public void removeOperation(SCSOpUnit op) {
    opRunner.delete(op);
    op.saveState();
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