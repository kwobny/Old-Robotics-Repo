//Speed (/speedfactor) Control System

package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

import org.firstinspires.ftc.teamcode.OpModes.motionLibrary.MathFunctions.*;
import java.util.ArrayList;

//Every acceleration or automatic change in an output (motor buffer speedfactors mainly) is called an operation

//Each SCS operation consists of four main things:
//1: An input (usually time, distance, sensors, etc. but can be anything)
//2: An output (usually motor buffers, but can also be anything)
//3: A threshold which specifies when to stop running the operation
//4: A graph function (provided by the MathFunctions sub library)

public class SCS {

  //variables
  ArrayList<SCSOpUnit> operations = new ArrayList<>();
  ArrayList<Integer> indices = new ArrayList<>();
  
  //class cannot be instantiated outside of package
  SCS() {
    //
  }

  void runSCS() {
    for (int i = 0; i < operations.size(); i++) {
      SCSOpUnit j = operations.get(i);
      if (j.isRunning)
      {
        j.update();
      }
      else {
        indices.add(i);
      }
    }
    Constants.removeFromArray(operations, indices);
  }

  public SCSOpUnit addOperation(SCSOpUnit op) throws Exception {
    if (op.isRunning) throw new Exception("The SCS operation you are trying to run is already running");

    op.calibrate();
    op.isRunning = true;
    operations.add(op);
    return op;
  }

  public void removeOperation(SCSOpUnit op) throws Exception {
    if (!op.isRunning) throw new Exception("Cannot remove an SCS operation that has not been added in the first place");
    op.running = false;
  }

  public class AddOpCallback implements WaitCallback {
    private SCSOpUnit operation;

    public AddOpCallback(SCSOpUnit operation) {
      this.operation = operation;
    }

    @Override
    public void run(WaitCondition cond) {
      addOperation(operation);
    }
  }

  public class RemoveOpCallback implements WaitCallback {
    private SCSOpUnit operation;

    public RemoveOpCallback(SCSOpUnit operation) {
      this.operation = operation;
    }

    @Override
    public void run(WaitCondition cond) {
      removeOperation(operation);
    }
  }

}