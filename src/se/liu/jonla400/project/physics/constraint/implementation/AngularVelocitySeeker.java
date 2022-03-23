package se.liu.jonla400.project.physics.constraint.implementation;

import se.liu.jonla400.project.math.Interval;
import se.liu.jonla400.project.physics.constraint.ActiveImpulse1D;
import se.liu.jonla400.project.physics.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.constraint.VelocityConstrainer;
import se.liu.jonla400.project.physics.main.Body;

public class AngularVelocitySeeker implements VelocityConstrainer
{
    private Body body;
    private double targetAngularVel;
    private double maxTorque;

    public AngularVelocitySeeker(final Body body, final double targetAngularVel, final double maxTorque) {
	this.body = body;
	this.targetAngularVel = targetAngularVel;
	this.maxTorque = maxTorque;
    }

    public static AngularVelocitySeeker createStartingAsAngularFriction(final Body body, final double maxTorque) {
	return new AngularVelocitySeeker(body, 0, maxTorque);
    }

    public void setTargetAngularVel(final double targetAngularVel) {
	this.targetAngularVel = targetAngularVel;
    }

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
