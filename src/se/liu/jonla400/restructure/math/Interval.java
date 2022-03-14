package se.liu.jonla400.restructure.math;

/**
 * Represents an interval with a start and an end value, in double precision.
 * No restriction is put in place whether the start value precedes the end value or not.
 */
public class Interval
{
    private double start;
    private double end;

    /**
     * Creates a new interval with the given start and end values
     *
     * @param start The start value
     * @param end The end value
     */
    public Interval(final double start, final double end) {
	this.start = start;
	this.end = end;
    }

    /**
     * Gets the start value
     *
     * @return The start value
     */
    public double getStart() {
	return start;
    }

    /**
     * Gets the end value
     *
     * @return The end value
     */
    public double getEnd() {
	return end;
    }

    /**
     * Gets the displacement of the end value from the start value, which
     * can be negative.
     *
     * @return The displacement from the start to the end
     */
    public double getStartToEndDisplacement() {
	return end - start;
    }

    /**
     * Returns the result of flipping this interval
     *
     * @return A flipped version of this interval
     */
    public Interval flip() {
	return new Interval(end, start);
    }

    /**
     * Returns the result of flipping this interval if the boolean is true
     *
     * @param b The boolean indicating whether to flip or not
     * @return A potentially flipped version of this interval
     */
    public Interval flipIf(final boolean b) {
	return b ? flip() : new Interval(start, end);
    }

    /**
     * Returns the result of sorting this interval so that the start value
     * precedes the end value
     *
     * @return A sorted version of this interval
     */
    public Interval sort() {
	return flipIf(end < start);
    }

    /**
     * Returns the value within this interval closest to the given value
     *
     * @param value The value to clamp
     * @return The closest value
     */
    public double clamp(final double value) {
	final Interval sorted = sort();
	if (value < sorted.start) {
	    return sorted.start;
	}
	return Math.min(value, sorted.end);
    }

    /**
     * Performs linear interpolation between the start and end value of this interval.
     * The passed in value is typically between 0 and 1, and represent "how long" to
     * travel from the start value to the end value. The point traveled to is returned.
     * A value of 0 returns the start value, a value of 0.5 returns the mid-point between
     * the start and end values and a value of 1 returns the end value.
     *
     * @param normalizedValue The interpolant (how long to travel, typically between 0 and 1)
     * @return The value traveled to
     */
    public double performLerp(final double normalizedValue) {
	final double startToEndDisplacement = getStartToEndDisplacement();
	final double startToValueDisplacement = normalizedValue * startToEndDisplacement;
	return start + startToValueDisplacement;
    }

    /**
     * Performs inverse linear interpolation between the start and end value of this interval.
     * The result of this operation is a value, typically between 0 and 1, that represent the
     * placement of the given value inside this interval. A result value of 0 means that the
     * given value is the start value, a result value of 0.5 means that the given value is
     * the mid-point of the start and end values and a result value of 1 means that the given
     * value is the end value.
     *
     * @param value The value to convert into an interpolant
     * @return The interpolant (i.e. placement in this interval)
     */
    public double performInverseLerp(final double value) {
	final double startToValueDisplacement = value - start;
	final double startToEndDisplacement = getStartToEndDisplacement();
	return startToValueDisplacement / startToEndDisplacement;
    }

    /**
     * Returns the corresponding value in another interval. If the value
     * is the start value of this interval, the result will be the start
     * value of the other interval. If the value is the mid-point between
     * the start and end values of this interval, the result will be the
     * mid-point between the start and end values of the other interval.
     * If the value is the end value of this interval, the result will be
     * the end value of the other interval.
     *
     * @param value The value to map to the other interval
     * @param otherInterval The interval to map to
     * @return The corresponding value in the other interval
     */
    public double mapValueToOtherInterval(final double value, final Interval otherInterval) {
	final double normalizedValue = performInverseLerp(value);
	return otherInterval.performLerp(normalizedValue);
    }

    @Override public String toString() {
	return "Interval{" + "start=" + start + ", end=" + end + '}';
    }
}
