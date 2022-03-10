package se.liu.jonla400.project.physics.implementation.collision;

import se.liu.jonla400.project.math.Interval;
import se.liu.jonla400.project.math.Vector2D;

public class LineSegment
{
    private Vector2D start;
    private Vector2D end;

    private Vector2D normal;
    private Vector2D tangent;

    private double normalPos;
    private double tangentStart;
    private double tangentEnd;

    public LineSegment(final Vector2D start, final Vector2D end) {
	this.start = start.copy();
	this.end = end.copy();

	final Vector2D startToEnd = end.subtract(start);
	tangent = startToEnd.normalize();
	normal = tangent.rotate90Degrees(Vector2D.RotationDirection.X_TO_Y);

	normalPos = normal.dot(start);
	tangentStart = tangent.dot(start);
	tangentEnd = tangent.dot(end);
    }

    public Vector2D getStart() {
	return start.copy();
    }

    public Vector2D getEnd() {
	return end.copy();
    }

    public Vector2D getNormal() {
	return normal.copy();
    }

    public Vector2D getTangent() {
	return tangent.copy();
    }

    public double getNormalPos() {
	return normalPos;
    }

    public double getTangentStart() {
	return tangentStart;
    }

    public double getTangentEnd() {
	return tangentEnd;
    }

    public double getTangentPosOf(final Vector2D point) {
	return tangent.dot(point);
    }

    public double getNormalPosOf(final Vector2D point) {
	return normal.dot(point);
    }

    public double getRelativeNormalPosOf(final Vector2D point) {
	return getNormalPosOf(point) - normalPos;
    }

    public Vector2D getClosestPointTo(final Vector2D point) {
	final double tangentPos = tangent.dot(point);
	final double closestTangentPos = new Interval(tangentStart, tangentEnd).clamp(tangentPos);
	return tangent.multiply(closestTangentPos).add(normal.multiply(normalPos));
    }

}
