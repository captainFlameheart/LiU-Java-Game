package se.liu.jonla400.project.physics.collision;

import se.liu.jonla400.project.math.Interval;
import se.liu.jonla400.project.math.Matrix22;
import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.constraint.ActiveImpulse1D;
import se.liu.jonla400.project.physics.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.constraint.OffsetBodyPointPair;

/**
 * Represents an active velocity constraint between two colliding bodies. An impulse is applied
 * along the normal direction of the collision to avoid further penetration and to make the
 * bodies bounce according to the collision's bounce factor. An impulse is also applied in the
 * tangent direction of the collision to simulate friction, and its scale is limited by the
 * collision's friction coefficient.
 */
public class ActiveCollisionConstraint implements ActiveVelocityConstraint
{
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

    /**
     * Creates an ActiveCollisionConstraint based on the {@link CollisionData}. If
     * the penetration is greater than the given penetration tolerence, the normal impulse
     * will be extra strong to make the bodies seperate. The given correction fraction determines
     * how much of the penetration should have been corrected after the upcoming time step, but since
     * the velocity of bodies remains between time steps this value should not be too large
     * (otherwise some extra bounce will occur).
     *
     * @param collisionData The collision data
     * @param deltaTime The size of the time step after solving this velocity constraint
     * @param penetrationTolerance How much of the penetration that is tolerated
     * @param penetrationCorrectionFraction How much to fix of the penetration error, between 0 and 1
     * @return The created ActiveCollisionConstraint
     */
    public static ActiveCollisionConstraint createFromCollisionData(
            final CollisionData<?> collisionData, final double deltaTime,
            final double penetrationTolerance, final double penetrationCorrectionFraction)
    {
        final OffsetBodyPointPair contactPoints = collisionData.getContactPoints();
        final Vector2D normal = collisionData.getNormal();
        final Vector2D tangent = normal.rotate90Degrees();

        final double targetNormalVel = getTargetNormalVel(
                contactPoints, normal, collisionData.getPenetration(), penetrationTolerance, penetrationCorrectionFraction,
                collisionData.getBounceCoefficient(), deltaTime
        );

        final Matrix22 invertedMass = contactPoints.getInvertedMass();
        final double normalMass = projectMassAlongDirection(invertedMass, normal);
        final double tangentMass = projectMassAlongDirection(invertedMass, tangent);

        final ActiveImpulse1D normalImpulse = new ActiveImpulse1D();
        final ActiveImpulse1D tangentImpulse = new ActiveImpulse1D();

        return new ActiveCollisionConstraint(
                contactPoints, normal, tangent, targetNormalVel, collisionData.getFrictionCoefficient(), normalMass, tangentMass,
                normalImpulse, tangentImpulse);
    }

    private static double getTargetNormalVel(
            final OffsetBodyPointPair contactPoints, final Vector2D normal,
            final double penetration, final double penetrationTolerance, final double penetrationCorrectionFraction,
            final double bounceCoefficient, final double deltaTime)
    {
        final double penetrationCorrectionVel =
                getPenetrationCorrectionVel(penetration, penetrationTolerance, penetrationCorrectionFraction, deltaTime);
        final double bounceVel = getBounceVel(contactPoints, normal, bounceCoefficient);
        return Math.max(penetrationCorrectionVel, bounceVel);
    }

    private static double getPenetrationCorrectionVel(
            final double penetration, final double penetrationTolerance, final double penetrationCorrectionFraction,
            final double deltaTime)
    {
        final double penetrationError = penetration - penetrationTolerance;
        if (penetrationError <= 0) {
            return 0;
        }
        final double penetrationCorrection = penetrationCorrectionFraction * penetrationError;
        return penetrationCorrection / deltaTime; // The bigger the time step the smaller the velocity has to be to correct the penetration
    }

    private static double getBounceVel(final OffsetBodyPointPair contactPoints, final Vector2D normal, final double bounceCoefficient) {
        final double initNormalVel = getVelAlong(normal, contactPoints);
        if (initNormalVel >= 0) {
            // The contact points are moving away from each other
            return 0;
        }
        return -bounceCoefficient * initNormalVel;
    }

    private static double projectMassAlongDirection(final Matrix22 invertedMass, final Vector2D dir) {
        // See derivation at doc/math/projectedInvertedMassDerivation
        final double dirX = dir.getX();
        final double dirY = dir.getY();
        final double commonWeight = dirX * dirY;
        final Matrix22 weights = Matrix22.create(
                dirX * dirX, commonWeight,
                commonWeight, dirY * dirY
        );

        final double invertedProjectedMass = invertedMass.getWeightedSum(weights);
        return 1 / invertedProjectedMass;
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
        final Interval normalImpulseRange = new Interval(0, Double.POSITIVE_INFINITY);

        final double targetDeltaNormalVel = targetNormalVel - getVelAlong(normal);
        final double deltaNormalImpulse = normalImpulse.update(targetDeltaNormalVel, normalMass, normalImpulseRange);
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
