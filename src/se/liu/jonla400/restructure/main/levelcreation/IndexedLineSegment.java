package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.math.Vector2D;
import se.liu.jonla400.restructure.physics.implementation.collision.LineSegment;

import java.awt.*;

public class IndexedLineSegment
{
    private int index;
    private Vector2D start;
    private Vector2D end;
    private LineSegmentType type;

    private IndexedLineSegment(final int index, final Vector2D start, final Vector2D end, final LineSegmentType type) {
	this.index = index;
	this.start = start;
	this.end = end;
	this.type = type;
    }

    public static IndexedLineSegment create(final int index, final Vector2D start, final Vector2D end, final LineSegmentType type) {
	return new IndexedLineSegment(index, start.copy(), end.copy(), type);
    }

    public int getIndex() {
	return index;
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

    public Color getColor() {
	return type.getColor();
    }

    public LineSegment<LineSegmentType> convertToCollidableLineSegment() {
	return LineSegment.create(start, end, type);
    }

    public Vector2D getClosestPointTo(final Vector2D point) {
	return convertToCollidableLineSegment().getClosestPointTo(point);
    }
}
