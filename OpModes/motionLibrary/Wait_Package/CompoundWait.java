package org.firstinspires.ftc.teamcode.OpModes.motionLibrary.Wait_Package;

public abstract class CompoundWait implements WaitCondition {

  WaitTask[] tasks;

  public static CompoundWait getNew(Comparator mode, final WaitTask ...tasks) {

    CompoundWait retVal;

    switch (mode) {
      case AND:
        retVal = new AndPoll();
        break;
      case OR:
        retVal = new OrPoll();
        break;
      default:
        throw new RuntimeException("I don't know how the F#ck you did this, but you managed to provide a value from the Comparator enum that was not accounted for in the CompoundWait functionality.");
    }

    retVal.tasks = tasks;

    return retVal;
  }

  private static class AndPoll extends CompoundWait {

    private boolean[] conditionSatisfied = new boolean[conditions.length];
    
    @Override
    public boolean pollCondition() {
      for (WaitTask currentWait : tasks) {

        if (!conditionSatisfied[i] && currentWait.run()) {
          conditionSatisfied[i] = true;
        }

      }
      
      for (boolean bool : conditionSatisfied) {
        if (!bool) {
          return false;
        }
      }
      return true;
    }

  }

  private static class OrPoll extends CompoundWait {
    
    @Override
    public boolean pollCondition() {
      for (WaitTask currentWait : waits) {

        if (currentWait.run()) {
          return true;
        }

      }
      return false;
    }

  }

}