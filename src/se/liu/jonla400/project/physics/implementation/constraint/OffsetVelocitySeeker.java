package se.liu.jonla400.project.physics.implementation.constraint;

import se.liu.jonla400.project.math.Matrix22;
import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.abstraction.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.abstraction.constraint.VelocityConstrainer;
import se.liu.jonla400.project.physics.abstraction.main.Body;

public class OffsetVelocitySeeker implements VelocityConstrainer
{
    private Body body;
    private Vector2D offset;
    private Vector2D targetVel;
    private double maxForce;

    public OffsetVelocitySeeker(final Body body, final Vector2D offset, final Vector2D targetVel, final double maxForce) {
        this.body = body;
        this.offset = offset.copy();
        this.targetVel = targetVel;
        this.maxForce = maxForce;
    }

    @Override public ActiveVelocityConstraint generateConstraint(final double deltaTime) {
        final Matrix22 pointInvMass = body.getInvMassAt(offset);
        final double maxImpulse = maxForce * deltaTime;

        return new ActiveVelocityConstraint()
        {
            private Vector2D impulse = Vector2D.createZeroVector();

            @Override public void updateImpulse() {
                final Vector2D vel = body.getVelAt(offset);
                final Vector2D targetDeltaVel = targetVel.subtract(vel);
                final Vector2D targetDeltaImpulse = pointInvMass.getFactorIfProductIs(targetDeltaVel);

                final Vector2D targetImpulse = impulse.add(targetDeltaImpulse);
                final Vector2D nextImpulse = targetImpulse.limitMagnitude(maxImpulse);
                final Vector2D deltaImpulse = nextImpulse.subtract(impulse);
                impulse.set(nextImpulse);

                body.applyOffsetImpulse(offset, deltaImpulse);
            }
        };
    }
}
