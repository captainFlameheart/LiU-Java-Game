package se.liu.jonla400.project.physics.abstraction.constraint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ActiveVelocityConstraintList implements ActiveVelocityConstraint
{
    private List<ActiveVelocityConstraint> subConstraints;
    private int iterations;

    private ActiveVelocityConstraintList(final List<ActiveVelocityConstraint> subConstraints, final int iterations) {
	this.subConstraints = subConstraints;
	this.iterations = iterations;
    }

    public static ActiveVelocityConstraintList create(final Collection<ActiveVelocityConstraint> subConstraints, final int iterations) {
	return new ActiveVelocityConstraintList(new ArrayList<>(subConstraints), iterations);
    }

    public static ActiveVelocityConstraintList createWithSingleIteration(final Collection<ActiveVelocityConstraint> subConstraints) {
	return create(subConstraints, 1);
    }

    public static ActiveVelocityConstraintList createWithSingleIteration(final ActiveVelocityConstraint... subConstraints) {
	return createWithSingleIteration(Arrays.asList(subConstraints));
    }

    @Override public void updateImpulse() {
	for (int i = 0; i < iterations; i++) {
	    subConstraints.forEach(ActiveVelocityConstraint::updateImpulse);
	}
    }
}
