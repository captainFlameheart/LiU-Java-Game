package se.liu.jonla400.project.main.leveldefinition;

import se.liu.jonla400.project.math.ClosestPointFinder;
import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.collision.implementation.LineSegment;

/**
 * Defines a line segment, including its {@link LineSegmentType}. Also contains method's
 * for finding the closest point to another point and converting to a {@link LineSegment}.
 */
public class LineSegmentDefinition implements ClosestPointFinder
{
    private Vector2D start;
    private Vector2D end;
    private LineSegmentType type;

    private LineSegmentDefinition() {
	// Used by gson
	start = null;
	end = null;
	type = null;
    }

    private LineSegmentDefinition(final Vector2D start, final Vector2D end, final LineSegmentType type) {
	this.start = start;
	this.end = end;
	this.type = type;
    }

    /**
     * Returns a new LineSegmentDefinition by copying the start and end points
     *
     * @param start The start point of the line segment
     * @param end The end point of the line segment
     * @param type The type of the line segment
     * @return The created LineSegmentDefinition
     */
    public static LineSegmentDefinition copyEndPoints(final Vector2D start, final Vector2D end, final LineSegmentType type) {
	if (start.equals(end)) {
	    throw new IllegalArgumentException("The start and end points are the same: " + start);
	}
	return new LineSegmentDefinition(start.copy(), end.copy(), type);
    }

    /**
     * Create a LineSegmentDefinition by converting from the collidable {@link LineSegment}
     *
     * @param collidableSegment The collidable segment to convert
     * @return The created LineSegmentDefinition
     */
    public static LineSegmentDefinition createFromCollidableSegment(final LineSegment<LineSegmentType> collidableSegment) {
	return new LineSegmentDefinition(collidableSegment.getStart(), collidableSegment.getEnd(), collidableSegment.getUserData());
    }

    /**
     * @return Whether this definition is considered invalid
     */
    public boolean isInvalid() {
	return start == null || end == null || type == null || start.equals(end);
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
     * @return The type of this line segment
     */
    public LineSegmentType getType() {
	return type;
    }

    /**
     * @return A line segment used by the {@link se.liu.jonla400.project.physics} package
     */
    public LineSegment<LineSegmentType> convertToCollidableSegment() {
	return LineSegment.copyEndPoints(start, end, type);
    }

    /**
     * Returns the closest of this line segment to the given point
     *
     * @param point The reference point
     * @return The closest point
     */
    @Override public Vector2D findClosestPointTo(final Vector2D point) {
	return convertToCollidableSegment().findClosestPointTo(point);
    }
}
