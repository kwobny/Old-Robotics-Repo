package org.firstinspires.ftc.teamcode.OpModes.LithiumCore.Utils;

public class StaticUtils {
	//an is equal function for doubles
	public static boolean isEqual(double a, double b) {
		return a == b || Math.abs(a - b) < 1E-6;
	}
}