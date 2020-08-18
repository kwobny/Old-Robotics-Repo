//Speed (/speedfactor) Control System

package new_code;

import MathFunctions.*;
import java.util.ArrayList;

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

  public SCSOpUnit addOperation(SCSOpUnit op) {
    op.calibrate();
    op.isRunning = true;
    operations.add(op);
    return op;
  }

  public void removeOperation(SCSOpUnit op) {
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