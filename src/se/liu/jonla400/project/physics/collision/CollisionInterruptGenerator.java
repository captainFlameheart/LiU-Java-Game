package se.liu.jonla400.project.physics.collision;

import se.liu.jonla400.project.physics.collision.collisiondetection.ContinousCollisionDetector;
import se.liu.jonla400.project.physics.collision.collisioninfo.UpcomingCollision;
import se.liu.jonla400.project.physics.collision.collisionlistening.CollisionListener;
import se.liu.jonla400.project.timestepping.Interrupt;
import se.liu.jonla400.project.timestepping.InterruptGenerator;

import java.util.Optional;

/**
 * Interrupts a time step when a collision occurs according to an arbitrary
 * ContinousCollisionDetector, and passes the collision information onto
 * an arbitrary CollisionListener
 */
public class CollisionInterruptGenerator implements InterruptGenerator
{
    private ContinousCollisionDetector collisionDetector;
    private CollisionListener collisionListener;

    /**
     * Creates a new time step collision interrupter that gets collisions from the
     * given collision detector and passes the collision information to the given
     * collision listener
     *
     * @param collisionDetector The collision detector to get collisions from
     * @param collisionListener The collision listerner to give collisions to
     */
    public CollisionInterruptGenerator(final ContinousCollisionDetector collisionDetector, final CollisionListener collisionListener) {
	this.collisionDetector = collisionDetector;
	this.collisionListener = collisionListener;
    }

    /**
     * Returns the next collision interrupt of the time step, if any. During the
     * interrupt, the collision will be delegated to the collision listener.
     *
     * @param timeLeft The time left of the time step
     * @return The next collision interrupt, if any
     */
    @Override public Optional<Interrupt> generateNextInterrupt(final double timeLeft) {
	final Optional<UpcomingCollision> possibleCollision = collisionDetector.detectCollision(timeLeft);
	if (possibleCollision.isEmpty()) {
	    return Optional.empty();
	}

	final UpcomingCollision collision = possibleCollision.get();
	// Interrupt the time step when the collision occurs and give the collision information to the collision listener
	return Optional.of(new Interrupt(
		collision.getTimeOfImpact(), timeLeftAfter -> collisionListener.onCollision(collision, timeLeftAfter)
	));
    }
}
