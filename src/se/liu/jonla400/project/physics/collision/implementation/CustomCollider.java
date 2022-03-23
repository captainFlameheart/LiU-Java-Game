package se.liu.jonla400.project.physics.collision.implementation;

import se.liu.jonla400.project.physics.main.Body;

public class CustomCollider<T>
{
    private Body body;
    private TranslatedCustomShape<T> shape;

    public CustomCollider(final Body body, final TranslatedCustomShape<T> shape) {
	this.body = body;
	this.shape = shape;
    }

    public Body getBody() {
	return body;
    }

    public TranslatedCustomShape<T> getShape() {
	return shape;
    }
}
