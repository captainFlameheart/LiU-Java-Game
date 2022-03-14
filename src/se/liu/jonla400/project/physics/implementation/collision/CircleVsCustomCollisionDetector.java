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

	    final double bounceCoefficient = 0.3;
	    final double frictionCoefficient = 1;

	    collisions.add(new CollisionData(
		    circleBody, circleContactPoint, customColliderBody, customColliderContactPoint,
		    collisionNormal, penetration, bounceCoefficient, frictionCoefficient
	    ));
	}
	return collisions;
    }

}