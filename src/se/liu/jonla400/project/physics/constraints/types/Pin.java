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
 * target position with a velocity.
 */
public class Pin implements VelocityConstrainer
{
    private PointMass pointMass;
    private Vector2D pinnedPoint;
    private Vector2D pointPinnedTo;
    private double posCorrectionFraction;

    /**
     * Creates a new Pin with a default position correction fraction
     *
     * @param pointMass The point mass whose velocity and angular velocity to constrain
     * @param pinnedPoint The local point of the point mass to pin to a global point
     * @param pointPinnedTo The global point to pin the local point to
     */
    public Pin(final PointMass pointMass, final Vector2D pinnedPoint, final Vector2D pointPinnedTo) {
	this.pointMass = pointMass;
	this.pinnedPoint = pinnedPoint;
	this.pointPinnedTo = pointPinnedTo;
	posCorrectionFraction = 0.5;
    }

    /**
     * Sets the position correction fraction, which represents how much the position of
     * the pinned point will be corrected during one time step. The position correction is
     * done through applying an impulse towards the target position.
     *
     * If the fraction is 0, no position correction will occur. If the
     * fraction is 0.5, the pinned point will get a velocity that *would* solve the
     * position error by 50% during the time step if the point retained the same velocity
     * throughout the time step. This however is rare since the point is usually orbiting
     * the point mass's center and thus changing its velocity constantly.
     *
     * The fraction should be between 0 and 1
     *
     * @param posCorrectionFraction The fraction of the position error to fix after each time step
     */
    public void setPosCorrectionFraction(final double posCorrectionFraction) {
	this.posCorrectionFraction = posCorrectionFraction;
    }

    /**
     * Initializes a new ActiveVelocityConstraint used to constrain the velocity of
     * the pinned point. The velocity constraint is only required to work for the
     * current position and angle of the point mass.
     *
     * @param deltaTime The size of the upcoming time step
     * @return The new ActiveVelocityConstraint
     */
    @Override public ActiveVelocityConstraint initActiveVelConstraint(final double deltaTime) {
	// Get the current offset of the pinned point from the point mass
	final Vector2D pinnedPointOffset = pointMass.convertLocalPointToOffset(pinnedPoint);
	// Get the current inverted mass of the pinned point
	final Matrix22 pinnedPointInvMass = pointMass.getInvMassAt(pinnedPointOffset);
	// Get the target velocity of the pinned point based on the position error
	final Vector2D pinnedPointGlobalPos = pointMass.convertOffsetToGlobalPoint(pinnedPointOffset);
	final Vector2D fullPosCorrection = pointPinnedTo.subtract(pinnedPointGlobalPos);
	final Vector2D pinnedPointTargetVel = fullPosCorrection.multiply(posCorrectionFraction / deltaTime);
	// Return an active velocity constraint that does the following when its solution is updated
	return () -> {
	    // Get the current velocity of the pinned point of the point mass
	    final Vector2D pinnedPointVel = pointMass.getVelAt(pinnedPointOffset);
	    // Get the desired change in velocity based on the target velocity
	    final Vector2D pinnedPointDeltaVel = pinnedPointTargetVel.subtract(pinnedPointVel);
	    // Get the impulse that must be applied to the point to get the desired
	    // change in velocity
	    final Vector2D deltaImpulse = pinnedPointInvMass.getFactorIfProductIs(pinnedPointDeltaVel);
	    // Apply the impulse at the point
	    pointMass.applyOffsetImpulse(pinnedPointOffset, deltaImpulse);
	};
    }
}
