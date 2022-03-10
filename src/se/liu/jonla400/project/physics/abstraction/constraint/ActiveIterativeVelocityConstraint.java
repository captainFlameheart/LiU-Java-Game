package se.liu.jonla400.project.physics.abstraction.constraint;

import java.util.Collection;

public class ActiveIterativeVelocityConstraint implements ActiveVelocityConstraint
{
    private int iterations;
    private Collection<ActiveVelocityConstraint> subConstraints;

    public ActiveIterativeVelocityConstraint(final int iterations, final Collection<ActiveVelocityConstraint> subConstraints) {
	this.iterations = iterations;
	this.subConstraints = subConstraints;
    }

    public ActiveIterativeVelocityConstraint(final Collection<ActiveVelocityConstraint> subConstraints) {
	this(1, subConstraints);
    }

    @Override public void updateImpulse() {
	for (int i = 0; i < iterations; i++) {
	    subConstraints.forEach(ActiveVelocityConstraint::updateImpulse);
	}
    }
}
