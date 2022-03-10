package se.liu.jonla400.project.physics.collision.collisiondetection.types.fresh;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.PointMass;
import se.liu.jonla400.project.physics.collision.collisioninfo.Collision;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CircleVsPlaneCollisionDetector1
{
    private final static double BOUNCE_COEFFICIENT = 0.3;
    private final static double FRICTION_COEFFICIENT = 0;

    private CircleCollider circleCollider;
    private PlaneCollider planeCollider;

    private double collisionSeparationTolerence;

    public CircleVsPlaneCollisionDetector1(
            final CircleCollider circleCollider, final PlaneCollider planeCollider, final double collisionSeparationTolerence)
    {
        this.circleCollider = circleCollider;
        this.planeCollider = planeCollider;
        this.collisionSeparationTolerence = collisionSeparationTolerence;
    }

    public Optional<SimultaneousCollisionSet> detectNextCollisions(final double timeLimit) {
        final Collection<Plane> planes = planeCollider.getPlanes();

        final Vector2D startLocalCirclePos = getLocalPos(circleCollider.getPointMass(), planeCollider.getPointMass());
        final SeparationSet startSeparations = getSeparations(startLocalCirclePos, planes);
        final Collection<Separation> nonPositiveStartSeparations = startSeparations.getNonPositiveSeparations();
        if (!nonPositiveStartSeparations.isEmpty()) {
            final double timeOfImpact = 0;
            return Optional.of(getCollisions(nonPositiveStartSeparations, timeOfImpact));
        }

        final Vector2D endLocalCirclePos = getLocalCirclePosAtTime(timeLimit);
        final SeparationSet endSeparations = getSeparations(endLocalCirclePos, planes);
        final Optional<SimultaneousCollisionSet> firstCollisions = foo(0, timeLimit, endSeparations.negativeSeparations);
        if (firstCollisions.isPresent()) {
            return firstCollisions;
        }
        if (!endSeparations.toleratedSeparations.isEmpty()) {
            return Optional.of(getCollisions(endSeparations.toleratedSeparations, timeLimit));
        }
        return Optional.empty();
    }


    private Optional<SimultaneousCollisionSet> foo(final double startTime, final double endTime, final Collection<Separation> planesCollidedWith) {
        return null;
        /*if (planesCollidedWith.isEmpty()) {
            return Optional.empty();
        }

        final double midTime = (startTime + endTime) / 2;
        final Vector2D circlePosAtMidTime = getLocalCirclePosAtTime(midTime);
        final SeparationSet separationsAtMidTime = getSeparations(circlePosAtMidTime, planesCollidedWith);

        foo(separationsAtMidTime.negativeSeparations*/
    }

    private Vector2D getLocalCirclePosAtTime(final double time) {
        final PointMass circlePointMassCopy = circleCollider.getPointMass().copy();
        final PointMass planePointMassCopy = planeCollider.getPointMass().copy();

        circlePointMassCopy.tick(time);
        planePointMassCopy.tick(time);

        return getLocalPos(circlePointMassCopy, planePointMassCopy);
    }

    private Vector2D getLocalPos(final PointMass globalPosCarrier, final PointMass frame) {
        return frame.convertGlobalPointToLocalPoint(globalPosCarrier.getPos());
    }

    private SeparationSet getSeparations(final Vector2D circlePos, final Collection<Plane> planes) {
        final Collection<Separation> negativeSeparations = new ArrayList<>();
        final Collection<Separation> toleratedSeparations = new ArrayList<>();
        final Collection<Separation> positiveSeparations = new ArrayList<>();

        for (Plane plane : planes) {
            final double separation = plane.getSeparationOf(circlePos) - circleCollider.getRadius();
            final Separation planeToCircleSeparation = new Separation(plane, separation);
            if (separation < -collisionSeparationTolerence) {
                negativeSeparations.add(planeToCircleSeparation);
            } else if (separation > collisionSeparationTolerence) {
                positiveSeparations.add(planeToCircleSeparation);
            } else {
                toleratedSeparations.add(planeToCircleSeparation);
            }
        }

        return new SeparationSet(negativeSeparations, toleratedSeparations, positiveSeparations);
    }

    private SimultaneousCollisionSet getCollisions(final Collection<Separation> separations, final double timeOfImpact) {
        final List<Collision> collisions = new ArrayList<>(separations.size());
        for (Separation separation : separations) {
            collisions.add(new Collision(
                    circleCollider.getPointMass(), null, null, separation.value,
                    BOUNCE_COEFFICIENT, FRICTION_COEFFICIENT
            ));
        }
        return new SimultaneousCollisionSet(collisions, timeOfImpact);
    }

    private static class SeparationSet
    {
        private Collection<Separation> negativeSeparations;
        private Collection<Separation> toleratedSeparations;
        private Collection<Separation> positiveSeparations;

        private SeparationSet(final Collection<Separation> negativeSeparations,
                              final Collection<Separation> toleratedSeparations,
                              final Collection<Separation> positiveSeparations)
        {
            this.negativeSeparations = negativeSeparations;
            this.toleratedSeparations = toleratedSeparations;
            this.positiveSeparations = positiveSeparations;
        }

        private Collection<Separation> getNonPositiveSeparations() {
            return Stream.concat(negativeSeparations.stream(), toleratedSeparations.stream()).collect(Collectors.toList());
        }
    }

    private static class Separation
    {
        private Plane plane;
        private double value;

        private Separation(final Plane plane, final double value) {
            this.plane = plane;
            this.value = value;
        }
    }

}
