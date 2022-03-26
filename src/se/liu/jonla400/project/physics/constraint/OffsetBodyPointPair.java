package se.liu.jonla400.project.physics.constraint;

import se.liu.jonla400.project.math.Matrix22;
import se.liu.jonla400.project.math.Vector2D;

/**
 * Represents a pair of {@link OffsetBodyPoint}s. The velocity of this pair is considered to be the
 * first point's velocity relative to the second point's velocity. An impulse can be applied to this
 * pair, which means that an impulse will be applied to the first point and an impulse with the same
 * magnitude but opposite direction will be applied to the second point. This follows Newton's third
 * law of motion. This body point pair also has an inverted mass matrix M such that
 * M * λ = Δv where λ is an arbitrary impulse applied to the pair and Δv is the pair's change in
 * velocity that the impulse causes.
 */
public class OffsetBodyPointPair
{
    private OffsetBodyPoint bodyPointA;
    private OffsetBodyPoint bodyPointB;

    public OffsetBodyPointPair(final OffsetBodyPoint bodyPointA, final OffsetBodyPoint bodyPointB) {
	this.bodyPointA = bodyPointA;
	this.bodyPointB = bodyPointB;
    }

    /**
     * @return The first point's velocity relative to the second point's velocity
     */
    public Vector2D getVel() {
	return bodyPointA.getVel().subtract(bodyPointB.getVel());
    }

    /**
     * @return The inverted mass, specifying the relation between an arbitrary impulse and the change in velocity
     */
    public Matrix22 getInvertedMass() {
	// See derivation at doc/math/pairInvertedMassDerivation.jpg
	return bodyPointA.getInvertedMass().add(bodyPointB.getInvertedMass());
    }

    /**
     * Applies the given impulse to this pair by applying the impulse to the first point and the negated
     * impulse to the second point
     *
     * @param impulse The impulse to apply
     */
    public void applyImpulse(final Vector2D impulse) {
	bodyPointA.applyImpulse(impulse);
	bodyPointB.applyImpulse(impulse.negate());
    }
}
