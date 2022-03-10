package se.liu.jonla400.project.restructure.physics.abstraction.collision;

import se.liu.jonla400.project.restructure.math.Interval;
import se.liu.jonla400.project.restructure.math.Matrix22;
import se.liu.jonla400.project.restructure.math.Vector2D;
import se.liu.jonla400.project.restructure.physics.abstraction.main.Body;
import se.liu.jonla400.project.restructure.physics.abstraction.constraint.ActiveImpulse1D;
import se.liu.jonla400.project.restructure.physics.abstraction.constraint.ActiveVelocityConstraint;

public class ActiveCollisionConstraint implements ActiveVelocityConstraint
{
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

    public ActiveCollisionConstraint(final CollisionData collisionData)
    {
        // Copy over from the collision data
        bodies = collisionData.getBodies();
        contactPointOffsets = collisionData.getContactPointOffsets();
        normal = collisionData.getNormal();
        frictionCoefficient = collisionData.getFrictionCoefficient();

        // Pre-compute other data
        tangent = normal.rotate90Degrees(Vector2D.RotationDirection.Y_TO_X);

        final double initNormalVel = getNormalVel();
        if (initNormalVel < 0) {
            targetNormalVel = -collisionData.getBounceCoefficient() * initNormalVel;
        } else {
            targetNormalVel = 0;
        }

        final Matrix22 collisionInvMass = getInvMassOfContactPoint(0).add(getInvMassOfContactPoint(1));
        normalMass = projectMassAlongDirection(collisionInvMass, normal);
        tangentMass = projectMassAlongDirection(collisionInvMass, tangent);

        // Initialize the impulses
        normalImpulse = new ActiveImpulse1D();
        tangentImpulse = new ActiveImpulse1D();
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

    @Override public void updateImpulse() {
        updateNormalImpulse();
        updateTangentImpulse();
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
