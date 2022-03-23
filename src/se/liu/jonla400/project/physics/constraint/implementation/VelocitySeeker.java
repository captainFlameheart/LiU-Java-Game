package se.liu.jonla400.project.physics.constraint.implementation;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.constraint.VelocityConstrainer;
import se.liu.jonla400.project.physics.main.Body;

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

    public static VelocitySeeker createStartingAsFriction(final Body body, final double maxForce) {
	return new VelocitySeeker(body, Vector2D.createZero(), maxForce);
    }

    public void setTargetVel(final Vector2D targetVel) {
	this.targetVel.set(targetVel);
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
