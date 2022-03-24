package se.liu.jonla400.project.physics.constraint;

/**
 * Represents an active velocity constraint of one or more
 * {@link se.liu.jonla400.project.physics.main.Body} objects before a time step occurs.
 * An active velocity constraint keeps track of the current impulse(s) being applied, and
 * can update the impulse on demand according to the current velocities of the involved bodies.
 */
public interface ActiveVelocityConstraint
{
    /**
     * Updates the impulse(s) currently applied to the bodies to try to satisfy
     * this constraint
     */
    void updateImpulse();
}
