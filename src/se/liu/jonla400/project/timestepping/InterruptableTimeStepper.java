package se.liu.jonla400.project.timestepping;

import java.util.Optional;

/**
 * Represents a time stepper that can be interrupted during a time step
 * with a routine that changes the outcome of the rest of the time step
 */
public class InterruptableTimeStepper implements TimeStepper
{
    private TimeStepper subTimeStepper;
    private InterruptGenerator interruptGenerator;

    /**
     * Creates a new interruptable time stepper with the given time stepper between interrupts
     * and the given generator of interrupts
     * @param subTimeStepper The time stepper that advances time between interrupts
     * @param interruptGenerator The generator of interrupts
     */
    public InterruptableTimeStepper(final TimeStepper subTimeStepper, final InterruptGenerator interruptGenerator) {
	this.subTimeStepper = subTimeStepper;
	this.interruptGenerator = interruptGenerator;
    }

    /**
     * Advances time by the given amount. If an interrupt occurs during this
     * time step, it will change the outcome of the rest of the time step.
     * @param deltaTime	The amount of time that should be advanced
     */
    @Override public void tick(final double deltaTime) {
	if (deltaTime < 0) {
	    throw new IllegalArgumentException("Negative delta time: " + deltaTime);
	}

	final Optional<Interrupt> possibleInterrupt = interruptGenerator.generateNextInterrupt(deltaTime);

	if (possibleInterrupt.isEmpty()) {
	    subTimeStepper.tick(deltaTime);
	    return;
	}

	final Interrupt interrupt = possibleInterrupt.get();
	final double timeUntilInterrupt = interrupt.getTime();

	if (timeUntilInterrupt >= deltaTime) {
	    subTimeStepper.tick(deltaTime);
	    return;
	}

	subTimeStepper.tick(timeUntilInterrupt);
	final double timeLeft = deltaTime - timeUntilInterrupt;
	interrupt.performRoutine(timeLeft);
	tick(timeLeft);
    }
}
