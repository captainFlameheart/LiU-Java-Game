package se.liu.jonla400.project.physics.implementation.constraint;

import se.liu.jonla400.project.math.Interval;
import se.liu.jonla400.project.physics.abstraction.main.Body;
import se.liu.jonla400.project.physics.abstraction.constraint.ActiveImpulse1D;
import se.liu.jonla400.project.physics.abstraction.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.abstraction.constraint.VelocityConstrainer;


/**
 * Used to apply top-down angular friction by reducing the angular speed of
 * a point mass. The amount of friction is controlled by a maximum friction torque.
 */
public class AngleFriction implements VelocityConstrainer
{
    private Body body;
    private double maxTorque;

    /**
     * Creates a new angular friction applier acting on the angular velocity of the given
     * point mass, restricted by the given maximum torque
     *
     * @param body The point mass whose angular speed to slow down
     * @param maxTorque The maximum friction torque
     */
    public AngleFriction(final Body body, final double maxTorque) {
	if (maxTorque < 0) {
	    throw new IllegalArgumentException("Negative torque: " + maxTorque);
	}

	this.body = body;
	this.maxTorque = maxTorque;
    }

    /**
     * Initializes a new ActiveVelocityConstraint used to slow down the angular speed of the point mass
     *
     * @param deltaTime The size of the upcoming time step
     * @return The new ActiveVelocityConstraint
     */
    @Override public ActiveVelocityConstraint generateConstraint(final double deltaTime) {
	final double maxAngularImpulse = maxTorque * deltaTime;
	final Interval angularImpulseRange = new Interval(-maxAngularImpulse, maxAngularImpulse);
	final double angularMass = body.getAngularMass();

	return new ActiveVelocityConstraint()
	{
	    private ActiveImpulse1D angularImpulse = new ActiveImpulse1D();

	    @Override public void updateImpulse() {
		final double targetDeltaAngularVel = -body.getAngularVel();
		final double deltaAngularImpulse = angularImpulse.update(targetDeltaAngularVel, angularMass, angularImpulseRange);
		body.applyAngularImpulse(deltaAngularImpulse);
	    }
	};
    }
}
