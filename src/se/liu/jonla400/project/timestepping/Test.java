package se.liu.jonla400.project.timestepping;

import java.util.Optional;

public class Test
{
    public static void main(String[] args) {
	// Tests time stepping with interrupts.
	// Moves a point between two boundaries for
	// a number of time steps. Time step interrupts
	// occur when the point collides with the boundaries.
	// The position and velocity of the point is
	// printed to the console at key moments

	final Point point = new Point(0, 1);
	final Boundary leftBoundary = new Boundary(-2.5, point);
	final Boundary rightBoundary = new Boundary(2.5, point);

	final double timeStepDeltaTime = 1;
	final int timeSteps = 15;

	TimeStepper timeStepper = new InterruptableTimeStepper(
		point,
		new InterruptGeneratorQueue(leftBoundary, rightBoundary)
	);

	for (int i = 0; i < timeSteps; i++) {
	    System.out.println("Before tick: " + point);
	    timeStepper.tick(timeStepDeltaTime);
	    System.out.println("After tick: " + point + "\n");
	}
    }

    private static class Point implements TimeStepper
    {
	private double pos;
	private double vel;

	private Point(final double pos, final double vel) {
	    this.pos = pos;
	    this.vel = vel;
	}

	@Override public void tick(final double deltaTime) {
	    pos += vel * deltaTime;
	}

	@Override public String toString() {
	    return "Point{" + "pos=" + pos + ", vel=" + vel + '}';
	}
    }

    private static class Boundary implements InterruptGenerator
    {
	private double pos;
	private Point target;

	private Boundary(final double pos, final Point target) {
	    this.pos = pos;
	    this.target = target;
	}

	@Override public Optional<Interrupt> generateNextInterrupt(final double timeLeft) {
	    final double relativePos = pos - target.pos;
	    final double timeUntilCollision = relativePos / target.vel;

	    if (timeUntilCollision < 0 || timeUntilCollision >= timeLeft) {
		return Optional.empty();
	    }

	    return Optional.of(new Interrupt(
		    timeUntilCollision,
		    ignored -> {
			System.out.println("Before solving collision: " + target);
			target.vel *= -1;

			// Move the position of the point a tiny bit towards where it came from.
			// Otherwise the point will never stop colliding with this boundary
			target.pos += Math.copySign(0.01, target.vel);

			System.out.println("After solving collision: " + target);
		    })
	    );
	}
    }
}
