package se.liu.jonla400.restructure.physics.implementation.collision;

import se.liu.jonla400.restructure.physics.abstraction.main.Body;

public class CustomCollider
{
    private Body body;
    private TranslatedCustomShape shape;

    public CustomCollider(final Body body, final TranslatedCustomShape customShape) {
	this.body = body;
	this.shape = customShape;
    }

    public Body getBody() {
	return body;
    }

    public TranslatedCustomShape getShape() {
	return shape;
    }
}
