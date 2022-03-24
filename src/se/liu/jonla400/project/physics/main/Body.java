package se.liu.jonla400.project.physics.main;

import se.liu.jonla400.project.math.Matrix22;
import se.liu.jonla400.project.math.Vector2D;

/**
 * Represents a physical entity with a position, velocity, mass and their angular equivalents
 * (angle, angular velocity and angular mass). Impulses can be applied to the body and can also
 * be offset from the body's position which causes a change in both the velocity and the angular
 * velocity. A body can perform a time step of a given size, which moves the position along the
 * velocity (the same goes for the angle).
 */
public class Body
{
    private Vector2D pos;
    private Vector2D vel;
    private double mass;

    private double angle;
    private double angularVel;
    private double angularMass;

    private Body(final Vector2D pos, final Vector2D vel, final double mass, final double angle, final double angularVel,
		final double angularMass)
    {
	this.pos = pos;
	this.vel = vel;
	this.mass = mass;
	this.angle = angle;
	this.angularVel = angularVel;
	this.angularMass = angularMass;
    }

    /**
     * Creates a still body at the given position with the given mass and angular mass. The
     * angle is set to 0. No reference is kept to the position vector.
     *
     * @param pos The position
     * @param mass The mass
     * @param angularMass The angular mass
     * @return The created body
     */
    public static Body create(final Vector2D pos, final double mass, final double angularMass) {
	return new Body(pos.copy(), Vector2D.createZero(), mass, 0, 0, angularMass);
    }

    /**
     * Gets a copy of the position
     *
     * @return The copy of the position
     */
    public Vector2D getPos() {
	return pos.copy();
    }

    /**
     * Sets the position to a copy of another position
     *
     * @param pos The position to copy
     */
    public void setPos(final Vector2D pos) {
	this.pos.set(pos);
    }

    /**
     * Gets a copy of the velocity
     *
     * @return The copy of the velocity
     */
    public Vector2D getVel() {
	return vel.copy();
    }

    /**
     * Sets the velocity to a copy of another velocity
     *
     * @param vel The velocity to copy
     */
    public void setVel(final Vector2D vel) {
	this.vel.set(vel.copy());
    }

    /**
     * Gets the mass
     *
     * @return The mass
     */
    public double getMass() {
	return mass;
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
	// This method is likely used in the future
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
	// This method is likely used in the future
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
     * Converts a vector in the local space of this point mass into a vector in global space.
     *
     * The local space of a point mass has its origin at the point mass's position
     * and has the same angle as the point mass.
     *
     * @param localVector The vector in local space
     * @return The vector in global space
     */
    public Vector2D convertLocalToGlobalVector(final Vector2D localVector) {
	return localVector.rotate(angle);
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
    public Vector2D convertLocalToGlobalPoint(final Vector2D localPoint) {
	final Vector2D offset = convertLocalToGlobalVector(localPoint);
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
     * Converts a vector in global space into a vector in this point mass's local space.
     *
     * The local space of a point mass has its origin at the point mass's position
     * and has the same angle as the point mass.
     *
     * @param globalVector The vector in global space
     * @return The vector in local space
     */
    public Vector2D convertGlobalToLocalVector(final Vector2D globalVector) {
	return globalVector.rotate(-angle);
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
    public Vector2D convertGlobalToLocalPoint(final Vector2D globalPoint) {
	final Vector2D offset = convertGlobalPointToOffset(globalPoint);
	return convertGlobalToLocalVector(offset);
    }

    /**
     * Gets the orbiting velocity at the given offset from the position of this point mass.
     * The orbiting velocity depends only on the angular velocity and the given offset, but
     * not on the "translational" velocity.
     *
     * @param offset The offset from the position of this point mass
     * @return The orbiting velocity at that offset
     */
    public Vector2D getCircularVelAt(final Vector2D offset) {
	// The magnitude of the velocity at the offset is ||offset|| * angularVel
	// The direction of the velocity at the offset is perpendicular to the offset

	final Vector2D perpendicularOffset = offset.rotate90Degrees(Vector2D.RotationDirection.X_TO_Y);
	return perpendicularOffset.multiply(angularVel);
    }

    /**
     * Gets the velocity at the given offset from the position of this point mass.
     * This velocity depends on the "translational" velocity, the angular velocity and
     * the given offset.
     *
     * @param offset The offset from the position of this point mass
     * @return The velocity at that offset
     */
    public Vector2D getVelAt(final Vector2D offset) {
	final Vector2D circularVel = getCircularVelAt(offset);
	return vel.add(circularVel);
    }

    /**
     * Gets a 2 by 2 matrix representing the inverted mass at the given offset from
     * the position of this point mass.
     *
     * Mathematical explanation:
     *
     * Let J be an arbitrary column vector of length 2 representing an impulse applied at the given
     * offset, where J[0] is the x-coordinate and J[1] is the y-coordinate of the impulse
     *
     * Let Δv be a column vector representing the change in velocity at the given offset after the
     * impulse J has been applied, where Δv[0] is the x-coordinate and Δv[1] is the y-coordinate of
     * the change in velocity.
     *
     * This method returns a 2-by-2 matrix M such that M * J = Δv where * denotes matrix multiplication
     *
     * @param offset The offset from the position of this point mass
     * @return The inverse of the mass at the offset
     */
    public Matrix22 getInvertedMassAt(final Vector2D offset) {
	// See derivation in report
	final double invertedMass = 1 / mass;
	final double invertedAngularMass = 1 / angularMass;

	final double offsetX = offset.getX();
	final double offsetY = offset.getY();

	final double commonValue = -offsetX * offsetY * invertedAngularMass;
	return Matrix22.create(
		invertedMass + offsetY * offsetY * invertedAngularMass, commonValue,
		commonValue, invertedMass + offsetX * offsetX * invertedAngularMass
	);
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
     * @param impulse The impulse to apply at the offset
     */
    public void applyOffsetImpulse(final Vector2D offset, final Vector2D impulse) {
	// Change the velocity
	applyImpulse(impulse);

	// Change the angular velocity since the impulse is offset from the center
	// of this point mass
	final double angularImpulse = offset.cross(impulse); // See the definition of torque https://en.wikipedia.org/wiki/Torque
	applyAngularImpulse(angularImpulse);
    }

    /**
     * Move this point mass for a certain amount of time according to its velocity
     *
     * @param deltaTime	The amount of time during which to move
     */
    public void tick(final double deltaTime) {
	if (deltaTime < 0) {
	    throw new IllegalArgumentException("Negative delta time: " + deltaTime);
	}

	final Vector2D deltaPos = vel.multiply(deltaTime);
	pos.addLocally(deltaPos);

	final double deltaAngle = angularVel * deltaTime;
	angle += deltaAngle;
    }

    @Override public String toString() {
	return "Body{" + "pos=" + pos + ", vel=" + vel + ", mass=" + mass + ", angle=" + angle + ", angularVel=" + angularVel +
	       ", angularMass=" + angularMass + '}';
    }
}
