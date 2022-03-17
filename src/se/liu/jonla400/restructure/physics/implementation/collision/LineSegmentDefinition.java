package se.liu.jonla400.restructure.physics.implementation.collision;

import se.liu.jonla400.restructure.main.levelcreation.LineSegmentType;
import se.liu.jonla400.restructure.math.Vector2D;

public class LineSegmentDefinition
{
    private Vector2D start;
    private Vector2D end;
    private LineSegmentType type;

    private LineSegmentDefinition(final Vector2D start, final Vector2D end, final LineSegmentType type) {
	this.start = start;
	this.end = end;
	this.type = type;
    }

    public static LineSegmentDefinition create(final Vector2D start, final Vector2D end, final LineSegmentType type) {
	return new LineSegmentDefinition(start, end, type);
    }

    public static LineSegmentDefinition create(final Vector2D start, final Vector2D end) {
	return create(start, end, LineSegmentType.DEFAULT);
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

    public LineSegmentType getType() {
	return type;
    }

    public void setType(final LineSegmentType type) {
	this.type = type;
    }
}
