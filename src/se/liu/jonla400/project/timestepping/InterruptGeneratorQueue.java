package se.liu.jonla400.project.timestepping;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Represents a group of time step interrupt generators, where the earliest
 * interrupt generated from these will be next interrupt (if any).
 */
public class InterruptGeneratorQueue implements InterruptGenerator
{
    private List<InterruptGenerator> generators;

    /**
     * Creates a new group of interrupt generators
     *
     * @param generators The interrupt generators in this group
     */
    public InterruptGeneratorQueue(InterruptGenerator... generators) {
	this.generators = Arrays.asList(generators);
    }

    /**
     * Generates the earliest interrupt from the group of
     * generators, if any
     *
     * @param timeLeft The time left of the time step
     * @return The earliest interrupt, if any
     */
    @Override public Optional<Interrupt> generateNextInterrupt(final double timeLeft) {
	Interrupt firstInterrupt = null;
	double firstInterruptTime = Double.POSITIVE_INFINITY;

	for (InterruptGenerator generator : generators) {
	    final Optional<Interrupt> possibleInterrupt = generator.generateNextInterrupt(timeLeft);
	    if (possibleInterrupt.isEmpty()) {
		continue;
	    }

	    final Interrupt interrupt = possibleInterrupt.get();
	    final double interruptTime = interrupt.getTime();

	    if (interruptTime < firstInterruptTime) {
		firstInterrupt = interrupt;
		firstInterruptTime = interruptTime;
	    }
	}
	return Optional.ofNullable(firstInterrupt);
    }
}
