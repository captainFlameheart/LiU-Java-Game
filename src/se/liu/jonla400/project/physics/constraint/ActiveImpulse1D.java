package se.liu.jonla400.project.physics.constraint;

import se.liu.jonla400.project.math.Interval;

/**
 * Represents an active 1D impulse that can be updated in order to try to achieve a given
 * change in velocity of some 1D system while being restricted by a given impulse range.
 * This is used by {@link ActiveVelocityConstraint}s to keep track of the impulse currently
 * being applied to one or more bodies.
 */
public class ActiveImpulse1D
{
    private double impulse;

    /**
     * Creates an ActiveImpulse1D initially set to 0
     */
    public ActiveImpulse1D() {
	impulse = 0;
    }

    /**
     * @return Gets the value of the current impulse being applied
     */
    public double get() {
	return impulse;
    }

    /**
     * Updates the current impulse by trying to reach the given targeted change in velocity.
     * The total impulse is however limited by the impulse range. This method returns the change
     * of the total impulse.
     *
     * @param targetDeltaVel The target delta velocity of the "1D system"
     * @param mass The mass of the "1D system"
     * @param impulseRange The range that the total impulse must reside in
     * @return The change of current impulse
     */
    public double update(final double targetDeltaVel, final double mass, final Interval impulseRange) {
	final double targetDeltaImpulse = targetDeltaVel * mass;
	final double targetImpulse = impulse + targetDeltaImpulse;

	final double nextImpulse = impulseRange.clamp(targetImpulse);
	final double deltaImpulse = nextImpulse - impulse;
	impulse = nextImpulse;

	return deltaImpulse;
    }
}
