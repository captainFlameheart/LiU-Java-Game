package se.liu.jonla400.project.physics.abstraction.main;

import se.liu.jonla400.project.main.BodyDefinition;
import se.liu.jonla400.project.math.Matrix22;
import se.liu.jonla400.project.math.Vector2D;

public class Body
{
    private Vector2D pos;
    private Vector2D vel;
    private double mass;

    private double angle;
    private double angularVel;
    private double angularMass;

    /**
     * Creates a point mass with default values. These default values are
     * left unspecified in order to minimize the risk of external code being
     * dependent on the default values since the values can change at a later time.
     * To control the values, use the setter-methods after creating this point mass.
     */
    public Body() {
	pos = Vector2D.createZeroVector();
	vel = Vector2D.createZeroVector();
	mass = 1;

	angle = 0;
	angularVel = 0;
	angularMass = 1;
    }

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

    public static Body createFromDefinition(final BodyDefinition definition) {
	return new Body(definition.getPos(), Vector2D.createZeroVector(), definition.getMass(),
			0, 0, definition.getAngularMass());
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
	this.pos = pos.copy();
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
	this.vel = vel.copy();
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
     * Sets the mass
     *
     * @param mass The mass
     */
    public void setMass(final double mass) {
	if (mass <= 0) {
	    throw new IllegalArgumentException("Non-positive mass: " + mass);
	}
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
	if (angularMass <= 0) {
	    throw new IllegalArgumentException("Non-positive angular mass: " + angularMass);
	}
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
    public Vector2D convertLocalVectorToGlobalVector(final Vector2D localPoint) {
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
	final Vector2D offset = convertLocalVectorToGlobalVector(localPoint);
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
    public Matrix22 getInvMassAt(final Vector2D offset) {
	// See derivation in report

	final double invMass = 1 / mass;
	final double invAngularMass = 1 / angularMass;

	final double offsetX = offset.getX();
	final double offsetY = offset.getY();

	final double commonValue = -offsetX * offsetY * invAngularMass;
	return new Matrix22(new double[][]{
		{invMass + offsetY * offsetY * invAngularMass, commonValue},
		{commonValue, invMass + offsetX * offsetX * invAngularMass}
	});
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
