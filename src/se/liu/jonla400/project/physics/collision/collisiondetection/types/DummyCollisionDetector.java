package se.liu.jonla400.project.physics.collision.collisiondetection.types;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.PointMass;
import se.liu.jonla400.project.physics.collision.collisiondetection.ContinousCollisionDetector;
import se.liu.jonla400.project.physics.collision.collisioninfo.UpcomingCollision;

import java.util.Optional;

public class DummyCollisionDetector implements ContinousCollisionDetector
{
    private PointMass pointMass;
    private double radius;

    private Vector2D collisionPlaneNormal;
    private double collisionPlaneOffset;

    public DummyCollisionDetector(final PointMass pointMass, final double radius, final Vector2D collisionPlaneNormal,
				  final double collisionPlaneOffset)
    {
	this.pointMass = pointMass;
	this.radius = radius;
	this.collisionPlaneNormal = collisionPlaneNormal;
	this.collisionPlaneOffset = collisionPlaneOffset;
    }

    @Override public Optional<UpcomingCollision> detectCollision(final double upperTimeLimit) {
	final double posAlongNormal = collisionPlaneNormal.dot(pointMass.getPos());
	final double relPosAlongNormal = posAlongNormal - collisionPlaneOffset;
	final double separation = relPosAlongNormal - radius;

	if (separation <= 0) {
	    final double timeOfImpact = 0;
	    return Optional.of(createCollision(separation, timeOfImpact));
	}

	final double velAlongNormal = collisionPlaneNormal.dot(pointMass.getVel());
	if (velAlongNormal >= 0) {
	    return Optional.empty();
	}
	final double timeOfImpact = -separation / velAlongNormal;
	final double separationDuringCollision = 0;
	return Optional.of(createCollision(separationDuringCollision, timeOfImpact));
    }

    private UpcomingCollision createCollision(final double separation, final double timeOfImpact) {
	return new UpcomingCollision(
		pointMass, getContactPointOffset(), getCollisionNormal(),
		separation, getBounceCoefficient(), getFrictionCoefficient(),
		timeOfImpact
	);
    }

    private Vector2D getContactPointOffset() {
	return collisionPlaneNormal.multiply(-radius);
    }

    private Vector2D getCollisionNormal() {
	return collisionPlaneNormal.copy();
    }

    private double getBounceCoefficient() {
	return 0.4;
    }

    private double getFrictionCoefficient() {
	return 0.1;
    }
}
