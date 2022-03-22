package se.liu.jonla400.project.physics.abstraction.collision;

import se.liu.jonla400.project.math.Interval;
import se.liu.jonla400.project.math.Matrix22;
import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.abstraction.constraint.ActiveImpulse1D;
import se.liu.jonla400.project.physics.abstraction.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.abstraction.constraint.OffsetBodyPoint;
import se.liu.jonla400.project.physics.abstraction.constraint.OffsetBodyPointPair;

/**
 * Represents an active velocity constraint between two colliding bodies.
 */
public class ActiveCollisionConstraint implements ActiveVelocityConstraint
{
    // Can only push the points in contact along the normal, not pull them
    private final static Interval NORMAL_IMPULSE_RANGE = new Interval(0, Double.POSITIVE_INFINITY);

    private OffsetBodyPointPair contactPoints;

    private Vector2D normal;
    private Vector2D tangent;

    private double targetNormalVel;
    private double frictionCoefficient;

    private double normalMass;
    private double tangentMass;

    private ActiveImpulse1D normalImpulse;
    private ActiveImpulse1D tangentImpulse;

    private ActiveCollisionConstraint(final OffsetBodyPointPair contactPoints, final Vector2D normal, final Vector2D tangent,
                                      final double targetNormalVel, final double frictionCoefficient, final double normalMass,
                                      final double tangentMass, final ActiveImpulse1D normalImpulse, final ActiveImpulse1D tangentImpulse)
    {
        this.contactPoints = contactPoints;
        this.normal = normal;
        this.tangent = tangent;
        this.targetNormalVel = targetNormalVel;
        this.frictionCoefficient = frictionCoefficient;
        this.normalMass = normalMass;
        this.tangentMass = tangentMass;
        this.normalImpulse = normalImpulse;
        this.tangentImpulse = tangentImpulse;
    }

    public static ActiveCollisionConstraint createFromCollisionData(
            final CollisionData<?> collisionData, final double deltaTime,
            final double penetrationTolerence, final double penetrationCorrectionFraction)
    {
        final OffsetBodyPointPair contactPoints = collisionData.getContactPoints();
        final Vector2D normal = collisionData.getNormal();
        final Vector2D tangent = normal.rotate90Degrees(Vector2D.RotationDirection.Y_TO_X); // Temp

        final double targetNormalVel = getTargetNormalVel(
                contactPoints, normal, collisionData.getPenetration(), penetrationTolerence, penetrationCorrectionFraction,
                collisionData.getBounceCoefficient(), deltaTime
        );

        final Matrix22 invMass = contactPoints.getInvMass();
        final double normalMass = projectMassAlongDirection(invMass, normal);
        final double tangentMass = projectMassAlongDirection(invMass, tangent);

        final ActiveImpulse1D normalImpulse = new ActiveImpulse1D();
        final ActiveImpulse1D tangentImpulse = new ActiveImpulse1D();

        return new ActiveCollisionConstraint(
                contactPoints, normal, tangent, targetNormalVel, collisionData.getFrictionCoefficient(), normalMass, tangentMass,
                normalImpulse, tangentImpulse);
    }

    private static double getTargetNormalVel(
            final OffsetBodyPointPair contactPoints, final Vector2D normal,
            final double penetration, final double penetrationTolerence, final double penetrationCorrectionFraction,
            final double bounceCoefficient, final double deltaTime)
    {
        final double penetrationCorrectionVel =
                getPenetrationCorrectionVel(penetration, penetrationTolerence, penetrationCorrectionFraction, deltaTime);
        final double bounceVel = getBounceVel(contactPoints, normal, bounceCoefficient);
        return Math.max(penetrationCorrectionVel, bounceVel);
    }

    private static double getPenetrationCorrectionVel(
            final double penetration, final double penetrationTolerence, final double penetrationCorrectionFraction,
            final double deltaTime)
    {
        final double untoleratedPenetration = penetration - penetrationTolerence;
        if (untoleratedPenetration <= 0) {
            return 0;
        }
        final double penetrationCorrection = penetrationCorrectionFraction * untoleratedPenetration;
        return penetrationCorrection / deltaTime;
    }

    private static double getBounceVel(final OffsetBodyPointPair contactPoints, final Vector2D normal, final double bounceCoefficient) {
        final double initNormalVel = getVelAlong(normal, contactPoints);
        if (initNormalVel >= 0) {
            return 0;
        }
        return -bounceCoefficient * initNormalVel;
    }

    private static double projectMassAlongDirection(final Matrix22 invMass, final Vector2D dir) {
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
        final double targetDeltaNormalVel = targetNormalVel - getVelAlong(normal);
        final double deltaNormalImpulse = normalImpulse.update(targetDeltaNormalVel, normalMass, NORMAL_IMPULSE_RANGE);
        applyImpulse(normal, deltaNormalImpulse);
    }

    private void updateTangentImpulse() {
        final double maxTangentImpulse = frictionCoefficient * normalImpulse.get();
        final Interval tangentImpulseRange = new Interval(-maxTangentImpulse, maxTangentImpulse);

        final double targetDeltaTangentVel = -getVelAlong(tangent);
        final double deltaTangentImpulse = tangentImpulse.update(targetDeltaTangentVel, tangentMass, tangentImpulseRange);
        applyImpulse(tangent, deltaTangentImpulse);
    }

    private double getVelAlong(final Vector2D dir) {
        return getVelAlong(dir, contactPoints);
    }

    private static double getVelAlong(final Vector2D dir, final OffsetBodyPointPair contactPoints) {
        return dir.dot(contactPoints.getVel());
    }

    private void applyImpulse(final Vector2D dir, final double magnitude) {
        contactPoints.applyImpulse(dir.multiply(magnitude));
    }
}
