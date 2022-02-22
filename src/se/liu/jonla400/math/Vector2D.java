package se.liu.jonla400.math;

import java.util.Vector;

/**
 * Represents a 2D vector
 */
public class Vector2D
{
    private double x;
    private double y;

    private Vector2D(final double x, final double y) {
	this.x = x;
	this.y = y;
    }

    /**
     * Creates a zero vector
     *
     * @return The created vector
     */
    public static Vector2D zero() {
	return new Vector2D(0, 0);
    }

    /**
     * Creates a new vector from cartesian coordinates
     *
     * @param x The x component of the vector
     * @param y The y component of the vector
     * @return The created vector
     */
    public static Vector2D cartesian(final double x, final double y) {
	return new Vector2D(x, y);
    }

    /**
     * Creates a new vector from polar coordinates
     *
     * @param angle The angle of the vector
     * @param magnitude The magnitude of the vector
     * @return The created vector
     */
    public static Vector2D polar(final double angle, final double magnitude) {
	Vector2D result = unitFromAngle(angle);
	result.multiplyLocally(magnitude);
	return result;
    }

    /**
     * Creates a new unit vector (magnitude == 1) from an angle
     *
     * @param angle The angle of the vector
     * @return The created vector
     */
    public static Vector2D unitFromAngle(final double angle) {
	return new Vector2D(Math.cos(angle), Math.sin(angle));
    }

    /**
     * Copies this vector
     *
     * @return A copy of this vector
     */
    public Vector2D copy() {
	return new Vector2D(x, y);
    }

    /**
     * Get the x component of this vector
     *
     * @return The x component
     */
    public double getX() {
	return x;
    }

    /**
     * Get the y component of this vector
     *
     * @return The y component
     */
    public double getY() {
	return y;
    }

    /**
     * Get the angle of this vector
     *
     * @return The angle
     */
    public double getAngle() {
	return Math.atan2(y, x);
    }

    /**
     * Get the square of this vector's magnitude
     *
     * @return The square of the magnitude
     */
    public double getMagnitudeSquared() {
	// Is faster than getting the real magnitude since this
	// method does not require a square root operation
	return x * x + y * y;
    }

    /**
     * Get the magnitude of this vector
     *
     * @return The magnitude
     */
    public double getMagnitude() {
	return Math.sqrt(getMagnitudeSquared());
    }

    /**
     * Set this vector to a copy of another vector
     *
     * @param other The vector to copy from
     */
    public void set(final Vector2D other) {
	this.x = other.x;
	this.y = other.y;
    }

    /**
     * Set the cartesian coordinates of this vector
     *
     * @param x The x component
     * @param y The y component
     */
    public void setCartesian(final double x, final double y) {
	set(cartesian(x, y));
    }

    /**
     * Set the polar coordinates of this vector
     *
     * @param angle The angle
     * @param magnitude The magnitude
     */
    public void setPolar(final double angle, final double magnitude) {
	set(polar(angle, magnitude));
    }

    /**
     * Set the x component of this vector
     *
     * @param x The x component
     */
    public void setX(final double x) {
	this.x = x;
    }

    /**
     * Set the y component of this vector
     *
     * @param y The y component
     */
    public void setY(final double y) {
	this.y = y;
    }

    /**
     * Set the angle of this vector
     *
     * @param angle The angle
     */
    public void setAngle(final double angle) {
	final double deltaAngle = angle - getAngle();
	rotateLocally(deltaAngle);
    }

    /**
     * Set the magnitude of this vector
     *
     * @param angle The angle
     */
    public void setMagnitude(final double magnitude) {
	final double magnitudeRatio = magnitude / getMagnitude();
	multiplyLocally(magnitudeRatio);
    }

    /**
     * Returns the result of adding this vector with another vector
     *
     * @param other The other vector to add
     * @return The result of the addition
     */
    public Vector2D add(final Vector2D other) {
	return new Vector2D(x + other.x, y + other.y);
    }

    /**
     * Changes this vector by adding another vector
     *
     * @param other The other vector to add
     */
    public void addLocally(final Vector2D other) {
	set(add(other));
    }

    /**
     * Returns the result of subtracting this vector with another vector
     *
     * @param other The vector to subtract with
     * @return The result of the subtraction
     */
    public Vector2D subtract(final Vector2D other) {
	return new Vector2D(x - other.x, y - other.y);
    }

