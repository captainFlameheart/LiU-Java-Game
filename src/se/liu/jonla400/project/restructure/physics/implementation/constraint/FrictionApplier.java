package se.liu.jonla400.project.restructure.physics.implementation.constraint;

import se.liu.jonla400.project.restructure.physics.abstraction.main.Body;
import se.liu.jonla400.project.restructure.physics.abstraction.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.project.restructure.physics.abstraction.constraint.VelocityConstrainer;

/**
 * Used to apply top-down friction by reducing both the speed and angular speed of
 * a point mass. The amount of friction is controlled by a maximum friction force and torque.
 */
public class FrictionApplier implements VelocityConstrainer
{
    private TranslationalFrictionApplier translationalFrictionApplier;
    private AngularFrictionApplier angularFrictionApplier;

    /**
     * Creates a new friction applier acting on the velocity and angular velocity of the given
     * point mass, restricted by the given maximum force and torque
     *
     * @param body The point mass to slow down
     * @param maxForce The maximum friction forced used to slow down the speed
     * @param maxTorque The maximum friction torque used to slow down the angular speed
     */
    public FrictionApplier(final Body body, final double maxForce, final double maxTorque) {
	translationalFrictionApplier = new TranslationalFrictionApplier(body, maxForce);
	angularFrictionApplier = new AngularFrictionApplier(body, maxTorque);
    }

    /**
     * Initializes a new ActiveVelocityConstraint used to slow down the point mass
     *
     * @param deltaTime The size of the upcoming time step
     * @return The new ActiveVelocityConstraint
     */
    @Override public ActiveVelocityConstraint generateConstraint(final double deltaTime) {
	ActiveVelocityConstraint translationalConstraint = translationalFrictionApplier.generateConstraint(deltaTime);
	ActiveVelocityConstraint angularConstraint = angularFrictionApplier.generateConstraint(deltaTime);

	// Return an active velocity constraint that does the following when its solution is updated
	return () -> {
	    translationalConstraint.updateImpulse();
	    angularConstraint.updateImpulse();
	};
    }
}
