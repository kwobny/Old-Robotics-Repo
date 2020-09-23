package org.firstinspires.ftc.teamcode.OpModes.motionLibrary.GenericOperation;

import java.util.ArrayList;


//IMPORTANT: ----------------------------------------
//This class assumes that there will be only 1 runner per operation (whole lifespan). The operation could theoretically run on multiple platforms, just make sure that there is only 1 runner instance. However, this is not advised. Just try to run each operation on 1 thing only.
public abstract class OperationRunner<T extends Operation> {

  //data members
  private final ArrayList<Operation> opList = new ArrayList<>();
  
  public void add(Operation op) {
    if (op.currentRunner != null && op.currentRunner != this) {
      throw new RuntimeException("Each operation can only be assigned to 1 runner");
    }
    op.currentRunner = this;

    if (op.isActive)
      throw new RuntimeException("You cannot add an operation that is already added.");
    op.isActive = true;

    if (op.needsDelete) {
      op.needsDelete = false;
    }
    else {
      opList.add(op);
    }
  }

  public void remove(Operation op) {
    if (!op.isActive)
      throw new RuntimeException("You cannot add an operation that is already added.");
    op.isActive = false;

    op.needsDelete = true;
  }

  public void runAll() {
    for (int i = 0; i < opList.size(); i++) {

      final Operation op = opList.get(i);
      if (!op.needsDelete) {
        //run the operation code. In here, the is active properties can be set to true/false/whatever
        runOp(op);
      }

    }
    //scan and delete
    for (int i = 0; i < opList.size(); i++) {
      final Operation op = opList.get(i);
      if (op.needsDelete) {
        op.needsDelete = false;
        opList.remove(i);
        i -= 1;
      }
    }
  }

  protected abstract void runOp(T op);
  
  /*
  private final ArrayList<Integer> indices = new ArrayList<>();

  private void removeFromArray() {
    //the index array is expected to be sorted from least to greatest
    int valueDecrease = 0;
    for (int i : indices) {
      opList.remove(i-valueDecrease);
      valueDecrease += 1;
    }
  }
  */

}