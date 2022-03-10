package se.liu.jonla400.project.restructure.physics;

import se.liu.jonla400.project.restructure.physics.constraints.VelocityConstrainer;
import se.liu.jonla400.project.restructure.physics.timestepping.TimeStepper;

public class PhysicsWorld implements TimeStepper
{
    private TimeStepper timeStepper;
    private VelocityConstrainer velConstrainer;

    public PhysicsWorld(final TimeStepper timeStepper, final VelocityConstrainer velConstrainer) {
	this.timeStepper = timeStepper;
	this.velConstrainer = velConstrainer;
    }

    @Override public void tick(final double deltaTime) {
	velConstrainer.generateConstraint(deltaTime).updateImpulse();
	timeStepper.tick(deltaTime);
    }
}
