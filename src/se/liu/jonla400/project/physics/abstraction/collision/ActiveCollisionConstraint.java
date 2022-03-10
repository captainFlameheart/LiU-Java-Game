package se.liu.jonla400.project.physics.abstraction.collision;

import se.liu.jonla400.project.math.Interval;
import se.liu.jonla400.project.math.Matrix22;
import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.abstraction.main.Body;
import se.liu.jonla400.project.physics.abstraction.constraint.ActiveImpulse1D;
import se.liu.jonla400.project.physics.abstraction.constraint.ActiveVelocityConstraint;

/**
 * Represents an active velocity constraint between two colliding bodies.
 */
public class ActiveCollisionConstraint implements ActiveVelocityConstraint
{
    // Can only push the points in contact along the normal, not pull them
    private final static Interval NORMAL_IMPULSE_RANGE = new Interval(0, Double.POSITIVE_INFINITY);

    private Body[] bodies;
    private Vector2D[] contactPointOffsets;

    private Vector2D normal;
    private Vector2D tangent;

    private double targetNormalVel;
    private double frictionCoefficient;

    private double normalMass;
    private double tangentMass;

    private ActiveImpulse1D normalImpulse;
    private ActiveImpulse1D tangentImpulse;

    public ActiveCollisionConstraint(final CollisionData collisionData, final double deltaTime,
                                     final double penetrationTolerence, final double posCorrectionFraction)
    {
        bodies = collisionData.getBodies();
        contactPointOffsets = collisionData.getContactPointOffsets();

        normal = collisionData.getNormal();
        tangent = normal.rotate90Degrees(Vector2D.RotationDirection.Y_TO_X);

        initTargetNormalVel(collisionData, deltaTime, penetrationTolerence, posCorrectionFraction);
        frictionCoefficient = collisionData.getFrictionCoefficient();

        final Matrix22 collisionInvMass = getInvMassOfContactPoint(0).add(getInvMassOfContactPoint(1));
        normalMass = projectMassAlongDirection(collisionInvMass, normal);
        tangentMass = projectMassAlongDirection(collisionInvMass, tangent);

        normalImpulse = new ActiveImpulse1D();
        tangentImpulse = new ActiveImpulse1D();
    }

    private void initTargetNormalVel(final CollisionData collisionData, final double deltaTime,
                                     final double penetrationTolerence, final double posCorrectionFraction)
    {
        final double untoleratedPenetration = collisionData.getPenetration() - penetrationTolerence;
        final double targetNormalVelFromPenetration;
        if (untoleratedPenetration > 0) {
            final double posCorrection = posCorrectionFraction * untoleratedPenetration;
            targetNormalVelFromPenetration = posCorrection / deltaTime;
        } else {
            targetNormalVelFromPenetration = 0;
        }

        final double initNormalVel = getNormalVel();
        final double targetNormalVelFromBounce;
        if (initNormalVel < 0) {
            targetNormalVelFromBounce = -collisionData.getBounceCoefficient() * initNormalVel;
        } else {
            targetNormalVelFromBounce = 0;
        }

        targetNormalVel = Math.max(targetNormalVelFromPenetration, targetNormalVelFromBounce);
    }

    private Matrix22 getInvMassOfContactPoint(final int pointIndex) {
        return bodies[pointIndex].getInvMassAt(contactPointOffsets[pointIndex]);
    }

    private double projectMassAlongDirection(final Matrix22 invMass, final Vector2D dir) {
        // See derivation in report
        final double dirX = dir.getX();
        final double dirY = dir.getY();
        final double commonWeight = dirX * dirY;
        final Matrix22 weights = new Matrix22(new double[][]{
                {dirX * dirX, commonWeight},
                {commonWeight, dirY * dirY}
        });

        final double invProjectedMass = invMass.getWeightedSum(weights);
        return 1 / invProjectedMass;
    }

    /**
     * Constrains the velocities of the pair of bodies participating in the collision. Impulses
     * are applied for both non-penetration and friction
     */
    @Override public void updateImpulse() {
        updateNormalImpulse();
        updateTangentImpulse(); // Depends on the magnitude of the normal impulse
    }

    private void updateNormalImpulse() {
        final double targetDeltaNormalVel = targetNormalVel - getNormalVel();
        final double deltaNormalImpulse = normalImpulse.update(targetDeltaNormalVel, normalMass, NORMAL_IMPULSE_RANGE);
        applyImpulse(normal, deltaNormalImpulse);
    }

    private void updateTangentImpulse() {
        final double maxTangentImpulse = frictionCoefficient * normalImpulse.get();
        final Interval tangentImpulseRange = new Interval(-maxTangentImpulse, maxTangentImpulse);

        final double targetDeltaTangentVel = -getTangentVel();
        final double deltaTangentImpulse = tangentImpulse.update(targetDeltaTangentVel, tangentMass, tangentImpulseRange);
        applyImpulse(tangent, deltaTangentImpulse);
    }

    private double getNormalVel() {
        return getRelativeContactPointVelAlong(normal);
    }

    private double getTangentVel() {
        return getRelativeContactPointVelAlong(tangent);
    }

    private double getRelativeContactPointVelAlong(final Vector2D dir) {
        final Vector2D relativeContactPointVel = getContactPointVel(0).subtract(getContactPointVel(1));
        return dir.dot(relativeContactPointVel);
    }

    private Vector2D getContactPointVel(final int pointIndex) {
        return bodies[pointIndex].getVelAt(contactPointOffsets[pointIndex]);
    }

    private void applyImpulse(final Vector2D dir, final double impulse) {
        applyImpulse(0, dir, impulse);
        applyImpulse(1, dir, -impulse);
    }

    private void applyImpulse(final int pointIndex, final Vector2D dir, final double impulse) {
        bodies[pointIndex].applyOffsetImpulse(contactPointOffsets[pointIndex], dir.multiply(impulse));
    }

}
