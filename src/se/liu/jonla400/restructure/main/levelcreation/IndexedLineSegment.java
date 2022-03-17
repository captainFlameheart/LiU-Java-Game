package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.math.Vector2D;
import se.liu.jonla400.restructure.physics.implementation.collision.LineSegment;
import se.liu.jonla400.restructure.physics.implementation.collision.LineSegmentDefinition;

import java.awt.*;

public class IndexedLineSegment
{
    private int index;
    private LineSegmentDefinition lineSegment;

    private IndexedLineSegment(final int index, final LineSegmentDefinition lineSegment) {
	this.index = index;
	this.lineSegment = lineSegment;
    }

    public static IndexedLineSegment create(final int index, final Vector2D start, final Vector2D end, LineSegmentType type) {
	return new IndexedLineSegment(index, LineSegmentDefinition.create(start, end, type));
    }

    public int getIndex() {
	return index;
    }

    public Vector2D getStart() {
	return lineSegment.getStart();
    }

    public Vector2D getEnd() {
	return lineSegment.getEnd();
    }

    public LineSegmentType getType() {
	return lineSegment.getType();
    }

    public Color getColor() {
	return lineSegment.getType().getColor();
    }

    public Vector2D getClosestPointTo(final Vector2D point) {
	final LineSegment efficientLineSegment = LineSegment.createFromDefinition(lineSegment);
	return efficientLineSegment.getClosestPointTo(point);
    }
}
