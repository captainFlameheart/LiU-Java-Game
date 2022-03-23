package se.liu.jonla400.project.physics.collision.implementation;

import se.liu.jonla400.project.physics.main.Body;

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
