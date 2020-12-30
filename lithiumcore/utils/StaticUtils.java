package lithiumcore.utils;

public class StaticUtils {

  private StaticUtils() {} //cannot make an instance of this class.

	//an is equal function for doubles
	public static boolean equals(double a, double b) {
		return a == b || Math.abs(a - b) < 1E-6;
	}
  
}