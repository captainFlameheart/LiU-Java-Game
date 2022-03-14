package se.liu.jonla400.project.physics.implementation.constraint;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.abstraction.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.abstraction.constraint.VelocityConstrainer;
import se.liu.jonla400.project.physics.abstraction.main.Body;

public class Translator implements VelocityConstrainer
{
    private Body body;
    private Vector2D pulledLocalPoint;
    private Vector2D globalPointPulledTowards;
    private double maxForce;

    public Translator(final Body body, final Vector2D pulledLocalPoint,
		      final Vector2D globalPointPulledTowards, final double maxForce)
    {
	this.body = body;
	this.pulledLocalPoint = pulledLocalPoint.copy();
	this.globalPointPulledTowards = globalPointPulledTowards.copy();
	this.maxForce = maxForce;
    }

    public static Translator createAtLocalPoint(final Body body, final Vector2D pulledLocalPoint, final double maxForce) {
	return new Translator(body, pulledLocalPoint, body.convertLocalPointToGlobalPoint(pulledLocalPoint), maxForce);
    }

    public static Translator createAtOffset(final Body body, final Vector2D offset, final double maxForce) {
	return new Translator(body, body.convertOffsetToLocalPoint(offset), body.convertOffsetToGlobalPoint(offset), maxForce);
    }

    public static Translator createAtGlobalPoint(final Body body, final Vector2D globalPoint, final double maxForce) {
	return new Translator(body, body.convertGlobalPointToLocalPoint(globalPoint), globalPoint, maxForce);
    }

    public Body getBody() {
	return body;
    }

    public void setBody(final Body body) {
	this.body = body;
    }

    public Vector2D getPulledLocalPoint() {
	return pulledLocalPoint.copy();
    }

    public void setPulledLocalPoint(final Vector2D pulledLocalPoint) {
	this.pulledLocalPoint = pulledLocalPoint.copy();
    }

    public Vector2D getGlobalPointPulledTowards() {
	return globalPointPulledTowards.copy();
    }

    public void setGlobalPointPulledTowards(final Vector2D globalPointPulledTowards) {
	this.globalPointPulledTowards = globalPointPulledTowards.copy();
    }

    public double getMaxForce() {
	return maxForce;
    }

    public void setMaxForce(final double maxForce) {
	this.maxForce = maxForce;
    }

    @Override public ActiveVelocityConstraint generateConstraint(final double deltaTime) {
	final double targetPosCorrectionPerTick = 1;
	final boolean onlyTranslates = true;
	final PointSeeker pointSeeker = new PointSeeker(
		body, pulledLocalPoint, globalPointPulledTowards, targetPosCorrectionPerTick, maxForce, onlyTranslates
	);
	return pointSeeker.generateConstraint(deltaTime);
    }
}
