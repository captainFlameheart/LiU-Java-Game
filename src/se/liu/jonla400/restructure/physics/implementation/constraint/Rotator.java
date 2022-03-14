package se.liu.jonla400.restructure.physics.implementation.constraint;

import se.liu.jonla400.restructure.math.Vector2D;
import se.liu.jonla400.restructure.physics.abstraction.constraint.ActiveIterativeVelocityConstraint;
import se.liu.jonla400.restructure.physics.abstraction.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.restructure.physics.abstraction.constraint.VelocityConstrainer;
import se.liu.jonla400.restructure.physics.abstraction.main.Body;

import java.util.List;

public class Rotator implements VelocityConstrainer
{
    private Pin pin;

    private Vector2D leverDir;
    private double rotationPointAlongLever;
    private Vector2D targetGlobalPoint;
    private double maxForce;

    private int iterations;

    private Rotator(final Pin pin, final Vector2D leverDir, final double rotationPointAlongLever, final Vector2D targetGlobalPoint,
		    final double maxForce, final int iterations)
    {
	this.pin = pin;
	this.leverDir = leverDir;
	this.rotationPointAlongLever = rotationPointAlongLever;
	this.targetGlobalPoint = targetGlobalPoint;
	this.maxForce = maxForce;
	this.iterations = iterations;
    }

    public static Rotator createAtGlobalRotationPoint(
	    final Body body, final Vector2D globalRotationPoint, final double targetPosCorrectionPerTick,
	    final Vector2D leverDir, final double maxForce)
    {
	final Pin pin = Pin.createAtGlobalPoint(body, globalRotationPoint, targetPosCorrectionPerTick);
	final Vector2D normalizedLeverDir = leverDir.normalize();
	final double rotationPointAlongLever = normalizedLeverDir.dot(pin.getPinnedLocalPoint());
	final Vector2D targetGlobalPoint = globalRotationPoint.copy();
	final int iterations = 1;
	return new Rotator(pin, normalizedLeverDir, rotationPointAlongLever, targetGlobalPoint, maxForce, iterations);
    }

    public void setTargetGlobalPoint(final Vector2D targetGlobalPoint) {
	this.targetGlobalPoint.set(targetGlobalPoint);
    }

    @Override public ActiveVelocityConstraint generateConstraint(final double deltaTime) {
	final ActiveVelocityConstraint pinConstraint = pin.generateConstraint(deltaTime);
	final ActiveVelocityConstraint leverConstraint = generateLeverConstraint(deltaTime);

	return new ActiveIterativeVelocityConstraint(iterations, List.of(pinConstraint, leverConstraint));
    }

    private ActiveVelocityConstraint generateLeverConstraint(final double deltaTime) {
	final Body body = pin.getBody();
	final Vector2D localRotationPoint = pin.getPinnedLocalPoint();

	final Vector2D targetLocalPoint = body.convertGlobalPointToLocalPoint(targetGlobalPoint);
	final double targetPointAlongLever = leverDir.dot(targetLocalPoint);
	final double leverLength = targetPointAlongLever - rotationPointAlongLever;
	final Vector2D pulledLocalPoint = localRotationPoint.add(leverDir.multiply(leverLength));

	final PointSeeker
		leverEndToTargetSeeker = new PointSeeker(body, pulledLocalPoint, targetGlobalPoint, 1, maxForce);
	return leverEndToTargetSeeker.generateConstraint(deltaTime);
    }
}
