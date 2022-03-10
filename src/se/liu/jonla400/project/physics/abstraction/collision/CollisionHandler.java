package se.liu.jonla400.project.physics.abstraction.collision;

import se.liu.jonla400.project.physics.abstraction.constraint.ActiveIterativeVelocityConstraint;
import se.liu.jonla400.project.physics.abstraction.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.abstraction.constraint.VelocityConstrainer;

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
	return new ActiveIterativeVelocityConstraint(iterations, generateSubConstraints());
    }

    private Collection<ActiveVelocityConstraint> generateSubConstraints() {
	return collisionDetector.detectCollisions().stream().map(ActiveCollisionConstraint::new).collect(Collectors.toList());
    }
}
