package se.liu.jonla400.restructure.physics.implementation.collision;

import se.liu.jonla400.restructure.physics.abstraction.main.Body;

public class CustomCollider<T>
{
    private Body body;
    private TranslatedCustomShape<T> shape;

    public CustomCollider(final Body body, final TranslatedCustomShape<T> customShape) {
	this.body = body;
	this.shape = customShape;
    }

    public Body getBody() {
	return body;
    }

    public TranslatedCustomShape<T> getShape() {
	return shape;
    }
}
