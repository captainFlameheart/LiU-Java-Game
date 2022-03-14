package se.liu.jonla400.restructure.physics.implementation.collision;

import se.liu.jonla400.restructure.physics.abstraction.main.Body;

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
