package se.liu.jonla400.project.physics.constraint.implementation;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.constraint.VelocityConstrainer;
import se.liu.jonla400.project.physics.main.Body;

/**
 * Seeks a certain velocity of a body, but is limited by a maximum force.
 * If the target velocity is set to zero, this simulates top down friction.
 */
public class VelocitySeeker implements VelocityConstrainer
{
    private Body body;
    private Vector2D targetVel;
    private double maxForce;

    private VelocitySeeker(final Body body, final Vector2D targetVel, final double maxForce) {
	this.body = body;
	this.targetVel = targetVel;
	this.maxForce = maxForce;
    }

    /**
     * Creates a VelocitySeeker for the given body and with the given maximum force used
     * to steer the velocity. The initial target velocity is set to 0, which causes
     * top down friction.
     *
     * @param body The body to steer the velocity of
     * @param maxForce The maximum force used when changing the angular velocity
     * @return The created VelocitySeeker
     */
    public static VelocitySeeker createStartingAsFriction(final Body body, final double maxForce) {
	return new VelocitySeeker(body, Vector2D.createZero(), maxForce);
    }

    /**
     * Sets the target velocity, but does not keep a reference to the input vector
     *
     * @param targetVel The new target velocity
     */
    public void setTargetVel(final Vector2D targetVel) {
	this.targetVel.set(targetVel);
    }

    /**
     * Generates a velocity constraint that applies an impulse to try to make the body's
     * velocity reach the target velocity. The impulse is however limited by the maximum
     * force and size of the time step after solving the constraint.
     *
     * @param deltaTime The size of the time step after solving the velocity constraint
     * @return The generated velocity constraint
     */
    @Override public ActiveVelocityConstraint generateConstraint(final double deltaTime) {
	final double maxImpulse = maxForce * deltaTime;
	final double mass = body.getMass();

	return new ActiveVelocityConstraint()
	{
	    private Vector2D impulse = Vector2D.createZero();

	    @Override public void updateImpulse() {
		final Vector2D vel = body.getVel();
		final Vector2D targetDeltaVel = targetVel.subtract(vel);
		final Vector2D targetDeltaImpulse = targetDeltaVel.multiply(mass);

		final Vector2D targetImpulse = impulse.add(targetDeltaImpulse);
		final Vector2D nextImpulse = targetImpulse.limitMagnitude(maxImpulse);
		final Vector2D deltaImpulse = nextImpulse.subtract(impulse);

		body.applyImpulse(deltaImpulse);
		impulse.set(nextImpulse);
	    }
	};
    }
}
