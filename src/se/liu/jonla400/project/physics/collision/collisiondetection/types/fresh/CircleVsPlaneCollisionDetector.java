package se.liu.jonla400.project.physics.collision.collisiondetection.types.fresh;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.PointMass;
import se.liu.jonla400.project.physics.collision.collisioninfo.Collision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class CircleVsPlaneCollisionDetector
{
    private final static double BOUNCE_COEFFICIENT = 0.3;
    private final static double FRICTION_COEFFICIENT = 0;

    private CircleCollider circleCollider;
    private PlaneCollider planeCollider;

    private double collisionSeparationTolerence;

    public CircleVsPlaneCollisionDetector(
            final CircleCollider circleCollider, final PlaneCollider planeCollider, final double collisionSeparationTolerence)
    {
        this.circleCollider = circleCollider;
        this.planeCollider = planeCollider;
        this.collisionSeparationTolerence = collisionSeparationTolerence;
    }

    public Optional<SimultaneousCollisionSet> detectNextCollisions(final double timeLimit) {
        return null;
        /*final Vector2D localCirclePos = getLocalCirclePosAtTime(0);

        final List<Collision> collisions = new ArrayList<>();
        for (Plane plane : planeCollider) {
            final Vector2D planeNormal = plane.getNormal();
            final double circleRadius = circleCollider.getRadius();

            final double circlePosAlongNormal = planeNormal.dot(localCirclePos);
            final double minCirclePosAlongNormal = circlePosAlongNormal - circleRadius;
            final double separation = minCirclePosAlongNormal - plane.getOffset();

            if (separation <= collisionSeparationTolerence) {
                // INCORRECT!
                final Vector2D contactPointOffset = planeNormal.multiply(-circleRadius);
                collisions.add(new Collision(
                        circleCollider.getPointMass(), contactPointOffset, planeNormal, separation, BOUNCE_COEFFICIENT, FRICTION_COEFFICIENT
                ));
            }
        }

        if (!collisions.isEmpty()) {
            final double timeOfImpact = 0;
            return Optional.of(new SimultaneousCollisionSet(collisions, timeOfImpact));
        }

        final TreeNode treeNode = getTreeNode(planeCollider.getPlanes(), timeLimit);
        if (treeNode.hasPlanesWithNegativeSeparation()) {
            return
        }*/
    }

    private SimultaneousCollisionSet findFirstCollisions(final double startTime, final double endTime, final List<PlaneWithSeparation> planesCollidedWith) {
        /*
        final double midTime = (startTime + endTime) / 2;
        final TreeNode treeNode = getTreeNode(planesCollidedWith, midTime);

        if (treeNode.hasPlanesWithNegativeSeparation()) {
            return findFirstCollisions(startTime, midTime, treeNode.planesWithNegativeSeparation);
        }
        if (treeNode.hasPlanesWithToleratedSeparation()) {
            return getCollisionsFor(treeNode.planesWithToleratedSeparation, midTime);
        }
        if (treeNode.hasPlanesWithPositiveSeparation()) {
            return findFirstCollisions(midTime, endTime, treeNode.planesWithPositiveSeparation);
        }*/
        return null;
    }

    private TreeNode getTreeNode(final Collection<Plane> planes, final double time) {
        final List<PlaneWithSeparation> planesWithNegativeSeparation = new ArrayList<>();
        final List<PlaneWithSeparation> planesWithToleratedSeparation = new ArrayList<>();
        final List<PlaneWithSeparation> planesWithPositiveSeparation = new ArrayList<>();

        final Vector2D localCirclePos = getLocalCirclePosAtTime(time);

        for (Plane plane : planes) {
            final Vector2D planeNormal = plane.getNormal();
            final double circleRadius = circleCollider.getRadius();

            final double circlePosAlongNormal = planeNormal.dot(localCirclePos);
            final double minCirclePosAlongNormal = circlePosAlongNormal - circleRadius;
            final double separation = minCirclePosAlongNormal - plane.getOffset();

            final PlaneWithSeparation planeWithSeparation = new PlaneWithSeparation(plane, separation);
            if (separation < -collisionSeparationTolerence) {
                planesWithNegativeSeparation.add(planeWithSeparation);
            } else if (separation > collisionSeparationTolerence) {
                planesWithPositiveSeparation.add(planeWithSeparation);
            } else {
                planesWithToleratedSeparation.add(planeWithSeparation);
            }
        }

        return new TreeNode(planesWithNegativeSeparation, planesWithToleratedSeparation, planesWithPositiveSeparation);
    }

    private SimultaneousCollisionSet getCollisionsFor(final List<PlaneWithSeparation> planesWithSeparation, final double timeOfImpact) {
        /*final List<Collision> collisions = new ArrayList<>();
        for (PlaneWithSeparation planeWithSeparation : planesWithSeparation) {
            final Vector2D normal = planeWithSeparation.plane.getNormal();
            final Vector2D contactPointOffset = normal.multiply(-circleRadius);
            collisions.add(new Collision(
                    circleCollider.getPointMass(), contactPointOffset, normal, planeWithSeparation.separation, BOUNCE_COEFFICIENT, FRICTION_COEFFICIENT
            ));
        }
        return new SimultaneousCollisionSet(collisions, timeOfImpact);*/
        return null;
    }

    private Vector2D getLocalCirclePosAtTime(final double time) {
        final PointMass circlePointMassCopy = circleCollider.getPointMass().copy();
        final PointMass planePointMassCopy = planeCollider.getPointMass().copy();

        circlePointMassCopy.tick(time);
        planePointMassCopy.tick(time);

        final Vector2D circlePos = circlePointMassCopy.getPos();
        return planePointMassCopy.convertGlobalPointToLocalPoint(circlePos);
    }

    private static class TreeNode {
        private List<PlaneWithSeparation> planesWithNegativeSeparation;
        private List<PlaneWithSeparation> planesWithToleratedSeparation;
        private List<PlaneWithSeparation> planesWithPositiveSeparation;

        private TreeNode(final List<PlaneWithSeparation> planesWithNegativeSeparation,
                         final List<PlaneWithSeparation> planesWithToleratedSeparation,
                         final List<PlaneWithSeparation> planesWithPositiveSeparation)
        {
            this.planesWithNegativeSeparation = planesWithNegativeSeparation;
            this.planesWithToleratedSeparation = planesWithToleratedSeparation;
            this.planesWithPositiveSeparation = planesWithPositiveSeparation;
        }

        private boolean hasPlanesWithNegativeSeparation() {
            return !planesWithNegativeSeparation.isEmpty();
        }

        private boolean hasPlanesWithToleratedSeparation() {
            return !planesWithToleratedSeparation.isEmpty();
        }

        private boolean hasPlanesWithPositiveSeparation() {
            return !planesWithPositiveSeparation.isEmpty();
        }
    }

    private static class PlaneWithSeparation {
        private Plane plane;
        private double separation;

        private PlaneWithSeparation(final Plane plane, final double separation) {
            this.plane = plane;
            this.separation = separation;
        }
    }

}
