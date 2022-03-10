package se.liu.jonla400.project.restructure.physics.constraints.types;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.restructure.physics.Body;
import se.liu.jonla400.project.restructure.physics.constraints.ActiveVelocityConstraint;
import se.liu.jonla400.project.restructure.physics.constraints.VelocityConstrainer;

/**
 * Used to apply top-down friction by reducing the speed (but not the angular speed) of
 * a point mass. The amount of friction is controlled by a maximum friction force.
 */
public class TranslationalFrictionApplier implements VelocityConstrainer
{
    private Body body;
    private double maxForce;

    /**
     * Creates a new friction applier acting on the velocity of the given point mass, restricted
     * by the given maximum force
     *
     * @param body The point mass to slow down
     * @param maxForce The maximum friction force
     */
    public TranslationalFrictionApplier(final Body body, final double maxForce) {
	if (maxForce < 0) {
	    throw new IllegalArgumentException("Negative force: " + maxForce);
	}

	this.body = body;
	this.maxForce = maxForce;
    }

    /**
     * Initializes a new ActiveVelocityConstraint used to slow down the point mass
     *
     * @param deltaTime The size of the upcoming time step
     * @return The new ActiveVelocityConstraint
     */
    @Override public ActiveVelocityConstraint generateConstraint(final double deltaTime) {
	final double maxImpulse = maxForce * deltaTime;

	return new ActiveVelocityConstraint()
	{
	    private Vector2D impulse = Vector2D.createZeroVector();

	    @Override public void updateImpulse() {
		final Vector2D targetDeltaVel = body.getVel().negate(); // We want to stop the point mass...
		final Vector2D targetDeltaImpulse = targetDeltaVel.multiply(body.getMass());
		final Vector2D targetImpulse = impulse.add(targetDeltaImpulse);

		final Vector2D nextImpulse = targetImpulse.limitMagnitude(maxImpulse); // But we are limited by a maximum impulse
		final Vector2D deltaImpulse = nextImpulse.subtract(impulse);

		body.applyImpulse(deltaImpulse);
		impulse.set(nextImpulse);
	    }
	};
    }
}
