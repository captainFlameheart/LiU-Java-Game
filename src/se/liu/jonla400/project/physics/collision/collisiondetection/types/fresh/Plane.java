package se.liu.jonla400.project.physics.collision.collisiondetection.types.fresh;

import se.liu.jonla400.project.math.Vector2D;

public class Plane
{
    private Vector2D normal;
    private Vector2D tangent;
    private double offset;

    public Plane(final Vector2D normal, final double offset) {
	this.normal = normal.copy();
	this.tangent = normal.rotate90Degrees(Vector2D.RotationDirection.Y_TO_X);
	this.offset = offset;
    }

    public Vector2D getNormal() {
	return normal.copy();
    }

    public Vector2D getTangent() {
	return tangent.copy();
    }

    public double getOffset() {
	return offset;
    }

    public double getSeparationOf(final Vector2D point) {
	final double normalPos = normal.dot(point);
	return normalPos - offset;
    }
}
