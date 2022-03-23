package se.liu.jonla400.project.main.levelcreation;

import se.liu.jonla400.project.main.leveldefinition.LineSegmentType;
import se.liu.jonla400.project.main.leveldefinition.LineSegmentDefinition;
import se.liu.jonla400.project.math.ClosestPointFinder;
import se.liu.jonla400.project.math.Vector2D;

public class IndexedLineSegment implements ClosestPointFinder
{
    private int index;
    private LineSegmentDefinition lineSegment;

    private IndexedLineSegment(final int index, final LineSegmentDefinition lineSegment) {
	this.index = index;
	this.lineSegment = lineSegment;
    }

    public static IndexedLineSegment copyEndPoints(final int index, final Vector2D start, final Vector2D end, final LineSegmentType type) {
	return new IndexedLineSegment(index, LineSegmentDefinition.copyEndPoints(start, end, type));
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

    public LineSegmentDefinition removeIndex() {
	return lineSegment;
    }

    @Override public Vector2D findClosestPointTo(final Vector2D point) {
	return lineSegment.findClosestPointTo(point);
    }
}
