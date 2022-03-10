package se.liu.jonla400.project.restructure.physics.abstraction.main;

import se.liu.jonla400.project.restructure.physics.abstraction.constraint.IterativeVelocityConstrainer;
import se.liu.jonla400.project.restructure.physics.abstraction.constraint.VelocityConstrainer;

import java.util.ArrayList;
import java.util.Collection;

public class PhysicsEngine
{
    private Collection<Body> bodies;
    private IterativeVelocityConstrainer iterativeVelConstrainer;

    public PhysicsEngine(final int velConstraintIterations) {
	bodies = new ArrayList<>();
	iterativeVelConstrainer = new IterativeVelocityConstrainer(velConstraintIterations);
    }

    public void add(final Body body) {
	bodies.add(body);
    }

    public void remove(final Body body) {
	bodies.remove(body);
    }

    public void add(final VelocityConstrainer velConstrainer) {
	iterativeVelConstrainer.add(velConstrainer);
    }

    public void remove(final VelocityConstrainer velConstrainer) {
	iterativeVelConstrainer.remove(velConstrainer);
    }

    public void tick(final double deltaTime) {
	iterativeVelConstrainer.generateConstraint(deltaTime).updateImpulse();
	bodies.forEach(b -> b.tick(deltaTime));
    }

}
