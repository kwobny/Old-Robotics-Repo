/*public class NullCallback implements Callback {
  public static final Callback obj = new NullCallback();

  public void run() {}
}*/

public enum NullCallback implements WaitCallback {
  obj;

  public void run() {}
}