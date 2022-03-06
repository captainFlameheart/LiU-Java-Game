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

    private double collisionPlaneY;

    public DummyCollisionDetector(final PointMass pointMass, final double radius, final double collisionPlaneY) {
	this.pointMass = pointMass;
	this.radius = radius;
	this.collisionPlaneY = collisionPlaneY;
    }

    @Override public Optional<UpcomingCollision> detectCollision(final double upperTimeLimit) {
	final double y = pointMass.getPos().getY();
	final double bottomY = y - radius;
	final double separation = bottomY - collisionPlaneY;

	if (separation <= 0) {
	    final double timeOfImpact = 0;
	    return Optional.of(createCollision(separation, timeOfImpact));
	}

	final double vy = pointMass.getVel().getY();
	if (vy >= 0) {
	    return Optional.empty();
	}
	final double timeOfImpact = -separation / vy;
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
	return Vector2D.createCartesianVector(0, -radius);
    }

    private Vector2D getCollisionNormal() {
	return Vector2D.createCartesianVector(0, 1);
    }

    private double getBounceCoefficient() {
	return 0.3;
    }

    private double getFrictionCoefficient() {
	return 0.1;
    }
}
