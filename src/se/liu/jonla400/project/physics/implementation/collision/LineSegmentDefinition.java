package se.liu.jonla400.project.physics.implementation.collision;

import se.liu.jonla400.project.math.Vector2D;

public class LineSegmentDefinition
{
    private Vector2D start;
    private Vector2D end;

    private LineSegmentDefinition(final Vector2D start, final Vector2D end) {
	this.start = start;
	this.end = end;
    }

    public static LineSegmentDefinition create(final Vector2D start, final Vector2D end) {
	return new LineSegmentDefinition(start.copy(), end.copy());
    }

    public Vector2D getStart() {
	return start.copy();
    }

    public void setStart(final Vector2D start) {
	this.start.set(start);
    }

    public Vector2D getEnd() {
	return end.copy();
    }

    public void setEnd(final Vector2D end) {
	this.end.set(end);
    }
}
