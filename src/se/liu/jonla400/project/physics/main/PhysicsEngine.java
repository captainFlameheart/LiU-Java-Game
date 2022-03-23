package se.liu.jonla400.project.physics.main;

import se.liu.jonla400.project.physics.constraint.IterativeVelocityConstrainer;
import se.liu.jonla400.project.physics.constraint.VelocityConstrainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class PhysicsEngine
{
    private Collection<Body> bodies;
    private IterativeVelocityConstrainer iterativeVelConstrainer;

    private PhysicsEngine(final Collection<Body> bodies, final IterativeVelocityConstrainer iterativeVelConstrainer) {
	this.bodies = bodies;
	this.iterativeVelConstrainer = iterativeVelConstrainer;
    }

    public static PhysicsEngine createWithDefaultVelIterations() {
	return new PhysicsEngine(new ArrayList<>(), new IterativeVelocityConstrainer(10));
    }

    public void add(final Body... bodies) {
	this.bodies.addAll(Arrays.asList(bodies));
    }

    public void add(final VelocityConstrainer... velConstrainer) {
	iterativeVelConstrainer.add(velConstrainer);
    }

    public void tick(final double deltaTime) {
	iterativeVelConstrainer.generateConstraint(deltaTime).updateImpulse();
	bodies.forEach(b -> b.tick(deltaTime));
    }
}
