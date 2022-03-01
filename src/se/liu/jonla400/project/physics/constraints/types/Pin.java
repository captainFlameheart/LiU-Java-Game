package se.liu.jonla400.project.physics.constraints.types;

import se.liu.jonla400.project.math.Matrix22;
import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.PointMass;
import se.liu.jonla400.project.physics.constraints.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.constraints.VelocityConstrainer;

/**
 * Represents a constrainer of the velocity of a point mass that tries to restrict
 * one of its point to a given position. If the point on the point mass for any
 * reason gets away from the target position, the point will be steered towards the
 * target position through a velocity.
 */
public class Pin implements VelocityConstrainer
{
    private PointMass pointMass;
    private Vector2D localPoint;
    private Vector2D globalPoint;

    /**
     * Creates a new Pin
     *
     * @param pointMass The point mass whose velocity and angular velocity is to be constrained
     * @param localPoint The local point on the point mass to pin to a global point
     * @param globalPoint The global point to pin the local point to
     */
    public Pin(final PointMass pointMass, final Vector2D localPoint, final Vector2D globalPoint) {
	this.pointMass = pointMass;
	this.localPoint = localPoint;
	this.globalPoint = globalPoint;
    }

    /**
     * Initializes a new ActiveVelocityConstraint used to constrain the velocity of
     * the pinned point. The velocity constraint is only required to work for the
     * current position of the point mass.
     *
     * @param deltaTime The size of the time step after the velocity has been constrained
     * @return The new ActiveVelocityConstraint
     */
    @Override public ActiveVelocityConstraint initActiveVelConstraint(final double deltaTime) {
	// Get the current offset of the point on the point mass
	final Vector2D offset = pointMass.convertLocalPointToOffset(localPoint);
	// Get the current inverted mass of the point on the point mass
	final Matrix22 invMass = pointMass.getInvMassAt(offset);

	// Return an active velocity constraint that does the following when its solution is updated
	return () -> {
	    // Get the current velocity of the point on the point mass
	    final Vector2D vel = pointMass.getVelAt(offset);
	    // We want a velocity of zero, so the change in velocity we want is the
	    // negation of the current velocity
	    final Vector2D deltaVel = vel.negate();
	    // Compute the impulse that must be applied at to the point to get the desired
	    // change in velocity
	    final Vector2D deltaImpulse = invMass.getFactorIfProductIs(deltaVel);
	    // Apply the impulse at the point
	    pointMass.applyOffsetImpulse(offset, deltaImpulse);
	};
    }
}
