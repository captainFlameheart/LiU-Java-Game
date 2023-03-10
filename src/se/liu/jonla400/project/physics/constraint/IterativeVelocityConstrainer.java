package se.liu.jonla400.project.physics.constraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a list of velocity constrainers that tries to find a collective
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
    private List<VelocityConstrainer> subConstrainers;
    private int iterations;

    /**
     * Creates a new iterative velocity constrainer
     *
     * @param iterations The number of times to solve the sub velocity constraints one-by-one
     * @param subConstrainers The sub velocity constrainers to find a global solution for
     */
    public IterativeVelocityConstrainer(final int iterations, final VelocityConstrainer... subConstrainers) {
	this.iterations = iterations;
	this.subConstrainers = new ArrayList<>(Arrays.asList(subConstrainers));
    }

    /**
     * Adds sub velocity constrainers
     *
     * @param subConstrainer The velocity constrainers to be added
     */
    public void add(final VelocityConstrainer... subConstrainer) {
	subConstrainers.addAll(Arrays.asList(subConstrainer));
    }

    /**
     * Generates a new {@link ActiveVelocityConstraint} that, each time its solution is
     * updated, solves the collection of velocity constraints one-by-one for the
     * given number of iterations.
     *
     * @param deltaTime The size of the time step after the constraints have been solved
     * @return The generated {@link ActiveVelocityConstraint}
     */
    @Override public ActiveVelocityConstraint generateConstraint(final double deltaTime) {
	final List<ActiveVelocityConstraint> subConstraints = generateSubConstraints(deltaTime);

	// Return an active velocity constraint that, when its solution is updated,
	// iterates over and solves each sub constraint separately
	return ActiveVelocityConstraintList.create(subConstraints, iterations);
    }

    private List<ActiveVelocityConstraint> generateSubConstraints(final double deltaTime) {
	return subConstrainers.stream().map(c -> c.generateConstraint(deltaTime)).toList();
    }
}
