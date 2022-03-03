package se.liu.jonla400.project.physics.constraints;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a collection of velocity constrainers that tries to find a collective
 * global solution. This attempt is done through iterating over and updating the
 * solution to each velocity constraint for multiple iterations. This will hopefully
 * result in an approximation of a global solution through convergence. However, if
 * any pair of constraints contradict each other, no global solution exists.
 *
 * An object of this class is a VelocityConstrainer, and can thus be part of
 * a larger IterativeVelocityConstrainer
 */
public class IterativeVelocityConstrainer implements VelocityConstrainer
{
    private List<VelocityConstrainer> velConstrainers;
    private int iterations;

    /**
     * Creates a new iterative velocity constrainer
     *
     * @param iterations The number of times to solve the sub velocity constraints one-by-one
     * @param velConstrainers The sub velocity constrainers to find a global solution for
     */
    public IterativeVelocityConstrainer(final int iterations, VelocityConstrainer... velConstrainers) {
	this.iterations = iterations;
	this.velConstrainers = new ArrayList<>(Arrays.asList(velConstrainers));
    }

    /**
     * Adds a sub velocity constrainer
     *
     * @param velConstrainer The velocity constrainer to be added
     */
    public void add(final VelocityConstrainer velConstrainer) {
	velConstrainers.add(velConstrainer);
    }

    /**
     * Removes a sub velocity constrainer
     *
     * @param o
     * @return
     */
    public boolean remove(final Object o) {
	return velConstrainers.remove(o);
    }

    /**
     * Initializes a new ActiveVelocityConstraint that, each time its solution is
     * updated, solves the collection of velocity constraints one-by-one for the
     * given number of iterations.
     *
     * @param deltaTime The size of the upcoming time step
     * @return The new ActiveVelocityConstraint
     */
    @Override public ActiveVelocityConstraint initActiveVelConstraint(final double deltaTime) {
	final List<ActiveVelocityConstraint> activeVelConstraints = initActiveVelConstraints(deltaTime);

	// Return an active velocity constraint that, when its solution is updated,
	// iterates over and solves each sub constraint separately
	return () -> {
	    for (int i = 0; i < iterations; i++) {
		activeVelConstraints.forEach(ActiveVelocityConstraint::updateSolution);
	    }
	};
    }

    private List<ActiveVelocityConstraint> initActiveVelConstraints(final double deltaTime) {
	return velConstrainers.stream().map(c -> c.initActiveVelConstraint(deltaTime)).toList();
    }
}
