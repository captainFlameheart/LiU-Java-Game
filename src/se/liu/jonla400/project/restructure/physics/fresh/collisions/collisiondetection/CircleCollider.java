package se.liu.jonla400.project.restructure.physics.fresh.collisions.collisiondetection;

import se.liu.jonla400.project.restructure.physics.Body;

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
