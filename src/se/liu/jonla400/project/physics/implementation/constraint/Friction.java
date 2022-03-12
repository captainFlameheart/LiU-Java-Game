package se.liu.jonla400.project.physics.implementation.constraint;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.abstraction.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.abstraction.constraint.VelocityConstrainer;
import se.liu.jonla400.project.physics.abstraction.main.Body;

public class Friction implements VelocityConstrainer
{
    private Body body;
    private double maxForce;
    private double maxTorque;

    public Friction(final Body body, final double maxForce, final double maxTorque) {
	this.body = body;
	this.maxForce = maxForce;
	this.maxTorque = maxTorque;
    }

    public static Friction createForTranslation(final Body body, final double maxForce) {
	return new Friction(body, maxForce, 0);
    }

    public static Friction createForAngle(final Body body, final double maxTorque) {
	return new Friction(body, 0, maxTorque);
    }

    public Body getBody() {
	return body;
    }

    public void setBody(final Body body) {
	this.body = body;
    }

    public double getMaxForce() {
	return maxForce;
    }

    public void setMaxForce(final double maxForce) {
	this.maxForce = maxForce;
    }

    public double getMaxTorque() {
	return maxTorque;
    }

    public void setMaxTorque(final double maxTorque) {
	this.maxTorque = maxTorque;
    }

    @Override public ActiveVelocityConstraint generateConstraint(final double deltaTime) {
	final Vector2D offset = Vector2D.createZeroVector();
	final Vector2D targetVel = Vector2D.createZeroVector();
	final OffsetVelocitySeeker translationFriction = new OffsetVelocitySeeker(body, offset, targetVel, maxForce);
	final ActiveVelocityConstraint activeTranslationFriction = translationFriction.generateConstraint(deltaTime);

	final AngleFriction angleFriction = new AngleFriction(body, maxTorque);
	final ActiveVelocityConstraint activeAngleFriction = angleFriction.generateConstraint(deltaTime);

	return () -> {
	    activeTranslationFriction.updateImpulse();
	    activeAngleFriction.updateImpulse();
	};
    }
}
