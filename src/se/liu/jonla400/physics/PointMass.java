package se.liu.jonla400.physics;

import se.liu.jonla400.math.Vector2D;
import se.liu.jonla400.project.timestepping.TimeStepper;

/**
 * Represents a physical point in space that can move and rotate with time.
 * A point mass also has a mass and an angular mass (rotational inertia)
 * which in this case control how ineffective impulses are at changing
 * the velocity and the angular velocity respectively. A point mass does
 * not know about collisions. It also doesn't have any knowledgea about acceleration.
 * It only knows about position, velocity, mass and their angular counterparts.
 */
public class PointMass implements TimeStepper
{
    private Vector2D pos;
    private Vector2D vel;
    private double mass;

    private double angle;
    private double angularVel;
    private double angularMass;

    /**
     * Create a still point mass with a position and angle of zero and
     * a mass and angular mass of 1
     */
    public PointMass() {
	pos = Vector2D.createZeroVector();
	vel = Vector2D.createZeroVector();
	mass = 1;

	angle = 0;
	angularVel = 0;
	angularMass = 1;
    }

    /**
     * Get a copy of the position
     *
     * @return The copy of the position
     */
    public Vector2D getPos() {
	return pos.copy();
    }

    /**
     * Set the position to a copy of another position
     *
     * @param pos The position to copy
     */
    public void setPos(final Vector2D pos) {
	this.pos = pos.copy();
    }

    /**
     * Get a copy of the velocity
     *
     * @return The copy of the velocity
     */
    public Vector2D getVel() {
	return vel.copy();
    }

    /**
     * Set the velocity to a copy of another velocity
     *
     * @param vel The velocity to copy
     */
    public void setVel(final Vector2D vel) {
	this.vel = vel.copy();
    }

    /**
     * Get the mass
     *
     * @return The mass
     */
    public double getMass() {
	return mass;
    }

    /**
     * Set the mass
     *
     * @param mass The mass
     */
    public void setMass(final double mass) {
	this.mass = mass;
    }


    /**
     * Get the angle
     *
     * @return The angle
     */
    public double getAngle() {
	return angle;
    }

    /**
     * Set the angle
     *
     * @param angle The angle
     */
    public void setAngle(final double angle) {
	this.angle = angle;
    }


    /**
     * Get the angular velocity
     *
     * @return The angular velocity
     */
    public double getAngularVel() {
	return angularVel;
    }

    /**
     * Set the angular velocity
     *
     * @param angularVel The angular velocity
     */
    public void setAngularVel(final double angularVel) {
	this.angularVel = angularVel;
    }

    /**
     * Get the angular mass
     *
     * @return The angular mass
     */
    public double getAngularMass() {
	return angularMass;
    }

    /**
     * Set the angular mass
     *
     * @param angularMass The angular mass
     */
    public void setAngularMass(final double angularMass) {
	this.angularMass = angularMass;
    }

    /**
     * Converts a point in the local space of this point mass into an offset
     * from the point mass's position.
     *
     * The local space of a point mass has its origin at the point mass's position
     * and has the same angle as the point mass.
     *
     * @param localPoint The point in local space
     * @return The offset from the position of this point mass
     */
    public Vector2D convertLocalPointToOffset(final Vector2D localPoint) {
	return localPoint.rotate(angle);
    }

    /**
     * Converts an offset from this point mass's position into a point in global space
     *
     * @param offset The offset from the position of this point mass
     * @return The global point
     */
    public Vector2D convertOffsetToGlobalPoint(final Vector2D offset) {
	return pos.add(offset);
    }

    /**
     * Converts a point in the local space of this point mass into a point in global space.
     *
     * The local space of a point mass has its origin at the point mass's position
     * and has the same angle as the point mass.
     *
     * @param localPoint The point in local space
     * @return The point in global space
     */
    public Vector2D convertLocalPointToGlobalPoint(final Vector2D localPoint) {
	final Vector2D offset = convertLocalPointToOffset(localPoint);
	return convertOffsetToGlobalPoint(offset);
    }

    /**
     * Converts a point in global space into an offset from the position of this point mass
     *
     * @param globalPoint The point in global space
     * @return The offset from the position of this point mass
     */
    public Vector2D convertGlobalPointToOffset(final Vector2D globalPoint) {
	return globalPoint.subtract(pos);
    }

    /**
     * Converts an offset from the position of this point mass into a point in
     * this point mass's local space.
     *
     * The local space of a point mass has its origin at the point mass's position
     * and has the same angle as the point mass.
     *
     * @param offset The offset from the position of this point mass
     * @return The point in local space
     */
    public Vector2D convertOffsetToLocalPoint(final Vector2D offset) {
	return offset.rotate(-angle);
    }

    /**
     * Converts a point in global space into a point in the local space of this point mass.
     *
     * The local space of a point mass has its origin at the point mass's position
     * and has the same angle as the point mass.
     *
     * @param globalPoint The point in global space
     * @return The point in local space
     */
    public Vector2D convertGlobalPointToLocalPoint(final Vector2D globalPoint) {
	final Vector2D offset = convertGlobalPointToOffset(globalPoint);
	return convertOffsetToLocalPoint(offset);
    }

    /**
     * Changes the velocity of this point mass by applying a centered impulse.
     * The mass determines how ineffective the impulse is at changing the velocity.
     *
     * @param impulse The impulse to apply
     */
    public void applyImpulse(final Vector2D impulse) {
	final Vector2D deltaVel = impulse.divide(mass);
	vel.addLocally(deltaVel);
    }

    /**
     * Changes the angular velocity of this point mass by applying an angular impulse.
     * The angular mass determines how ineffective the angular impulse is at changing
     * the angular velocity.
     *
     * @param angularImpulse The angular impulse to apply
     */
    public void applyAngularImpulse(final double angularImpulse) {
	final double deltaAngularVel = angularImpulse / angularMass;
	angularVel += deltaAngularVel;
    }

    /**
     * Changes both the velocity and the angular velocity of this point mass by
     * applying an impulse that is offset from the position of this point mass.
     * Only the part of the impulse that is perpendicular to the offset is
     * transferred into an angular impulse. The mass and angular mass determine
     * how ineffective the impulse is at changing the velocity and angular velocity
     * respectively.
     *
     * @param offset The offset from the position of this point mass
     * @param impulse The impulse to apply
     */
    public void applyOffsetImpulse(final Vector2D offset, final Vector2D impulse) {
	// Change the velocity
	applyImpulse(impulse);

	// Change the angular velocity since the impulse is offset from the center
	// of this point mass
	final double angularImpulse = offset.cross(impulse);
	applyAngularImpulse(angularImpulse);
    }

    /**
     * Move this point mass for a certain amount of time according to its velocity
     *
     * @param deltaTime	The amount of time during which to move
     */
    @Override public void tick(final double deltaTime) {
	if (deltaTime < 0) {
	    throw new IllegalArgumentException("Negative delta time: " + deltaTime);
	}

	final Vector2D deltaPos = vel.multiply(deltaTime);
	pos.addLocally(deltaPos);
    }
}
