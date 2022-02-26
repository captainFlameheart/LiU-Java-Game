package se.liu.jonla400.project.temp;

public class Interval
{
    private double start;
    private double end;

    public Interval(final double start, final double end) {
	this.start = start;
	this.end = end;
    }

    public double getStart() {
	return start;
    }

    public double getEnd() {
	return end;
    }

    public double getStartToEndDisplacement() {
	return end - start;
    }

    public Interval sort() {
	return end < start ? new Interval(end, start) : new Interval(start, end);
    }

    public double performLerp(final double normalizedValue) {
	final double startToEndDisplacement = getStartToEndDisplacement();
	final double startToValueDisplacement = normalizedValue * startToEndDisplacement;
	return start + startToValueDisplacement;
    }

    public double performInverseLerp(final double value) {
	final double startToValueDisplacement = value - start;
	final double startToEndDisplacement = getStartToEndDisplacement();
	return startToValueDisplacement / startToEndDisplacement;
    }

    public double mapValueToOtherInterval(final double value, final Interval otherInterval) {
	final double normalizedValue = performInverseLerp(value);
	return otherInterval.performLerp(normalizedValue);
    }
}
