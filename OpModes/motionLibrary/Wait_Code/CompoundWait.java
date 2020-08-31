package org.firstinspires.ftc.teamcode.OpModes.motionLibrary;

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
        return null;
        break;
    }

    retVal.tasks = tasks;
  }

  private static class AndPoll extends CompoundWait {

    private boolean[] conditionSatisfied = new boolean[conditions.length];
    
    @Override
    public boolean pollCondition() {
      for (WaitTask currentWait : tasks) {

        if (!conditionSatisfied[i] && currentWait.condition.pollCondition()) {
          if (currentWait.callback != null) {
            currentWait.callback.run(currentWait.condition);
          }
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

        if (currentWait.condition.pollCondition()) {
          if (currentWait.callback != null) {
            currentWait.callback.run(currentWait.condition);
          }
          return true;
        }

      }
      return false;
    }

  }

}