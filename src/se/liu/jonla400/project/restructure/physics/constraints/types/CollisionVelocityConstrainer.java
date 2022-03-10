package se.liu.jonla400.project.restructure.physics.constraints.types;

import se.liu.jonla400.project.math.Interval;
import se.liu.jonla400.project.math.Matrix22;
import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.PointMass;
import se.liu.jonla400.project.physics.collision.collisioninfo.Collision;
import se.liu.jonla400.project.physics.constraints.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.constraints.VelocityConstrainer;
import se.liu.jonla400.project.temp.ActiveImpulse;

public class CollisionVelocityConstrainer implements VelocityConstrainer
{
    private final static Interval NORMAL_IMPULSE_RANGE = new Interval(0, Double.POSITIVE_INFINITY);

    private Collision collision;
    private int iterations;
    private boolean prioritizeSeparation;

    public CollisionVelocityConstrainer(final Collision collision) {
	this.collision = collision;
	iterations = 1;
	prioritizeSeparation = true;
    }

    /**
     * Sets the number of times to solve the separation and friction constraints individually
     * in the hope of approximating a common solution
     *
     * @param iterations The number of iterations
     */
    public void setIterations(final int iterations) {
	if (iterations < 0) {
	    throw new IllegalArgumentException("Negative number of iterations: " + iterations);
	}
	this.iterations = iterations;
    }

    /**
     * Sets whether the separation constraint should get an exact solution, potentially
     * at the cost of not fully satisfying the friction constraint. This is done
     * by solving the separation constraint one extra time after iteratively solving
     * the separation and friction constraints individually.
     *
     * @param prioritizeSeparation Whether the separation constraint should be prioritized
     */
    public void setPrioritizeSeparation(final boolean prioritizeSeparation) {
	this.prioritizeSeparation = prioritizeSeparation;
    }

    @Override public ActiveVelocityConstraint initActiveVelConstraint(final double deltaTime) {
	final Matrix22 contactPointInvMass = collision.getPointMass().getInvMassAt(collision.getContactPointOffset());
	final double normalMass = projectMassAlongDirection(contactPointInvMass, collision.getNormal());
	final double tangentMass = projectMassAlongDirection(contactPointInvMass, collision.getTangent());

	final double initialNormalVel = getNormalVel();
	final double targetNormalVel = initialNormalVel < 0 ? -collision.getBounceCoefficient() * initialNormalVel : 0;

	return new ActiveConstraint(normalMass, tangentMass, targetNormalVel);
    }

    /**
     * Returns the mass seen by an impulse applied to a point with the given inverted mass in
     * the given direction.
     *
     * Mathematical explanation:
     *
     * Let j (scalar) be an arbitrary impulse applied in the given direction.
     * Let Δv (svalar) be the change in velocity along the given direction that j causes
     * This method returns the scalar m such that j = m * Δv
     *
     * @param invMass The inverted mass of the point to apply the impulse to
     * @param dir The direction along which to apply the impulse
     * @return The seen mass along the direction
     */
    private static double projectMassAlongDirection(final Matrix22 invMass, final Vector2D dir) {
	// See derivation in report
	final double dirX = dir.getX();
	final double dirY = dir.getY();
	final double commonWeight = dirX * dirY;
	final Matrix22 weights = new Matrix22(new double[][]{
		{dirX * dirX, commonWeight},
		{commonWeight, dirY * dirY}
	});

	final double invProjectedMass = invMass.getWeightedSum(weights);
	return 1 / invProjectedMass;
    }

    private class ActiveConstraint implements ActiveVelocityConstraint
    {
	// Pre-computed values
	private double normalMass;
	private double tangentMass;
	private double targetNormalVel;

	private ActiveImpulse normalImpulse;
	private ActiveImpulse tangentImpulse;

	private ActiveConstraint(final double normalMass, final double tangentMass, final double targetNormalVel) {
	    this.normalMass = normalMass;
	    this.tangentMass = tangentMass;
	    this.targetNormalVel = targetNormalVel;

	    normalImpulse = new ActiveImpulse();
	    tangentImpulse = new ActiveImpulse();
	}

	@Override public void updateSolution() {
	    for (int i = 0; i < iterations; i++) {
		updateNormalImpulse();
		updateTangentImpulse(); // Depends on the normal impulse
	    }
	    if (prioritizeSeparation) {
		updateNormalImpulse(); // Satisfy the separation constraint last, which might break the friction constraint
	    }
	}

	private void updateNormalImpulse() {
	    final double targetDeltaNormalVel = targetNormalVel - getNormalVel();
	    final double deltaNormalImpulse = normalImpulse.update(targetDeltaNormalVel, normalMass, NORMAL_IMPULSE_RANGE);
	    applyNormalImpulse(deltaNormalImpulse);
	}

	private void updateTangentImpulse() {
	    final double targetDeltaTangentVel = -getTangentVel();
	    final double maxTangentImpulse = collision.getFrictionCoefficient() * normalImpulse.get();
	    final double deltaTangentImpulse = tangentImpulse.update(
		    targetDeltaTangentVel, tangentMass, new Interval(-maxTangentImpulse, maxTangentImpulse)
	    );
	    applyTangentImpulse(deltaTangentImpulse);
	}
    }

    private double getNormalVel() {
	return getContactPointVelAlong(collision.getNormal());
    }

    private double getTangentVel() {
	return getContactPointVelAlong(collision.getTangent());
    }

    private double getContactPointVelAlong(final Vector2D dir) {
	final PointMass pointMass = collision.getPointMass();
	final Vector2D contactPointOffset = collision.getContactPointOffset();
	Vector2D contactPointVel = pointMass.getVelAt(contactPointOffset);
	return dir.dot(contactPointVel);
    }

    private void applyNormalImpulse(final double normalImpulse) {
	applyImpulse(collision.getNormal(), normalImpulse);
    }

    private void applyTangentImpulse(final double tangentImpulse) {
	applyImpulse(collision.getTangent(), tangentImpulse);
    }

    private void applyImpulse(final Vector2D dir, final double impulse) {
	final PointMass pointMass = collision.getPointMass();
	final Vector2D contactPointOffset = collision.getContactPointOffset();
	pointMass.applyOffsetImpulse(contactPointOffset, dir.multiply(impulse));
    }
}
