package se.liu.jonla400.project.physics.constraint;

import se.liu.jonla400.project.math.Matrix22;
import se.liu.jonla400.project.math.Vector2D;

public class OffsetBodyPointPair
{
    private OffsetBodyPoint bodyPointA;
    private OffsetBodyPoint bodyPointB;

    public OffsetBodyPointPair(final OffsetBodyPoint bodyPointA, final OffsetBodyPoint bodyPointB) {
	this.bodyPointA = bodyPointA;
	this.bodyPointB = bodyPointB;
    }

    public Vector2D getVel() {
	return bodyPointA.getVel().subtract(bodyPointB.getVel());
    }

    public Matrix22 getInvertedMass() {
	return bodyPointA.getInvertedMass().add(bodyPointB.getInvertedMass());
    }

    public void applyImpulse(final Vector2D impulse) {
	bodyPointA.applyImpulse(impulse);
	bodyPointB.applyImpulse(impulse.negate());
    }
}
