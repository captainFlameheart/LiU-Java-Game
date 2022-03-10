package se.liu.jonla400.project.temp;

/**
 * A utility class that contains static methods for clamping values. This class cannot
 * be instantiated.
 */
public class Clamping
{
    private Clamping() {
    }

    /**
     * Returns the result of limiting the magnitude (absolute value) of the value.
     *
     * @param value The value whose magnitude to limit
     * @param maxMagnitude The maximum magnitude
     * @return The value after its magnitude has been limited
     */
    public static double limitMagnitude(final double value, final double maxMagnitude) {
	final double magnitude = Math.abs(value);
	if (magnitude > maxMagnitude) {
	    final double magnitudeRatio = maxMagnitude / magnitude;
	    return value * magnitudeRatio;
	}
	return value;
    }
}
