package se.liu.jonla400.project.physics.collision.implementation;

import se.liu.jonla400.project.math.ClosestPointFinder;
import se.liu.jonla400.project.math.Interval;
import se.liu.jonla400.project.math.Vector2D;

/**
 * Represents a line segment between two points with custom user data associated with it. The
 * main feature of a line segment is that it can compute the closest point to a given point.
 * A line segment of this class is immutable in order to be able to precompute values.
 *
 * @param <T> The type of user data associated with this line segment
 */
public class LineSegment<T> implements ClosestPointFinder
{
    private Vector2D start;
    private Vector2D end;

    private Vector2D normal;
    private Vector2D tangent;

    private double normalPos;
    private double tangentStart;
    private double tangentEnd;

    private T userData;

    private LineSegment(final Vector2D start, final Vector2D end, final T userData, final Vector2D normal, final Vector2D tangent,
			final double normalPos, final double tangentStart, final double tangentEnd)
    {
	this.start = start;
	this.end = end;
	this.userData = userData;
	this.normal = normal;
	this.tangent = tangent;
	this.normalPos = normalPos;
	this.tangentStart = tangentStart;
	this.tangentEnd = tangentEnd;
    }

    /**
     * Creates a LineSegment with the given start and end points and the given user data.
     * No references are kept of the input vectors. The start and end points must not be equal.
     *
     * @param start The start point
     * @param end The end point
     * @param userData The user data
     * @param <T> The type of user data
     * @return The created LineSegment
     */
    public static <T> LineSegment<T> copyEndPoints(final Vector2D start, final Vector2D end, T userData) {
	final Vector2D startToEnd = end.subtract(start);
	if (startToEnd.isZero()) {
	    throw new IllegalArgumentException("The start and end points are the same: " + start);
	}
	final Vector2D tangent = startToEnd.normalize();
	final Vector2D normal = tangent.rotate90Degrees();

	final double normalPos = normal.dot(start);
	final double tangentStart = tangent.dot(start);
	final double tangentEnd = tangent.dot(end);

	return new LineSegment<>(start.copy(), end.copy(), userData, normal, tangent, normalPos, tangentStart, tangentEnd);
    }

    /**
     * @return A read-only view of the start
     */
    public Vector2D getStart() {
	return start.copy();
    }

    /**
     * @return A read-only view of the end
     */
    public Vector2D getEnd() {
	return end.copy();
    }

    /**
     * @return The user data
     */
    public T getUserData() {
	return userData;
    }

    /**
     * Returns the closest point to the given point
     *
     * @param point The reference point
     * @return The closest point
     */
    @Override public Vector2D findClosestPointTo(final Vector2D point) {
	final double tangentPos = tangent.dot(point);
	final double closestTangentPos = new Interval(tangentStart, tangentEnd).clamp(tangentPos);
	return tangent.multiply(closestTangentPos).add(normal.multiply(normalPos));
    }
}
