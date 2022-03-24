package se.liu.jonla400.project.physics.collision.implementation;

import se.liu.jonla400.project.physics.main.Body;

/**
 * Represents a body with an attached circle with a given radius. This is used by {@link CircleVsCustomCollisionDetector}
 * to detect collisions.
 */
public class CircleCollider
{
    private Body body;
    private double radius;

    public CircleCollider(final Body body, final double radius) {
	this.body = body;
	this.radius = radius;
    }

    public Body getBody() {
	return body;
    }

    public double getRadius() {
	return radius;
    }
}
