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
    private int maxSubTimeSteps;

    /**
     * Creates a new interruptable time stepper with the given time stepper between interrupts
     * and the given generator of interrupts
     *
     * @param subTimeStepper The time stepper that advances time between interrupts
     * @param interruptGenerator The generator of interrupts
     * @param maxSubTimeSteps The maximum number of sub time stepping before interrupts are ignored
     */
    public InterruptableTimeStepper(final TimeStepper subTimeStepper, final InterruptGenerator interruptGenerator, final int maxTickIterations) {
	if (maxTickIterations < 0) {
	    throw new IllegalArgumentException("Negative maximum tick iterations: " + maxTickIterations);
	}

	this.subTimeStepper = subTimeStepper;
	this.interruptGenerator = interruptGenerator;
	this.maxSubTimeSteps = maxTickIterations;
    }

    /**
     * Advances time by the given amount by performing sub steps between interrupts.
     * An interrupt that occurs during this time step will change the outcome of
     * the rest of the time step. If too many sub steps occur, interrupts will be
     * ignored for the rest of the time step. The maximum number of sub time steps
     * is passed in as a constructor argument.
     *
     * @param deltaTime	The amount of time that should be advanced
     */
    @Override public void tick(double deltaTime) {
	if (deltaTime < 0) {
	    throw new IllegalArgumentException("Negative delta time: " + deltaTime);
	}

	for (int i = 0; i < maxSubTimeSteps; i++) {
	    final Optional<Interrupt> possibleInterrupt = getNextInterruptWithinTimeStep(deltaTime);

	    // If there is no upcoming interrupt, perform the rest of the time step without interrupts
	    if (possibleInterrupt.isEmpty()) {
		tickWithoutInterrupts(deltaTime);
		return;
	    }

	    // An interrupt occurs before the end of the time step
	    final Interrupt interrupt = possibleInterrupt.get();
	    final double timeUntilInterrupt = interrupt.getTime();
	    // Advance time until it's time to fire the interrupt
	    subTimeStepper.tick(timeUntilInterrupt);
	    deltaTime -= timeUntilInterrupt;
	    // Fire the interrupt
	    interrupt.performRoutine(deltaTime);
	}
	// The maximum number of tick iterations reached
	// Advance the rest of the time step without interrupts
	tickWithoutInterrupts(deltaTime);
    }

    private Optional<Interrupt> getNextInterruptWithinTimeStep(final double deltaTime) {
	// Ask the interrupt generator for the next interrupt
	final Optional<Interrupt> possibleInterrupt = interruptGenerator.generateNextInterrupt(deltaTime);

	// If no interrupt was returned, or the interrupt occurs after the end of the time step,
	// return no interrupt
	if (possibleInterrupt.isEmpty() || possibleInterrupt.get().getTime() >= deltaTime) {
	    return Optional.empty();
	}
	// Otherwise there is an interrupt within the time step, return it
	return possibleInterrupt;
    }

    private void tickWithoutInterrupts(final double deltaTime) {
	subTimeStepper.tick(deltaTime);
    }
}
