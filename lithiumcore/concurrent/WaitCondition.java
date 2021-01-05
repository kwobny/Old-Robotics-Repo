package lithiumcore.concurrent;

public interface WaitCondition {

  public boolean pollCondition(); //true means end wait, false means wait is still continuing

}