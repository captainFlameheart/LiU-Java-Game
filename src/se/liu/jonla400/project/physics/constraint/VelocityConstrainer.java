package se.liu.jonla400.project.physics.constraint;

/**
 * Represents a generator of an {@link ActiveVelocityConstraint} given the size of the time step that
 * occurs after the velocity constraint has been solved
 */
public interface VelocityConstrainer
{
    /**
     * Generates an {@link ActiveVelocityConstraint} based on the size of the time step that occurs
     * after the velocity constraint has been solved
     *
     * @param deltaTime The size of the time step after the constraint has been solved
     * @return The generated ActiveVelocityConstraint
     */
    ActiveVelocityConstraint generateConstraint(double deltaTime);
}
