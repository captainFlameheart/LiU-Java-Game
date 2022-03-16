package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.math.Vector2D;
import se.liu.jonla400.restructure.physics.implementation.collision.LineSegment;
import se.liu.jonla400.restructure.physics.implementation.collision.LineSegmentDefinition;

public class IndexedLineSegment
{
    private IndexedVertex start;
    private IndexedVertex end;

    public IndexedLineSegment(final IndexedVertex start, final IndexedVertex end) {
	this.start = start;
	this.end = end;
    }

    public IndexedVertex getStart() {
	return start;
    }

    public int getStartIndex() {
	return start.getIndex();
    }

    public Vector2D getStartPos() {
	return start.getVertex();
    }

    public IndexedVertex getEnd() {
	return end;
    }

    public int getEndIndex() {
	return end.getIndex();
    }

    public Vector2D getEndPos() {
	return end.getVertex();
    }

    public Vector2D getClosestPointTo(final Vector2D point) {
	final LineSegment lineSegment = new LineSegment(start.getVertex(), end.getVertex());
	return lineSegment.getClosestPointTo(point);
    }
}
