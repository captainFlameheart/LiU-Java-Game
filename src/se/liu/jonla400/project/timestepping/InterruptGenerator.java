package se.liu.jonla400.project.timestepping;

import java.util.Optional;

/**
 * Represents an object that generates the next upcoming interrupt
 * of a time step, if any
 */
public interface InterruptGenerator
{
    /**
     * Generates the next upcoming interrupt of a time step, if any
     * @param timeLeft The time left of the time step
     * @return The next upcoming interrupt of the time step, if any
     */
    Optional<Interrupt> generateNextInterrupt(double timeLeft);
}
