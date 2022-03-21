package se.liu.jonla400.project.physics.implementation.collision;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.abstraction.collision.CollisionData;
import se.liu.jonla400.project.physics.abstraction.collision.CollisionDetector;
import se.liu.jonla400.project.physics.abstraction.main.Body;

import java.util.ArrayList;
import java.util.Collection;

public class CircleVsCustomCollisionDetector<T> implements CollisionDetector<T>
{
    private CircleCollider circleCollider;
    private CustomCollider<T> customCollider;

    public CircleVsCustomCollisionDetector(final CircleCollider circleCollider, final CustomCollider<T> customCollider) {
	this.circleCollider = circleCollider;
	this.customCollider = customCollider;
    }

    private Vector2D getLocalCirclePosInCustomCollider() {
	final Vector2D globalCirclePos = circleCollider.getBody().getPos();
	return customCollider.getBody().convertGlobalPointToLocalPoint(globalCirclePos);
    }

    @Override public Collection<CollisionData<T>> detectCollisions() {
	final Collection<CollisionData<T>> collisions = new ArrayList<>();

	final Body circleBody = circleCollider.getBody();
	final double radius = circleCollider.getRadius();
	final Body customColliderBody = customCollider.getBody();
	final TranslatedCustomShape<T> translatedCustomShape = customCollider.getShape();
	final Vector2D customShapeTranslation = translatedCustomShape.getTranslation();

	final Vector2D localCirclePos = customColliderBody.convertGlobalPointToLocalPoint(circleBody.getPos());
	localCirclePos.subtractLocally(customShapeTranslation);

	for (LineSegment<T> lineSegment : translatedCustomShape.getShape()) {
	    final Vector2D closestPoint = lineSegment.findClosestPointTo(localCirclePos);
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
	    final Vector2D customColliderContactPoint = customColliderBody.convertLocalVectorToGlobalVector(
		    customShapeTranslation.add(closestPoint));

	    final double bounceCoefficient = 0.3;
	    final double frictionCoefficient = 1;

	    final T userData = lineSegment.getUserData();
	    collisions.add(new CollisionData<>(
		    circleBody, circleContactPoint, customColliderBody, customColliderContactPoint,
		    collisionNormal, penetration, bounceCoefficient, frictionCoefficient, userData
	    ));
	}
	return collisions;
    }

}
