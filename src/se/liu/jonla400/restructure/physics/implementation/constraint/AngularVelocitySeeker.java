package se.liu.jonla400.restructure.physics.implementation.constraint;

import se.liu.jonla400.restructure.math.Interval;
import se.liu.jonla400.restructure.physics.abstraction.constraint.ActiveImpulse1D;
import se.liu.jonla400.restructure.physics.abstraction.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.restructure.physics.abstraction.constraint.VelocityConstrainer;
import se.liu.jonla400.restructure.physics.abstraction.main.Body;

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

    public Body getBody() {
	return body;
    }

    public void setBody(final Body body) {
	this.body = body;
    }

    public double getTargetAngularVel() {
	return targetAngularVel;
    }

    public void setTargetAngularVel(final double targetAngularVel) {
	this.targetAngularVel = targetAngularVel;
    }

    public double getMaxTorque() {
	return maxTorque;
    }

    public void setMaxTorque(final double maxTorque) {
	this.maxTorque = maxTorque;
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
