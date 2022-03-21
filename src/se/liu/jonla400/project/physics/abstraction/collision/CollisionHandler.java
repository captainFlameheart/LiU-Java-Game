package se.liu.jonla400.project.physics.abstraction.collision;

import se.liu.jonla400.project.physics.abstraction.constraint.ActiveIterativeVelocityConstraint;
import se.liu.jonla400.project.physics.abstraction.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.abstraction.constraint.VelocityConstrainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollisionHandler<T> implements VelocityConstrainer
{
    private CollisionDetector<T> collisionDetector;
    private int iterations;

    private List<CollisionListener<T>> listeners;

    public CollisionHandler(final CollisionDetector<T> collisionDetector, final int iterations) {
	this.collisionDetector = collisionDetector;
	this.iterations = iterations;
	listeners = new ArrayList<>();
    }

    public CollisionHandler(final CollisionDetector<T> collisionDetector) {
	this(collisionDetector, 1);
    }

    public void addListener(final CollisionListener<T> listener) {
	listeners.add(listener);
    }

    private void notifyListeners(final CollisionData<T> collision) {
	for (CollisionListener<T> listener : listeners) {
	    listener.collisionOccured(collision);
	}
    }

    @Override public ActiveVelocityConstraint generateConstraint(final double deltaTime) {
	final Collection<ActiveVelocityConstraint> subConstraints = detectCollisionsAndGenerateConstraints(deltaTime);
	return new ActiveIterativeVelocityConstraint(iterations, subConstraints);
    }

    private Collection<ActiveVelocityConstraint> detectCollisionsAndGenerateConstraints(final double deltaTime) {
	final Collection<ActiveVelocityConstraint> subConstraints = new ArrayList<>();
	for (CollisionData<T> collision : collisionDetector.detectCollisions()) {
	    notifyListeners(collision);
	    subConstraints.add(new ActiveCollisionConstraint(collision, deltaTime, 0.05, 0.1));
	}
	return subConstraints;
    }

}
