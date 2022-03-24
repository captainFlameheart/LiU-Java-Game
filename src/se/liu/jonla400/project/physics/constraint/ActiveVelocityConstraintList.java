package se.liu.jonla400.project.physics.constraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Represents a list of {@link ActiveVelocityConstraint}s that can be solved one-by-one.
 * Optionally an iteration count can be set, describing how many times to solve each
 * constraint one-by-one each time this whole list is attempted to be solved. The more iterations,
 * the more likely it is that the constraints will converge to a global solution.
 */
public class ActiveVelocityConstraintList implements ActiveVelocityConstraint
{
    private List<ActiveVelocityConstraint> subConstraints;
    private int iterations;

    private ActiveVelocityConstraintList(final List<ActiveVelocityConstraint> subConstraints, final int iterations) {
	this.subConstraints = subConstraints;
	this.iterations = iterations;
    }

    /**
     * Creates an ActiveVelocityConstraintList of the given constraints and with the given number of iterations used
     * when trying to converge to global solution. No reference is kept to the given collection of sub constraints.
     *
     * @param subConstraints The constraints to fill the list with, in the order of the collections iterator.
     * @param iterations The number of times the constraints are solved one after the other when this whole list is attempted to be solved
     * @return The created ActiveVelocityConstraintList
     */
    public static ActiveVelocityConstraintList create(final Collection<ActiveVelocityConstraint> subConstraints, final int iterations) {
	return new ActiveVelocityConstraintList(new ArrayList<>(subConstraints), iterations);
    }

    /**
     * Creates an ActiveVelocityConstraintList of the given constraints that, when attempted to be solved, goes through each sub
     * constraint once and solves them individually. No reference is kept to the given collection of sub constraints.
     *
     * @param subConstraints The constraints to fill the list with, in the order of the collection's iterator.
     * @return The created ActiveVelocityConstraintList
     */
    public static ActiveVelocityConstraintList createWithSingleIteration(final Collection<ActiveVelocityConstraint> subConstraints) {
	return create(subConstraints, 1);
    }

    /**
     * Creates an ActiveVelocityConstraintList of the given constraints that, when attempted to be solved, goes through each sub
     * constraint once and solves them individually. No reference is kept to the given array of sub constraints.
     *
     * @param subConstraints The constraints to fill the list with, in the order of the array
     * @return The created ActiveVelocityConstraintList
     */
    public static ActiveVelocityConstraintList createWithSingleIteration(final ActiveVelocityConstraint... subConstraints) {
	return createWithSingleIteration(Arrays.asList(subConstraints));
    }

    /**
     * Goes through each sub constraint and solves it individually. Does this for the given number
     * of iterations
     */
    @Override public void updateImpulse() {
	for (int i = 0; i < iterations; i++) {
	    subConstraints.forEach(ActiveVelocityConstraint::updateImpulse);
	}
    }
}
