package se.liu.jonla400.project.physics.implementation.collision;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.abstraction.collision.CollisionData;
import se.liu.jonla400.project.physics.abstraction.collision.CollisionDetector;
import se.liu.jonla400.project.physics.abstraction.main.Body;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class CircleVsCustomCollisionDetector<T> implements CollisionDetector<T>
{
    private CircleCollider circleCollider;
    private CustomCollider<T> customCollider;

    public CircleVsCustomCollisionDetector(final CircleCollider circleCollider, final CustomCollider<T> customCollider) {
	this.circleCollider = circleCollider;
	this.customCollider = customCollider;
    }

    @Override public Collection<CollisionData<T>> detectCollisions() {
	final Collection<CollisionData<T>> collisions = new ArrayList<>();

	final Vector2D circlePosInCustomShape = getCirclePosInCustomShape();
	for (LineSegment<T> segment : getCustomShapeWithoutTranslation()) {
	    detectCircleVsSegmentCollision(circlePosInCustomShape, circleCollider.getRadius(), segment).ifPresent(c -> {
		final Vector2D circleContactPointOffset = convertCustomShapeVecToGlobalVec(c.circleContactPointOffset);
		final Vector2D customContactPointOffset = convertCustomShapePointToGlobalVec(c.segmentContactPoint);
		final Vector2D normal = convertCustomShapeVecToGlobalVec(c.normal);

		final double bounceCoefficient = 0.3;
		final double frictionCoefficient = 1;

		collisions.add(CollisionData.create(
			circleCollider.getBody(), circleContactPointOffset, customCollider.getBody(), customContactPointOffset,
			normal, c.penetration, bounceCoefficient, frictionCoefficient, segment.getUserData()));
	    });
	}
	return collisions;
    }

    private Vector2D getCirclePosInCustomShape() {
	final Vector2D circlePos = circleCollider.getBody().getPos();
	final Body customColliderBody = customCollider.getBody();
	final Vector2D customShapeTranslation = customCollider.getShape().getTranslation();

	final Vector2D circlePosInCustomCollider = customColliderBody.convertGlobalToLocalPoint(circlePos);
	return circlePosInCustomCollider.subtract(customShapeTranslation);
    }

    private CustomShape<T> getCustomShapeWithoutTranslation() {
	return customCollider.getShape().getShape();
    }

    private static Optional<CircleVsSegmentCollision> detectCircleVsSegmentCollision(final Vector2D circlePos, final double circleRadius, final LineSegment<?> segment) {
	final Vector2D closestSegmentPoint = segment.findClosestPointTo(circlePos);
	final Vector2D circleOffsetFromClosestPoint = circlePos.subtract(closestSegmentPoint);
	final double dist = circleOffsetFromClosestPoint.getMagnitude();
	if (dist == 0) {
	    return Optional.empty();
	}
	final double penetration = circleRadius - dist;
	if (penetration < 0) {
	    return Optional.empty();
	}
	final Vector2D collisionNormal = circleOffsetFromClosestPoint.divide(dist);
	final Vector2D circleContactPointOffset = collisionNormal.multiply(-circleRadius);
	return Optional.of(new CircleVsSegmentCollision(circleContactPointOffset, closestSegmentPoint, collisionNormal, penetration));
    }

    private Vector2D convertCustomShapeVecToGlobalVec(final Vector2D customShapeVec) {
	final Body customBody = customCollider.getBody();
	return customBody.convertLocalToGlobalVector(customShapeVec);
    }

    private Vector2D convertCustomShapePointToGlobalVec(final Vector2D customShapePoint) {
	final Body customBody = customCollider.getBody();
	final Vector2D customShapeTranslation = customCollider.getShape().getTranslation();

	final Vector2D customColliderPoint = customShapeTranslation.add(customShapePoint);
	return customBody.convertLocalToGlobalVector(customColliderPoint);
    }

    private static class CircleVsSegmentCollision
    {
	private Vector2D circleContactPointOffset;
	private Vector2D segmentContactPoint;
	private Vector2D normal;
	private double penetration;

	private CircleVsSegmentCollision(final Vector2D circleContactPointOffset, final Vector2D segmentContactPoint, final Vector2D normal,
					final double penetration)
	{
	    this.circleContactPointOffset = circleContactPointOffset;
	    this.segmentContactPoint = segmentContactPoint;
	    this.normal = normal;
	    this.penetration = penetration;
	}
    }
}
