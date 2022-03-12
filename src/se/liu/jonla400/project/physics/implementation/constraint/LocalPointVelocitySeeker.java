package se.liu.jonla400.project.physics.implementation.constraint;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.abstraction.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.abstraction.constraint.VelocityConstrainer;
import se.liu.jonla400.project.physics.abstraction.main.Body;

public class LocalPointVelocitySeeker implements VelocityConstrainer
{
    private Body body;
    private Vector2D localPoint;
    private Vector2D targetVel;
    private double maxForce;

    public LocalPointVelocitySeeker(final Body body, final Vector2D localPoint, final Vector2D targetVel, final double maxForce) {
        this.body = body;
        this.localPoint = localPoint.copy();
        this.targetVel = targetVel;
        this.maxForce = maxForce;
    }

    @Override public ActiveVelocityConstraint generateConstraint(final double deltaTime) {
        final Vector2D offset = body.convertLocalVectorToGlobalVector(localPoint);
        final OffsetVelocitySeeker offsetVelSeeker = new OffsetVelocitySeeker(body, offset, targetVel, maxForce);
        return offsetVelSeeker.generateConstraint(deltaTime);
    }
}
