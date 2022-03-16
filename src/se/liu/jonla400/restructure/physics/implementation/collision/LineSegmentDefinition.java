package se.liu.jonla400.restructure.physics.implementation.collision;

import se.liu.jonla400.restructure.math.Vector2D;

public class LineSegmentDefinition
{
    private Vector2D start;
    private Vector2D end;

    private LineSegmentDefinition(final Vector2D start, final Vector2D end) {
	this.start = start;
	this.end = end;
    }

    public static LineSegmentDefinition create(final Vector2D start, final Vector2D end) {
	return new LineSegmentDefinition(start, end);
    }

    public Vector2D getStart() {
	return start;
    }

    public void setStart(final Vector2D start) {
	this.start = start;
    }

    public Vector2D getEnd() {
	return end;
    }

    public void setEnd(final Vector2D end) {
	this.end = end;
    }
}
