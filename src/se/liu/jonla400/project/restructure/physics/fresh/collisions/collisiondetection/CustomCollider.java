package se.liu.jonla400.project.restructure.physics.fresh.collisions.collisiondetection;

import se.liu.jonla400.project.restructure.physics.Body;

public class CustomCollider
{
    private Body body;
    private CustomShape shape;

    public CustomCollider(final Body body, final CustomShape customShape) {
	this.body = body;
	this.shape = customShape;
    }

    public Body getBody() {
	return body;
    }

    public CustomShape getShape() {
	return shape;
    }
}
