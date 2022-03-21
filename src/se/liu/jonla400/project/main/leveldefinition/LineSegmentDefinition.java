package se.liu.jonla400.project.main.leveldefinition;

import se.liu.jonla400.project.main.LineSegmentType;
import se.liu.jonla400.project.math.ClosestPointFinder;
import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.implementation.collision.LineSegment;

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

    public static LineSegmentDefinition copyEndPoints(final Vector2D start, final Vector2D end, final LineSegmentType type) {
	return new LineSegmentDefinition(start.copy(), end.copy(), type);
    }

    public Vector2D getStart() {
	return start.copy();
    }

    public Vector2D getEnd() {
	return end.copy();
    }

    public LineSegmentType getType() {
	return type;
    }

    public LineSegment<LineSegmentType> convertToCollidableSegment() {
	return LineSegment.copyEndPoints(start, end, type);
    }

    @Override public Vector2D findClosestPointTo(final Vector2D point) {
	return convertToCollidableSegment().findClosestPointTo(point);
    }
}
