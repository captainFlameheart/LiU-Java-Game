package se.liu.jonla400.project.physics.main;

import se.liu.jonla400.project.physics.constraint.IterativeVelocityConstrainer;
import se.liu.jonla400.project.physics.constraint.VelocityConstrainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Represents a physics engine that keeps track of {@link Body} objects and
 * {@link VelocityConstrainer} objects constraining the bodies' velocities. When asking
 * the physics engine to advance time forward, the velocity constraints are first attempted
 * to be solved, followed by moving the bodies according to their velocities.
 */
public class PhysicsEngine
{
    private Collection<Body> bodies;
    private IterativeVelocityConstrainer iterativeVelConstrainer;

    private PhysicsEngine(final Collection<Body> bodies, final IterativeVelocityConstrainer iterativeVelConstrainer) {
	this.bodies = bodies;
	this.iterativeVelConstrainer = iterativeVelConstrainer;
    }

    /**
     * Creates a new PhysicsEngine with a default number of iterations for solving the velocity
     * constraints. More iterations often means that a more accurate approximation of a global
     * solution for the constraints is found.
     *
     * @return The created PhysicsEngine
     */
    public static PhysicsEngine createWithDefaultVelIterations() {
	return new PhysicsEngine(new ArrayList<>(), new IterativeVelocityConstrainer(10));
    }

    /**
     * @param bodies The bodies to be added
     */
    public void add(final Body... bodies) {
	this.bodies.addAll(Arrays.asList(bodies));
    }

    /**
     * @param velConstrainer The velocity constrainers to be added
     */
    public void add(final VelocityConstrainer... velConstrainer) {
	iterativeVelConstrainer.add(velConstrainer);
    }

    /**
     * Ticks time forward by first constraining the velocities of the bodies and then moving
     * the bodies along their velocities
     *
     * @param deltaTime The size of the time step
     */
    public void tick(final double deltaTime) {
	iterativeVelConstrainer.generateConstraint(deltaTime).updateImpulse();
	bodies.forEach(b -> b.tick(deltaTime));
    }
}
