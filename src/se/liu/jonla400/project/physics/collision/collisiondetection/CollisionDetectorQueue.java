package se.liu.jonla400.project.physics.collision.collisiondetection;

import se.liu.jonla400.project.physics.collision.collisioninfo.Collision;
import se.liu.jonla400.project.physics.collision.collisioninfo.UpcomingCollision;
import se.liu.jonla400.project.timestepping.Interrupt;
import se.liu.jonla400.project.timestepping.InterruptGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CollisionDetectorQueue implements ContinousCollisionDetector
{
    private List<ContinousCollisionDetector> collisionDetectors;

    public CollisionDetectorQueue(final ContinousCollisionDetector... collisionDetectors) {
	this.collisionDetectors = new ArrayList<>(Arrays.asList(collisionDetectors));
    }

    @Override public Optional<UpcomingCollision> detectCollision(double upperTimeLimit) {
	// DUPLICATE CODE!!!

	UpcomingCollision firstCollision = null;

	for (ContinousCollisionDetector collisionDetector : collisionDetectors) {
	    final Optional<UpcomingCollision> possibleCollision = collisionDetector.detectCollision(upperTimeLimit);
	    if (possibleCollision.isEmpty()) {
		continue;
	    }

	    final UpcomingCollision collision = possibleCollision.get();
	    final double timeOfImpact = collision.getTimeOfImpact();

	    if (timeOfImpact < upperTimeLimit) {
		firstCollision = collision;
		upperTimeLimit = timeOfImpact;
	    }
	}
	return Optional.ofNullable(firstCollision);
    }
}
