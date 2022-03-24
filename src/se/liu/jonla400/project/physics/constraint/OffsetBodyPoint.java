package se.liu.jonla400.project.physics.constraint;

import se.liu.jonla400.project.math.Matrix22;
import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.main.Body;

/**
 * Represents a point on a {@link Body} offset from its position. Since the point is
 * offset from the body's position, the point has a velocity that depends on both the velocity and
 * angular velocity of the body. It also has a 2-by-2 inverted mass matrix M such that
 * M * λ = Δv where λ is an arbitrary impulse applied to the point and Δv is the point's change in
 * velocity that the impulse causes.
 */
public class OffsetBodyPoint
{
    private Body body;
    private Vector2D offset;

    private OffsetBodyPoint(final Body body, final Vector2D offset) {
	this.body = body;
	this.offset = offset;
    }

    /**
     * Creates a new OffsetBodyPoint on the given body with the given offset from the body's position.
     * No reference to the given offset vector is kept.
     *
     * @param body The body
     * @param offset The offset from the body's position
     * @return The created OffsetBodyPoint
     */
    public static OffsetBodyPoint copyOffset(final Body body, final Vector2D offset) {
	return new OffsetBodyPoint(body, offset.copy());
    }

    /**
     * @return A read-only view of the velocity
     */
    public Vector2D getVel() {
	return body.getVelAt(offset);
    }

    /**
     * @return The inverted mass, specifying the relation between an arbitrary impulse and the change in velocity
     */
    public Matrix22 getInvertedMass() {
	return body.getInvertedMassAt(offset);
    }

    /**
     * Applies the given impulse to this point, which changes the body's velocity and angular velocity
     *
     * @param impulse The impulse to apply
     */
    public void applyImpulse(final Vector2D impulse) {
	body.applyOffsetImpulse(offset, impulse);
    }
}
