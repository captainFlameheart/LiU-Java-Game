package se.liu.jonla400.project.temp.rootfinding;

import se.liu.jonla400.project.restructure.math.Interval;

import java.util.Optional;

public class SmallErrorFinder
{
    private int globalSearchMaxSteps;
    private int localSearchMaxIterations;
    private double errorTolerence;

    public SmallErrorFinder(final int globalSearchMaxSteps, final int localSearchMaxIterations, final double errorTolerence) {
	if (globalSearchMaxSteps < 1) {
	    throw new IllegalArgumentException("Global search maximum steps less than 1: " + globalSearchMaxSteps);
	}
	if (errorTolerence < 0) {
	    throw new IllegalArgumentException("Negative error tolerence: " + errorTolerence);
	}
	if (localSearchMaxIterations < 1) {
	    throw new IllegalArgumentException("Local search iterations less than 1: " + localSearchMaxIterations);
	}

	this.globalSearchMaxSteps = globalSearchMaxSteps;
	this.errorTolerence = errorTolerence;
	this.localSearchMaxIterations = localSearchMaxIterations;
    }

    public <R extends ErrorState> Optional<InputToErrorStateMap<R>> findSmallErrorState(
	    final ContinousErrorStateFunction<R> function, final Interval inputInterval)
    {
	final Optional<ZeroErrorIsolation> zeroErrorIsolation = performGlobalSearch(function, inputInterval);
	if (zeroErrorIsolation.isEmpty()) {
	    return Optional.empty();
	}
	return performLocalSearch(function, zeroErrorIsolation.get());
    }

    private Optional<ZeroErrorIsolation> performGlobalSearch(final ContinousErrorStateFunction<?> function, final Interval inputInterval) {
	final double startInput = inputInterval.getStart();
	final double stepSize = inputInterval.getStartToEndDisplacement() / globalSearchMaxSteps;

	double prevInput = startInput;
	double prevError = getError(function, startInput);

	for (int i = 1; i <= globalSearchMaxSteps; i++) {
	    final double input = startInput + i * stepSize;
	    final double error = getError(function, input);

	    if (crossedZero(prevError, error)) {
		return Optional.of(new ZeroErrorIsolation(
			new Interval(prevInput, input),
			new Interval(prevError, error)
		));
	    }

	    prevInput = input;
	    prevError = error;
	}

	return Optional.empty();
    }

    private static double getError(final ContinousErrorStateFunction<?> function, final double input) {
	return function.getErrorStateOf(input).getError();
    }

    private static boolean crossedZero(final double prevError, final double error) {
	return (prevError >= 0 && error <= 0) || (prevError <= 0 && error >= 0);
    }

    private <R extends ErrorState> Optional<InputToErrorStateMap<R>> performLocalSearch(
	    final ContinousErrorStateFunction<R> function, final ZeroErrorIsolation zeroErrorIsolation)
    {
	// Important! Assumes the zero error isolation is valid (i.e. the error interval crosses 0)

	final Interval positiveToNegativePointers = zeroErrorIsolation.getPositiveToNegativePointers();
	double positivePointer = positiveToNegativePointers.getStart();
	double negativePointer = positiveToNegativePointers.getEnd();

	for (int i = 0; i < localSearchMaxIterations; i++) {
	    final double mid = (positivePointer + negativePointer) / 2;
	    final R errorState = function.getErrorStateOf(mid);
	    final double error = errorState.getError();

	    if (error > 0) {
		if (error < errorTolerence) {
		    return Optional.of(new InputToErrorStateMap<>(mid, errorState));
		}
		positivePointer = mid;
	    } else {
		if (error > -errorTolerence) {
		    return Optional.of(new InputToErrorStateMap<>(mid, errorState));
		}
		negativePointer = mid;
	    }
	}
	return Optional.empty();
    }

    private static class ZeroErrorIsolation
    {
	private Interval inputInterval;
	private Interval errorInterval;

	private ZeroErrorIsolation(final Interval inputInterval, final Interval errorInterval) {
	    this.inputInterval = inputInterval;
	    this.errorInterval = errorInterval;
	}

	private Interval getPositiveToNegativePointers() {
	    return inputInterval.flipIf(errorInterval.getStart() < errorInterval.getEnd());
	}
    }
}
