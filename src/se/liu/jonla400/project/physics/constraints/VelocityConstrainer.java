package se.liu.jonla400.project.physics.constraints;

/**
 * Represents an object that can indirectly constrain the velocity of an arbitrary
 * physical system by initializing a new ActiveVelocityConstraint.
 */
public interface VelocityConstrainer
{
    /**
     * Initializes a new ActiveVelocityConstraint on an arbitrary physical system.
     * The velocity constraint might depend on the size of the upcoming time step.
     * The velocity constraint is only required to work for the current position of
     * the system.
     *
     * @param deltaTime The size of the upcoming time step
     * @return The new ActiveVelocityConstraint
     */
    ActiveVelocityConstraint initActiveVelConstraint(double deltaTime);
}
