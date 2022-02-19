package se.liu.jonla400.project.timestepping;

/**
 * Represents a routine that is performed during a time step and
 * changes the outcome of the rest of the time step
 */
public interface InterruptRoutine
{
    /**
     * Performs the routine during a time step
     * @param timeLeft The amount of time left in the time step
     */
    void perform(double timeLeft);
}
