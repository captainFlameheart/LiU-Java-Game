package se.liu.jonla400.project.physics.collision;

import se.liu.jonla400.project.physics.constraint.ActiveVelocityConstraintList;
import se.liu.jonla400.project.physics.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.constraint.VelocityConstrainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Handles collisions by detecting them using a {@link CollisionDetector}, notifying
 * {@link CollisionListener}s about the collisions and finally solving the collisions
 * using {@link ActiveCollisionConstraint}s.
 *
 * @param <T> The type of user data associated with each collision
 */
public class CollisionHandler<T> implements VelocityConstrainer
{
    private CollisionDetector<T> collisionDetector;
    private double penetrationTolerance;
    private double penetrationCorrectionFraction;
    private List<CollisionListener<T>> listeners;

    public CollisionHandler(final CollisionDetector<T> collisionDetector, final double penetrationTolerance,
			    final double penetrationCorrectionFraction, final List<CollisionListener<T>> listeners)
    {
	this.collisionDetector = collisionDetector;
	this.penetrationTolerance = penetrationTolerance;
	this.penetrationCorrectionFraction = penetrationCorrectionFraction;
	this.listeners = listeners;
    }

    /**
     * Creates a CollisionHandler with default configuration for solving collisions
     *
     * @param collisionDetector The collision detector to get the collisions from
     * @param <T> The type of user data associated with each collision
     * @return The created CollisionHandler
     */
    public static <T> CollisionHandler<T> createWithDefaultConfig(final CollisionDetector<T> collisionDetector) {
	final double penetrationTolerence = 0.05;
	final double penetrationCorrectionFraction = 0.1;
	return new CollisionHandler<>(collisionDetector, penetrationTolerence, penetrationCorrectionFraction, new ArrayList<>());
    }

    /**
     * Registers a {@link CollisionListener} to this collision handler
     *
     * @param listener The listener to add
     */
    public void addListener(final CollisionListener<T> listener) {
	listeners.add(listener);
    }

    private void notifyListeners(final CollisionData<T> collision) {
	for (CollisionListener<T> listener : listeners) {
	    listener.collisionOccurred(collision);
	}
    }

    /**
     * Generates a collective constraint for each collision by first detecting the collisions using the
     * collision detector. Also notifies each listener about the collisions that are detected.
     *
     * @param deltaTime The size of the time step after solving the collisions
     * @return The collective velocity constraint used to solve the collisions
     */
    @Override public ActiveVelocityConstraint generateConstraint(final double deltaTime) {
	final Collection<ActiveVelocityConstraint> subConstraints = detectCollisionsAndGenerateConstraints(deltaTime);
	return ActiveVelocityConstraintList.createWithSingleIteration(subConstraints);
    }

    private Collection<ActiveVelocityConstraint> detectCollisionsAndGenerateConstraints(final double deltaTime) {
	final Collection<ActiveVelocityConstraint> subConstraints = new ArrayList<>();
	for (CollisionData<T> collision : collisionDetector.detectCollisions()) {
	    notifyListeners(collision);
	    subConstraints.add(ActiveCollisionConstraint.createFromCollisionData(
		    collision, deltaTime, penetrationTolerance, penetrationCorrectionFraction));
	}
	return subConstraints;
    }
}
