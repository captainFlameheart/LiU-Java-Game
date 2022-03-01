package se.liu.jonla400.project.physics.constraints;

/**
 * Represents an active constraint on the velocity of an arbitrary physical system.
 * An active velocity constraint has some current solution, typically in the form
 * of an impulse, which can be updated to try to satisfy this constraint.
 *
 * After updating the solution to this constraint, other constraints acting on the same
 * system can break, and after updating their solutions this constraint can break.
 * To try to approximate a global solution to all of the constraints, one can iterate
 * and update the solution of each constraint for a fixed number of iterations in the hope
 * of convergense. See IterativeVelocityConstrainer for more details on this.*
 */
public interface ActiveVelocityConstraint
{
    /**
     * Updates the solution to this velocity constraint, typically by changing the impulse
     * that is currently being applied
     */
    void updateSolution();
}