    /**
     * Changes this vector by subtracting another vector
     *
     * @param other The vector to subtract with
     */
    public void subtractLocally(final Vector2D other) {
	set(subtract(other));
    }

    /**
     * Returns the result of multiplying this vector with a scalar
     *
     * @param scalar The scalar to multiply with
     * @return The result of the multiplication
     */
    public Vector2D multiply(final double scalar) {
	return new Vector2D(x * scalar, y * scalar);
    }

    /**
     * Changes this vector by multiplying with a scalar
     *
     * @param scalar The scalar to multiply with
     */
    public void multiplyLocally(final double scalar) {
	set(multiply(scalar));
    }

    /**
     * Returns the result of multiplying this vector with the inverse of a scalar
     *
     * @param scalar The inverse of the scalar to multiply with
     * @return The result of the inverse multiplication (division)
     */
    public Vector2D divide(final double scalar) {
	return multiply(1 / scalar);
    }

    /**
     * Changes this vector by multiplying with the inverse of a scalar
     *
     * @param scalar The inverse of the scalar to multiply with
     */
    public void divideLocally(final double scalar) {
	set(divide(scalar));
    }

    /**
     * Returns the result of normalizing this vector into a unit vector (of length 1)
     *
     * @return The normalized vector
     */
    public Vector2D normalize() {
	return divide(getMagnitude());
    }

    /**
     * Changes this vector by normalizing it into a unit vector (of length 1)
     */
    public void normalizeLocally() {
	set(normalize());
    }

    /**
     * Returns the result of negating this vector
     *
     * @return The result of the negation
     */
    public Vector2D negate() {
	return multiply(-1);
    }

    /**
     * Changes this vector by negating it
     */
    public void negateLocally() {
	set(negate());
    }

    /**
     * Returns the result of rotating this vector 90 degrees in one of two directions.
     * Whether the rotation direction represents a clockwise or a counter-clockwise
     * direction is dependent on where the x- and y-axes are interpreted to point towards.
     *
     * @param direction The direction to rotate in
     * @return The result of the rotation
     */
    public Vector2D rotate90Degrees(final RotationDirection direction) {
	final double newY = direction.sign * x;
	final double newX = -direction.sign * y;
	return new Vector2D(newX, newY);
    }

    /**
     * Changes this vector by rotating it 90 degrees in one of two directions.
     * Whether the rotation direction represents a clockwise or a counter-clockwise
     * direction is dependent on where the x- and y-axes are interpreted to point towards.
     *
     * @param direction The direction to rotate in
     */
    public void rotate90DegreesLocally(final RotationDirection direction) {
	set(rotate90Degrees(direction));
    }

    /**
     * Returns the result of rotating this vector by an arbitrary angle
     *
     * @param deltaAngle The angle to rotate with
     * @return The result of the rotation
     */
    public Vector2D rotate(final double deltaAngle) {
	// See derivation at https://matthew-brett.github.io/teaching/rotation_2d.html
	final double cosDeltaAngle = Math.cos(deltaAngle);
	final double sinDeltaAngle = Math.sin(deltaAngle);

	final double newX = x * cosDeltaAngle - y * sinDeltaAngle;
	final double newY = x * sinDeltaAngle + y * cosDeltaAngle;
	return new Vector2D(newX, newY);
    }

    /**
     * Changes this vector by rotating it by an arbitrary angle
     *
     * @param deltaAngle The angle to rotate with
     */
    public void rotateLocally(final double deltaAngle) {
	set(rotate(deltaAngle));
    }

    /**
     * Returns the dot product between this vector and another vector.
     * Given two vectors v = (x1, y1) and w = (x2, y2) the dot product is
     * defined as (x1 * x2) + (y1 * y2). Another equivalent definition is
     * ||a|| * ||b|| * cos(angle between a and b).
     *
     * @param other The other vector to perform the dot product with
     * @return The dot product
     * @see <a href="https://en.wikipedia.org/wiki/Dot_product">wikipedia</a>
     */
    public double dot(final Vector2D other) {
	return (x * other.x) + (y * other.y);
    }

    public static enum RotationDirection
    {
	/**
	 * The x-axis becomes the y-axis
	 */
	X_TO_Y(1),

	/**
	 * The y-axis becomes the x-axis
	 */
	Y_TO_X(-1);

	private final int sign;

	private RotationDirection(final int sign) {
	    this.sign = sign;
	}
    }

}
