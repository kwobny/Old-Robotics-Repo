package LithiumCore.Wait_Package;

//This class is immutable
public abstract class CompoundCondition implements WaitCondition {

  public final WaitCondition[] conditions;

  public CompoundCondition(final WaitCondition ...conditions) {
    this.conditions = conditions;
  }

  public static class AndPoll extends CompoundCondition {

    private boolean[] conditionSatisfied;

    public AndPoll(final WaitCondition ...conditions) {
      super(conditions);
      conditionSatisfied = new boolean[conditions.length];
    }
    
    @Override
    public boolean pollCondition() {
      int completeCounter = 0;

      for (int i = 0; i < conditions.length; ++i) {
        WaitCondition cond = conditions[i];

        if (conditionSatisfied[i]) {
          ++completeCounter;
        }
        else if (cond.pollCondition()) {
          conditionSatisfied[i] = true;
          ++completeCounter;
        }

      }

      return completeCounter == conditions.length;
    }

  }

  public static class OrPoll extends CompoundCondition {

    public OrPoll(final WaitCondition ...conditions) {
      super(conditions);
    }
    
    @Override
    public boolean pollCondition() {
      for (WaitCondition cond : conditions) {
        if (cond.pollCondition()) {
          return true;
        }
      }
      return false;
    }

  }

  public static AndPoll getAnd(final WaitCondition ...conditions) {
    return new AndPoll(conditions);
  }
  public static OrPoll getOr(final WaitCondition ...conditions) {
    return new OrPoll(conditions);
  }

}