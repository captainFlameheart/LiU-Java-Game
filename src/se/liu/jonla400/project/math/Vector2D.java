package se.liu.jonla400.project.math;

import java.util.Objects;

/**
 * Represents a 2D vector that supports typical vector operations.
 * Some of these operations act on the vector object itself, changing
 * its underlying data. Other operations do not change the vector
 * that is operated upon. These operations instead returns a new vector
 * as the result.
 */
public class Vector2D implements ClosestPointFinder
{
    private double x;
    private double y;

    private Vector2D() {
	// Used by gson
	x = 0;
	y = 0;
    }

    private Vector2D(double x, double y) {
	this.x = x;
	this.y = y;
    }

    /**
     * Creates a new vector from cartesian coordinates
     *
     * @param x The x component of the vector
     * @param y The y component of the vector
     * @return The created vector
     */
    public static Vector2D createCartesian(final double x, final double y) {
	return new Vector2D(x, y);
    }

    /**
     * Creates a zero vector
     *
     * @return The created vector
     */
    public static Vector2D createZero() {
	return createCartesian(0, 0);
    }

    /**
     * Creates a new unit vector (magnitude == 1) from an angle
     *
     * @param angle The angle of the vector
     * @return The created vector
     */
    public static Vector2D createUnitVector(final double angle) {
	return createCartesian(Math.cos(angle), Math.sin(angle));
    }

    /**
     * Copies this vector
     *
     * @return A copy of this vector
     */
    public Vector2D copy() {
	return createCartesian(x, y);
    }

    /**
     * Returns whether the components of this vector are exactly zero
     *
     * @return Whether this is a zero vector
     */
    public boolean isZero() {
	return x == 0 && y == 0;
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
     * Returns the result of setting the magnitude of this vector.
     * The result is undefined if the vector is a zero vector
     *
     * @param magnitude The magnitude to set
     * @return The result of setting the magnitude
     */
    public Vector2D setMagnitude(final double magnitude) {
	final double magnitudeRatio = magnitude / getMagnitude();
	return multiply(magnitudeRatio);
    }

    /**
     * Returns the result of adding this vector with another vector
     *
     * @param other The other vector to add
     * @return The result of the addition
     */
    public Vector2D add(final Vector2D other) {
	return createCartesian(x + other.x, y + other.y);
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
	return createCartesian(x - other.x, y - other.y);
    }

    /**
     * Returns the result of multiplying this vector with a scalar
     *
     * @param scalar The scalar to multiply with
     * @return The result of the multiplication
     */
    public Vector2D multiply(final double scalar) {
	return createCartesian(x * scalar, y * scalar);
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
     * Returns a vector half as big as this one
     *
     * @return The result of halving this vector
     */
    public Vector2D getHalf() {
	return divide(2);
    }

    /**
     * Returns the result of normalizing this vector into a unit vector (of length 1).
     * The result is undefined if this vector is a zero vector
     *
     * @return The normalized vector
     */
    public Vector2D normalize() {
	return divide(getMagnitude());
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
     * Returns the result of limiting the magnitude of this vector
     *
     * @param maxMagnitude The maximum magnitude
     */
    public Vector2D limitMagnitude(final double maxMagnitude) {
	final double magnitudeSquared = getMagnitudeSquared();	// Faster than getting the actual magnitude
	final double maxMagnitudeSquared = maxMagnitude * maxMagnitude;
	if (magnitudeSquared > maxMagnitudeSquared) {
	    final double magnitude = Math.sqrt(magnitudeSquared);
	    final double magnitudeRatio = maxMagnitude / magnitude;
	    return multiply(magnitudeRatio);
	}
	return copy();
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
	return createCartesian(newX, newY);
    }

    /**
     * Returns the result of rotating this vector 90 degrees in an arbitrary direction.
     * This method is useful when the direction is unimportant
     *
     * @return The result of the rotation
     */
    public Vector2D rotate90Degrees() {
	return rotate90Degrees(RotationDirection.X_TO_Y);
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
	return createCartesian(newX, newY);
    }

    /**
     * Returns the dot product between this vector and another vector.
     * Given two vectors v = (x1, y1) and w = (x2, y2) the dot product is
     * defined as (x1 * x2) + (y1 * y2). Another equivalent definition is
     * ||v|| * ||w|| * cos(angle between v and w).
     *
     * @param other The other vector to perform the dot product with
     * @return The dot product
     * @see <a href="https://en.wikipedia.org/wiki/Dot_product">wikipedia</a>
     */
    public double dot(final Vector2D other) {
	return (x * other.x) + (y * other.y);
    }

    /**
     * Assumes that this vector and the input vector are defining 3D
     * vectors with z-components implicitly set to 0. The cross product
     * of these 3D vectors will be normal to the plane containing them,
     * which implies that both the x- and y-coordinate of the cross product
     * are 0. This method returns the z-coordinate of the cross product.
     *
     * In this 2D interpretation of the cross product, it is defined as
     * (x1 * y2) - (y1 * x2) where (x1, y1) and (x2, y2) are two vectors
     *
     * @param other The other vector to perform the cross product with
     * @return The z-component of the implicit 3D cross product
     * @see <a href="https://stackoverflow.com/questions/243945/calculating-a-2d-vectors-cross-product">wikipedia</a>
     */
    public double cross(final Vector2D other) {
	return (x * other.y) - (y * other.x);
    }

    /**
     * Returns the square of the euclidean distance between this point and the other point
     *
     * @param other The other point
     * @return The distance squared
     */
    public double getDistanceSquaredTo(final Vector2D other) {
	return other.subtract(this).getMagnitudeSquared();
    }

    @Override public boolean equals(final Object o) {
	if (this == o) {
	    return true;
	}
	if (o == null || getClass() != o.getClass()) {
	    return false;
	}
	final Vector2D vector2D = (Vector2D) o;
	return Double.compare(vector2D.x, x) == 0 && Double.compare(vector2D.y, y) == 0;
    }

    @Override public int hashCode() {
	return Objects.hash(x, y);
    }

    @Override public String toString() {
	return "Vector2D{" + "x=" + x + ", y=" + y + ", angle=" + getAngle() + ", magnitude=" + getMagnitude() + '}';
    }

    @Override public Vector2D findClosestPointTo(final Vector2D point) {
	return this;
    }

    /**
     * Represents a rotation direction for 90 degree rotations
     */
    public static enum RotationDirection
    {
	/**
	 * The x-axis becomes the y-axis
	 */
	X_TO_Y(1),

	/**
	 * The y-axis becomes the x-axis
	 * (Code inspection comment: this field is likely used in the future)
	 */
	Y_TO_X(-1);

	private final int sign;

	private RotationDirection(final int sign) {
	    this.sign = sign;
	}
    }

}
