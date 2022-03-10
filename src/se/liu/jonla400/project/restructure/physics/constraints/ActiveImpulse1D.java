package se.liu.jonla400.project.restructure.physics.constraints;

import se.liu.jonla400.project.math.Interval;

public class ActiveImpulse1D
{
    private double impulse;

    public ActiveImpulse1D() {
	impulse = 0;
    }

    public double get() {
	return impulse;
    }

    public double update(final double targetDeltaVel, final double mass, final Interval impulseRange) {
	final double targetDeltaImpulse = targetDeltaVel * mass;
	final double targetImpulse = impulse + targetDeltaImpulse;

	final double nextImpulse = impulseRange.clamp(targetImpulse);
	final double deltaImpulse = nextImpulse - impulse;
	impulse = nextImpulse;

	return deltaImpulse;
    }
}
