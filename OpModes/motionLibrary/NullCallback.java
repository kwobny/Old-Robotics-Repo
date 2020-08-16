public class NullCallback implements Callback {
  public static final Callback obj = new NullCallback();

  public void run() {}
}