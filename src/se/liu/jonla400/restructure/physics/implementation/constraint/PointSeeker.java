package se.liu.jonla400.restructure.physics.implementation.constraint;

import se.liu.jonla400.restructure.math.Vector2D;
import se.liu.jonla400.restructure.physics.abstraction.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.restructure.physics.abstraction.constraint.VelocityConstrainer;
import se.liu.jonla400.restructure.physics.abstraction.main.Body;

public class PointSeeker implements VelocityConstrainer
{
    private Body body;
    private Vector2D localPoint;
    private Vector2D targetGlobalPoint;
    private double targetPosCorrectionPerTick;
    private double maxForce;
    private boolean onlyTranslates;

    public PointSeeker(final Body body, final Vector2D localPoint, final Vector2D targetGlobalPoint,
		       final double targetPosCorrectionPerTick, final double maxForce, final boolean onlyTranslates)
    {
	this.body = body;
	this.localPoint = localPoint;
	this.targetGlobalPoint = targetGlobalPoint;
	this.targetPosCorrectionPerTick = targetPosCorrectionPerTick;
	this.maxForce = maxForce;
	this.onlyTranslates = onlyTranslates;
    }

    public PointSeeker(final Body body, final Vector2D localPoint, final Vector2D targetGlobalPoint,
		       final double targetPosCorrectionPerTick, final double maxForce)
    {
	this(body, localPoint, targetGlobalPoint, targetPosCorrectionPerTick, maxForce, false);
    }

    @Override public ActiveVelocityConstraint generateConstraint(final double deltaTime) {
	final Vector2D pointOffset = body.convertLocalVectorToGlobalVector(localPoint);
	final Vector2D steeredPointOffset = onlyTranslates ? Vector2D.createZero() : pointOffset;

	final Vector2D globalPoint = body.convertOffsetToGlobalPoint(pointOffset);
	final Vector2D pointToTarget = targetGlobalPoint.subtract(globalPoint);
	final Vector2D targetVel = pointToTarget.multiply(targetPosCorrectionPerTick / deltaTime);

	final OffsetVelocitySeeker
		velSeeker = new OffsetVelocitySeeker(body, steeredPointOffset, targetVel, maxForce);
	return velSeeker.generateConstraint(deltaTime);
    }

}
