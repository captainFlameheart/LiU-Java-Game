package se.liu.jonla400.restructure.physics.implementation.constraint;

import se.liu.jonla400.restructure.math.Vector2D;
import se.liu.jonla400.restructure.physics.abstraction.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.restructure.physics.abstraction.constraint.VelocityConstrainer;
import se.liu.jonla400.restructure.physics.abstraction.main.Body;

public class Pin implements VelocityConstrainer
{
    private Body body;
    private Vector2D pinnedLocalPoint;
    private Vector2D globalPointPinnedTo;
    private double targetPosCorrectionPerTick;

    public Pin(final Body body, final Vector2D pinnedLocalPoint, final Vector2D globalPointPinnedTo,
	       final double targetPosCorrectionPerTick)
    {
	this.body = body;
	this.pinnedLocalPoint = pinnedLocalPoint.copy();
	this.globalPointPinnedTo = globalPointPinnedTo.copy();
	this.targetPosCorrectionPerTick = targetPosCorrectionPerTick;
    }

    public static Pin createAtLocalPoint(final Body body, final Vector2D pinnedLocalPoint, final double targetPosCorrectionPerTick) {
	return new Pin(body, pinnedLocalPoint, body.convertLocalPointToGlobalPoint(pinnedLocalPoint), targetPosCorrectionPerTick);
    }

    public static Pin createAtOffset(final Body body, final Vector2D pinnedPointOffset, final double targetPosCorrectionPerTick) {
	return new Pin(body, body.convertOffsetToLocalPoint(pinnedPointOffset),
		       body.convertOffsetToGlobalPoint(pinnedPointOffset), targetPosCorrectionPerTick);
    }

    public static Pin createAtGlobalPoint(final Body body, final Vector2D globalPointPinnedTo, final double targetPosCorrectionPerTick) {
	return new Pin(body, body.convertGlobalPointToLocalPoint(globalPointPinnedTo), globalPointPinnedTo, targetPosCorrectionPerTick);
    }

    public Body getBody() {
	return body;
    }

    public void setBody(final Body body) {
	this.body = body;
    }

    public Vector2D getPinnedLocalPoint() {
	return pinnedLocalPoint;
    }

    public void setPinnedLocalPoint(final Vector2D pinnedLocalPoint) {
	this.pinnedLocalPoint = pinnedLocalPoint.copy();
    }

    public Vector2D getGlobalPointPinnedTo() {
	return globalPointPinnedTo;
    }

    public void setGlobalPointPinnedTo(final Vector2D globalPointPinnedTo) {
	this.globalPointPinnedTo = globalPointPinnedTo.copy();
    }

    public double getTargetPosCorrectionPerTick() {
	return targetPosCorrectionPerTick;
    }

    public void setTargetPosCorrectionPerTick(final double targetPosCorrectionPerTick) {
	this.targetPosCorrectionPerTick = targetPosCorrectionPerTick;
    }


    @Override public ActiveVelocityConstraint generateConstraint(final double deltaTime) {
	final double maxForce = Double.POSITIVE_INFINITY;
	final boolean onlyTranslates = false;
	final PointSeeker pointSeeker = new PointSeeker(
		body, pinnedLocalPoint, globalPointPinnedTo, targetPosCorrectionPerTick, maxForce, onlyTranslates
	);
	return pointSeeker.generateConstraint(deltaTime);
    }
}
