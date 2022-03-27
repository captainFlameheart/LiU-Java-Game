package se.liu.jonla400.project.physics.collision.implementation;

import se.liu.jonla400.project.physics.main.Body;

/**
 * Represents a {@link Body} with an attached {@link TranslatedCustomShape}. This is used by
 * {@link CircleVsCustomCollisionDetector} to detect collisions. Each line segment is bound
 * to custom user data. The shape is considered to be in the body's local space, and the
 * shape's translation is thus also a local vector.
 *
 * @param <T> The type of user data associated with each line segment
 */
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
