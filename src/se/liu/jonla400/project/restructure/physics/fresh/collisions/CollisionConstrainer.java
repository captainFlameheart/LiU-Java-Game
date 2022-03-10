package se.liu.jonla400.project.restructure.physics.fresh.collisions;

import se.liu.jonla400.project.restructure.physics.constraints.ActiveIterativeVelocityConstraint;
import se.liu.jonla400.project.restructure.physics.constraints.ActiveVelocityConstraint;
import se.liu.jonla400.project.restructure.physics.constraints.VelocityConstrainer;
import se.liu.jonla400.project.restructure.physics.fresh.collisions.collisiondetection.CollisionDetector;

import java.util.Collection;
import java.util.stream.Collectors;

public class CollisionConstrainer implements VelocityConstrainer
{
    private CollisionDetector collisionDetector;
    private int iterations;

    public CollisionConstrainer(final CollisionDetector collisionDetector, final int iterations) {
	this.collisionDetector = collisionDetector;
	this.iterations = iterations;
    }

    public CollisionConstrainer(final CollisionDetector collisionDetector) {
	this(collisionDetector, 1);
    }

    @Override public ActiveVelocityConstraint generateConstraint(final double deltaTime) {
	return new ActiveIterativeVelocityConstraint(iterations, generateSubConstraints());
    }

    private Collection<ActiveVelocityConstraint> generateSubConstraints() {
	return collisionDetector.detectCollisions().stream().map(ActiveCollisionConstraint::new).collect(Collectors.toList());
    }
}
