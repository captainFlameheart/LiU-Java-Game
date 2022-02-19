package se.liu.jonla400.project.timestepping;

/**
 * Represents an object that can advance time with discrete steps.
 */
public interface TimeStepper
{
    /**
     * Advances time by the given amount
     * @param deltaTime	The amount of time that should be advanced
     */
    void tick(double deltaTime);
}
