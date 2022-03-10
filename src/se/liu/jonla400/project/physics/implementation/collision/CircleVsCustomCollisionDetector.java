package se.liu.jonla400.project.physics.implementation.collision;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.abstraction.collision.CollisionDetector;
import se.liu.jonla400.project.physics.abstraction.main.Body;
import se.liu.jonla400.project.physics.abstraction.collision.CollisionData;

import java.util.ArrayList;
import java.util.Collection;

public class CircleVsCustomCollisionDetector implements CollisionDetector
{
    private CircleCollider circleCollider;
    private CustomCollider customCollider;

    public CircleVsCustomCollisionDetector(final CircleCollider circleCollider, final CustomCollider customCollider) {
	this.circleCollider = circleCollider;
	this.customCollider = customCollider;
    }

    private Vector2D getLocalCirclePosInCustomCollider() {
	final Vector2D globalCirclePos = circleCollider.getBody().getPos();
	return customCollider.getBody().convertGlobalPointToLocalPoint(globalCirclePos);
    }

    @Override public Collection<CollisionData> detectCollisions() {
	final Collection<CollisionData> collisions = new ArrayList<>();

	final Body circleBody = circleCollider.getBody();
	final Body customColliderBody = customCollider.getBody();
	final double customColliderAngle = customColliderBody.getAngle();

	final Vector2D localCirclePos = customColliderBody.convertGlobalPointToLocalPoint(circleBody.getPos());
	final double radius = circleCollider.getRadius();

	for (LineSegment lineSegment : customCollider.getShape()) {
	    final Vector2D closestPoint = lineSegment.getClosestPointTo(localCirclePos);
	    final Vector2D circleOffsetFromClosestPoint = localCirclePos.subtract(closestPoint);
	    final double dist = circleOffsetFromClosestPoint.getMagnitude();
	    if (dist == 0) {
		continue;
	    }
	    final double penetration = radius - dist;
	    if (penetration < 0) {
		continue;
	    }
	    final Vector2D localCollisionNormal = circleOffsetFromClosestPoint.divide(dist);
	    final Vector2D localCircleContactPoint = localCollisionNormal.multiply(-radius);

	    final Vector2D collisionNormal = customColliderBody.convertLocalVectorToGlobalVector(localCollisionNormal);
	    final Vector2D circleContactPoint = customColliderBody.convertLocalVectorToGlobalVector(localCircleContactPoint);
	    final Vector2D customColliderContactPoint = customColliderBody.convertLocalVectorToGlobalVector(closestPoint);

	    final double bounceCoefficient = 0.5;
	    final double frictionCoefficient = 1;

	    collisions.add(new CollisionData(
		    circleBody, circleContactPoint, customColliderBody, customColliderContactPoint,
		    collisionNormal, penetration, bounceCoefficient, frictionCoefficient
	    ));

	    /*
	    final Vector2D lineSegmentStart = lineSegment.getStart();
	    final Vector2D lineSegmentEnd = lineSegment.getEnd();
	    final Vector2D lineSegmentNormal = lineSegment.getNormal();
	    final Vector2D lineSegmentTangent = lineSegment.getTangent();
	    final double lineSegmentNormalPos = lineSegment.getNormalPos();
	    final double lineSegmentTangentStart = lineSegment.getTangentStart();
	    final double lineSegmentTangentEnd = lineSegment.getTangentEnd();

	    final double circleNormalPos = lineSegmentNormal.dot(localCirclePos);
	    final double relativeNormalPos = circleNormalPos - lineSegmentNormalPos;
	    final double circleTangentPos = lineSegment.getTangentPosOf(localCirclePos);

	    if (circleTangentPos < lineSegmentTangentStart) {

	    } else if (circleTangentPos > lineSegmentTangentEnd) {

	    } else {
		if (relativeNormalPos == 0) {
		    continue;
		}
		final double dist = Math.abs(relativeNormalPos);
		final double penetration = radius - dist;
		if (penetration < 0) {
		    continue;
		}
		final double sideSign = relativeNormalPos / dist;
		final Vector2D localCollisionNormal = lineSegmentNormal.multiply(sideSign);
		final Vector2D localCircleContactPoint = localCollisionNormal.multiply(-radius);
		final Vector2D localCustomColliderContactPoint =
			lineSegmentTangent.multiply(circleTangentPos).add(lineSegmentNormal.multiply(lineSegmentNormalPos));

		final Vector2D collisionNormal = customColliderBody.convertLocalVectorToGlobalVector(localCollisionNormal);
		final Vector2D circleContactPoint = customColliderBody.convertLocalVectorToGlobalVector(localCircleContactPoint);
		final Vector2D customColliderContactPoint = customColliderBody.convertLocalVectorToGlobalVector(localCustomColliderContactPoint);

		final double bounceCoefficient = 0.9;
		final double frictionCoefficient = 0.5;

		collisions.add(new CollisionData(
			circleBody, circleContactPoint, customColliderBody, customColliderContactPoint,
			collisionNormal, penetration, bounceCoefficient, frictionCoefficient
		));
	    }*/
	}
	return collisions;
    }

}
