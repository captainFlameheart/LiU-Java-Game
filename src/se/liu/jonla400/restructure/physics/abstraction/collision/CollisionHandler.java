package se.liu.jonla400.restructure.physics.abstraction.collision;

import se.liu.jonla400.restructure.physics.abstraction.constraint.ActiveIterativeVelocityConstraint;
import se.liu.jonla400.restructure.physics.abstraction.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.restructure.physics.abstraction.constraint.VelocityConstrainer;

import java.util.Collection;
import java.util.stream.Collectors;

public class CollisionHandler implements VelocityConstrainer
{
    private CollisionDetector collisionDetector;
    private int iterations;

    public CollisionHandler(final CollisionDetector collisionDetector, final int iterations) {
	this.collisionDetector = collisionDetector;
	this.iterations = iterations;
    }

    public CollisionHandler(final CollisionDetector collisionDetector) {
	this(collisionDetector, 1);
    }

    @Override public ActiveVelocityConstraint generateConstraint(final double deltaTime) {
	return new ActiveIterativeVelocityConstraint(iterations, generateSubConstraints(deltaTime));
    }

    private Collection<ActiveVelocityConstraint> generateSubConstraints(final double deltaTime) {
	return collisionDetector.detectCollisions().stream().map(
		c -> new ActiveCollisionConstraint(c, deltaTime, 0.05, 0.1)
	).collect(Collectors.toList());
    }
}
