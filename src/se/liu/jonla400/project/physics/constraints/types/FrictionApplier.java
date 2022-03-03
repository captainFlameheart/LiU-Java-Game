package se.liu.jonla400.project.physics.constraints.types;

import se.liu.jonla400.project.physics.PointMass;
import se.liu.jonla400.project.physics.constraints.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.constraints.VelocityConstrainer;

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
     * @param pointMass The point mass to slow down
     * @param maxForce The maximum friction forced used to slow down the speed
     * @param maxTorque The maximum friction torque used to slow down the angular speed
     */
    public FrictionApplier(final PointMass pointMass, final double maxForce, final double maxTorque) {
	translationalFrictionApplier = new TranslationalFrictionApplier(pointMass, maxForce);
	angularFrictionApplier = new AngularFrictionApplier(pointMass, maxTorque);
    }

    /**
     * Initializes a new ActiveVelocityConstraint used to slow down the point mass
     *
     * @param deltaTime The size of the upcoming time step
     * @return The new ActiveVelocityConstraint
     */
    @Override public ActiveVelocityConstraint initActiveVelConstraint(final double deltaTime) {
	ActiveVelocityConstraint translationalConstraint = translationalFrictionApplier.initActiveVelConstraint(deltaTime);
	ActiveVelocityConstraint angularConstraint = angularFrictionApplier.initActiveVelConstraint(deltaTime);

	// Return an active velocity constraint that does the following when its solution is updated
	return () -> {
	    translationalConstraint.updateSolution();
	    angularConstraint.updateSolution();
	};
    }
}
