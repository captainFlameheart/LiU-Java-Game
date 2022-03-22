package se.liu.jonla400.project.physics.implementation.constraint;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.abstraction.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.abstraction.constraint.VelocityConstrainer;
import se.liu.jonla400.project.physics.abstraction.main.Body;

public class VelocitySeeker implements VelocityConstrainer
{
    private Body body;
    private Vector2D targetVel;
    private double maxForce;

    public VelocitySeeker(final Body body, final Vector2D targetVel, final double maxForce) {
	this.body = body;
	this.targetVel = targetVel;
	this.maxForce = maxForce;
    }

    public void setTargetVel(final Vector2D targetVel) {
	this.targetVel = targetVel;
    }

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
