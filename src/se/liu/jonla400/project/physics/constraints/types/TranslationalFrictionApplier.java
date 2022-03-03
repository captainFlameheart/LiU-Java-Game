package se.liu.jonla400.project.physics.constraints.types;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.PointMass;
import se.liu.jonla400.project.physics.constraints.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.constraints.VelocityConstrainer;

/**
 * Used to apply top-down friction by reducing the speed (but not the angular speed) of
 * a point mass. The amount of friction is controlled by a maximum friction force.
 */
public class TranslationalFrictionApplier implements VelocityConstrainer
{
    private PointMass pointMass;
    private double maxForce;

    /**
     * Creates a new friction applier acting on the velocity of the given point mass, restricted
     * by the given maximum force
     *
     * @param pointMass The point mass to slow down
     * @param maxForce The maximum friction force
     */
    public TranslationalFrictionApplier(final PointMass pointMass, final double maxForce) {
	if (maxForce < 0) {
	    throw new IllegalArgumentException("Negative force: " + maxForce);
	}

	this.pointMass = pointMass;
	this.maxForce = maxForce;
    }

    /**
     * Initializes a new ActiveVelocityConstraint used to slow down the point mass
     *
     * @param deltaTime The size of the upcoming time step
     * @return The new ActiveVelocityConstraint
     */
    @Override public ActiveVelocityConstraint initActiveVelConstraint(final double deltaTime) {
	final double maxImpulse = maxForce * deltaTime;

	return new ActiveVelocityConstraint()
	{
	    Vector2D impulse = Vector2D.createZeroVector();

	    @Override public void updateSolution() {
		final Vector2D targetDeltaVel = pointMass.getVel().negate(); // We want to stop the point mass...
		final Vector2D targetDeltaImpulse = targetDeltaVel.multiply(pointMass.getMass());
		final Vector2D targetImpulse = impulse.add(targetDeltaImpulse);

		final Vector2D nextImpulse = targetImpulse.limitMagnitude(maxImpulse); // But we are limited by a maximum impulse
		final Vector2D deltaImpulse = nextImpulse.subtract(impulse);

		pointMass.applyImpulse(deltaImpulse);
		impulse.set(nextImpulse);
	    }
	};
    }
}
