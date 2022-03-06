package se.liu.jonla400.project.physics.constraints.types;

import se.liu.jonla400.project.math.Interval;
import se.liu.jonla400.project.physics.PointMass;
import se.liu.jonla400.project.physics.constraints.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.constraints.VelocityConstrainer;
import se.liu.jonla400.project.temp.ActiveImpulse;

/**
 * Used to apply top-down angular friction by reducing the angular speed of
 * a point mass. The amount of friction is controlled by a maximum friction torque.
 */
public class AngularFrictionApplier implements VelocityConstrainer
{
    private PointMass pointMass;
    private double maxTorque;

    /**
     * Creates a new angular friction applier acting on the angular velocity of the given
     * point mass, restricted by the given maximum torque
     *
     * @param pointMass The point mass whose angular speed to slow down
     * @param maxTorque The maximum friction torque
     */
    public AngularFrictionApplier(final PointMass pointMass, final double maxTorque) {
	if (maxTorque < 0) {
	    throw new IllegalArgumentException("Negative torque: " + maxTorque);
	}

	this.pointMass = pointMass;
	this.maxTorque = maxTorque;
    }

    /**
     * Initializes a new ActiveVelocityConstraint used to slow down the angular speed of the point mass
     *
     * @param deltaTime The size of the upcoming time step
     * @return The new ActiveVelocityConstraint
     */
    @Override public ActiveVelocityConstraint initActiveVelConstraint(final double deltaTime) {
	final double maxAngularImpulse = maxTorque * deltaTime;
	final Interval angularImpulseRange = new Interval(-maxAngularImpulse, maxAngularImpulse);

	return new ActiveVelocityConstraint()
	{
	    private ActiveImpulse angularImpulse = new ActiveImpulse();

	    @Override public void updateSolution() {
		final double targetDeltaAngularVel = -pointMass.getAngularVel();
		final double deltaAngularImpulse = angularImpulse.update(targetDeltaAngularVel, pointMass.getAngularMass(), angularImpulseRange);
		pointMass.applyAngularImpulse(deltaAngularImpulse);
	    }
	};
    }

}
