package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils;

public class StaticUtils {

  private StaticUtils() {} //cannot make an instance of this class.

	//an is equal function for doubles
	public static boolean isEqual(double a, double b) {
		return a == b || Math.abs(a - b) < 1E-6;
	}

  //Callback wrapper
  //Wraps a Callback to make it usable as a Function
  public static class CallbackWrapper implements Function<Void, Void> {
    public final Callback callback;
    @Override
    public Void apply(final Void param) {
      callback.run();
      return null;
    }
    public CallbackWrapper(final Calback callback) {
      this.callback = callback;
    }
  }
  public static CallbackWrapper getCW(final Callback callback) {
    return new CallbackWrapper(callback);
  }

  //Void function wrapper
  //Opposite of callback wrapper
  public static class VoidFunctionWrapper implements Callback {
    public final Function<Void, Void> function;
    @Override
    public void run() {
      function.apply(null);
    }
    public VoidFunctionWrapper(final Function<Void, Void> function) {
      this.function = function;
    }
  }
  public static VoidFunctionWrapper getVFW(final Function<Void, Void> function) {
    return new VoidFunctionWrapper(function);
  }
}