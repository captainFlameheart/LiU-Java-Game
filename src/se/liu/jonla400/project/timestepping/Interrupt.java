package se.liu.jonla400.project.timestepping;

/**
 * Represents an interrupt during a time step that changes
 * the outcome of the rest of the time step
 */
public class Interrupt
{
    private double time;
    private InterruptRoutine routine;

    /**
     * Creates a time step interrupt that occurs at the given time from
     * the start of the time step and performs the given routine
     * @param time The time from the start of the time step
     * @param routine The routine to be performed
     */
    public Interrupt(final double time, final InterruptRoutine routine) {
	if (time < 0) {
	    throw new IllegalArgumentException("Negative time: " + time);
	}

	this.time = time;
	this.routine = routine;
    }

    /**
     * @return The time this interrupt occurs since the start of the time step
     */
    public double getTime() {
	return time;
    }

    /**
     * Performs the routine of this time step interrupt
     * @param timeLeft The amount of time left in the time step
     */
    public void performRoutine(final double timeLeft) {
	routine.perform(timeLeft);
    }
}
