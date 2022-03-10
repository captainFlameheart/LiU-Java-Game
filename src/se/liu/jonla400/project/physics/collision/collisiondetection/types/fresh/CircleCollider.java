package se.liu.jonla400.project.physics.collision.collisiondetection.types.fresh;

import se.liu.jonla400.project.physics.PointMass;

public class CircleCollider
{
    private PointMass pointMass;
    private double radius;

    public CircleCollider(final PointMass pointMass, final double radius) {
	this.pointMass = pointMass;
	this.radius = radius;
    }

    public PointMass getPointMass() {
	return pointMass;
    }

    public double getRadius() {
	return radius;
    }
}
