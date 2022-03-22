package se.liu.jonla400.project.physics.abstraction.collision;

import se.liu.jonla400.project.physics.abstraction.constraint.ActiveVelocityConstraintList;
import se.liu.jonla400.project.physics.abstraction.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.abstraction.constraint.VelocityConstrainer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollisionHandler<T> implements VelocityConstrainer
{
    private CollisionDetector<T> collisionDetector;
    private double penetrationTolerence;
    private double penetrationCorrectionFraction;
    private List<CollisionListener<T>> listeners;

    public CollisionHandler(final CollisionDetector<T> collisionDetector, final double penetrationTolerence,
			    final double penetrationCorrectionFraction, final List<CollisionListener<T>> listeners)
    {
	this.collisionDetector = collisionDetector;
	this.penetrationTolerence = penetrationTolerence;
	this.penetrationCorrectionFraction = penetrationCorrectionFraction;
	this.listeners = listeners;
    }

    public static <T> CollisionHandler<T> createWithDefaultConfig(final CollisionDetector<T> collisionDetector) {
	final double penetrationTolerence = 0.05;
	final double penetrationCorrectionFraction = 0.1;
	return new CollisionHandler<>(collisionDetector, penetrationTolerence, penetrationCorrectionFraction, new ArrayList<>());
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
	return ActiveVelocityConstraintList.createWithSingleIteration(subConstraints);
    }

    private Collection<ActiveVelocityConstraint> detectCollisionsAndGenerateConstraints(final double deltaTime) {
	final Collection<ActiveVelocityConstraint> subConstraints = new ArrayList<>();
	for (CollisionData<T> collision : collisionDetector.detectCollisions()) {
	    notifyListeners(collision);
	    subConstraints.add(ActiveCollisionConstraint.createFromCollisionData(
		    collision, deltaTime, penetrationTolerence, penetrationCorrectionFraction));
	}
	return subConstraints;
    }

}
