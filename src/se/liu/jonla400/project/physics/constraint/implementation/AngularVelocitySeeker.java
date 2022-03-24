package se.liu.jonla400.project.physics.constraint.implementation;

import se.liu.jonla400.project.math.Interval;
import se.liu.jonla400.project.physics.constraint.ActiveImpulse1D;
import se.liu.jonla400.project.physics.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.constraint.VelocityConstrainer;
import se.liu.jonla400.project.physics.main.Body;

/**
 * Seeks a certain angular velocity of a body, but is limited by a maximum torque (which is the angular
 * equivalent to a force). If the target angular velocity is set to zero, this simulates top down
 * angular friction.
 */
public class AngularVelocitySeeker implements VelocityConstrainer
{
    private Body body;
    private double targetAngularVel;
    private double maxTorque;

    private AngularVelocitySeeker(final Body body, final double targetAngularVel, final double maxTorque) {
	this.body = body;
	this.targetAngularVel = targetAngularVel;
	this.maxTorque = maxTorque;
    }

    /**
     * Creates an AngularVelocitySeeker for the given body and with the given maximum torque used
     * to steer the angular velocity. The initial target angular velocity is set to 0, which causes
     * top down angular friction.
     *
     * @param body The body to steer the angular velocity of
     * @param maxTorque The maximum torque used when changing the angular velocity
     * @return The created AngularVelocitySeeker
     */
    public static AngularVelocitySeeker createStartingAsAngularFriction(final Body body, final double maxTorque) {
	return new AngularVelocitySeeker(body, 0, maxTorque);
    }

    public void setTargetAngularVel(final double targetAngularVel) {
	this.targetAngularVel = targetAngularVel;
    }

    /**
     * Generates a velocity constraint that applies an angular impulse to try to make the body's
     * angular velocity reach the target angular velocity. The impulse is however limited by the maximum
     * torque and size of the time step after solving the constraint.
     *
     * @param deltaTime The size of the time step after solving the velocity constraint
     * @return The generated velocity constraint
     */
    @Override public ActiveVelocityConstraint generateConstraint(final double deltaTime) {
	final double maxAngularImpulse = maxTorque * deltaTime;
	final Interval angularImpulseRange = new Interval(-maxAngularImpulse, maxAngularImpulse);
	final double angularMass = body.getAngularMass();

	return new ActiveVelocityConstraint()
	{
	    private ActiveImpulse1D angularImpulse = new ActiveImpulse1D();

	    @Override public void updateImpulse() {
		final double targetDeltaAngularVel = targetAngularVel - body.getAngularVel();
		final double deltaAngularImpulse = angularImpulse.update(targetDeltaAngularVel, angularMass, angularImpulseRange);
		body.applyAngularImpulse(deltaAngularImpulse);
	    }
	};
    }
}
