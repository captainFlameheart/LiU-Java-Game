package se.liu.jonla400.project.main.levelcreation;

import se.liu.jonla400.project.main.leveldefinition.LineSegmentType;
import se.liu.jonla400.project.main.leveldefinition.LineSegmentDefinition;
import se.liu.jonla400.project.math.ClosestPointFinder;
import se.liu.jonla400.project.math.Vector2D;

/**
 * Represents a line segment and an index at which it exists in a {@link LevelBlueprint}'s list
 * of line segments. It also includes a {@link LineSegmentType}.
 */
public class IndexedLineSegment implements ClosestPointFinder
{
    private int index;
    private LineSegmentDefinition lineSegment;

    private IndexedLineSegment(final int index, final LineSegmentDefinition lineSegment) {
	this.index = index;
	this.lineSegment = lineSegment;
    }

    /**
     * Create a new IndexedLineSegment by copying the start and end points
     *
     * @param index The index of the line segment
     * @param start The start of the line segment
     * @param end The end of the line segment
     * @param type The type of the line segment
     * @return The created IndexedLineSegment
     */
    public static IndexedLineSegment copyEndPoints(final int index, final Vector2D start, final Vector2D end, final LineSegmentType type) {
	return new IndexedLineSegment(index, LineSegmentDefinition.copyEndPoints(start, end, type));
    }

    /**
     * @return The index of this line segment
     */
    public int getIndex() {
	return index;
    }

    /**
     * @return A read-only view of the start of this line segment
     */
    public Vector2D getStart() {
	return lineSegment.getStart();
    }

    /**
     * @return A read-only view of the end of this line segment
     */
    public Vector2D getEnd() {
	return lineSegment.getEnd();
    }

    /**
     * @return The type of this line segment
     */
    public LineSegmentType getType() {
	return lineSegment.getType();
    }

    /**
     * @return This line segment without the index
     */
    public LineSegmentDefinition removeIndex() {
	return lineSegment;
    }

    /**
     * Returns the closest of this line segment to the given point
     *
     * @param point The reference point
     * @return The closest point
     */
    @Override public Vector2D findClosestPointTo(final Vector2D point) {
	return lineSegment.findClosestPointTo(point);
    }
}
