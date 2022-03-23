package se.liu.jonla400.project.physics.constraint;

import se.liu.jonla400.project.math.Matrix22;
import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.main.Body;

public class OffsetBodyPoint
{
    private Body body;
    private Vector2D offset;

    private OffsetBodyPoint(final Body body, final Vector2D offset) {
	this.body = body;
	this.offset = offset;
    }

    public static OffsetBodyPoint copyOffset(final Body body, final Vector2D offset) {
	return new OffsetBodyPoint(body, offset.copy());
    }

    public Vector2D getVel() {
	return body.getVelAt(offset);
    }

    public Matrix22 getInvertedMass() {
	return body.getInvertedMassAt(offset);
    }

    public void applyImpulse(final Vector2D impulse) {
	body.applyOffsetImpulse(offset, impulse);
    }
}
