package se.liu.jonla400.project.physics.collision;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.constraint.OffsetBodyPoint;
import se.liu.jonla400.project.physics.constraint.OffsetBodyPointPair;
import se.liu.jonla400.project.physics.main.Body;

/**
 * Represents the data associated with a collision between two bodies. The shapes used to detect
 * the collision have been abstracted away. The data includes the bodies in contact,
 * the points on those bodies that are actually in contact, the collision normal (direction of
 * the collision), penetration (how much overlap there is along the normal) and {@link Material}.
 * The collision data also includes custom user data that are unique for each type of collision.
 *
 * @param <T> The type of user data
 */
public class CollisionData<T>
{
    private OffsetBodyPointPair contactPoints;

    private Vector2D normal;
    private double penetration;

    private Material material;

    private T userData;

    private CollisionData(final OffsetBodyPointPair contactPoints, final Vector2D normal, final double penetration,
			 final Material material, final T userData)
    {
	this.contactPoints = contactPoints;
	this.normal = normal;
	this.penetration = penetration;
	this.material = material;
	this.userData = userData;
    }

    /**
     * Creates a CollisionData object.
     *
     * @param bodyA One of the colliding bodies
     * @param contactPointOffsetA bodyA's contact point described as an offset from the body's position
     * @param bodyB The other colliding body
     * @param contactPointOffsetB bodyB's contact point described as an offset from the body's position
     * @param normal The direction A's contact point should bounce in relative to B's contact point, should be a unit vector
     * @param penetration The penetration of the contact points along the normal
     * @param material The bounce and friction coefficients
     * @param userData The user data
     * @param <T> The type of user data
     * @return The created collision data
     */
    public static <T> CollisionData<T> create(
	    final Body bodyA, final Vector2D contactPointOffsetA, final Body bodyB, final Vector2D contactPointOffsetB,
	    final Vector2D normal, final double penetration, final Material material, final T userData)
    {
	final OffsetBodyPointPair contactPoints = new OffsetBodyPointPair(
		OffsetBodyPoint.copyOffset(bodyA, contactPointOffsetA),
		OffsetBodyPoint.copyOffset(bodyB, contactPointOffsetB)
	);
	return new CollisionData<>(contactPoints, normal.copy(), penetration, material, userData);
    }

    public OffsetBodyPointPair getContactPoints() {
	return contactPoints;
    }

    /**
     * @return A read-only view of the collision normal, indicating in which direction the first contact point should bounce in
     */
    public Vector2D getNormal() {
	return normal.copy();
    }

    /**
     * @return The overlap along the normal
     */
    public double getPenetration() {
	return penetration;
    }

    /**
     * @return A fraction of how much of the velocity along the normal to retain (but negated)
     */
    public double getBounceCoefficient() {
	return material.getBounceCoefficient();
    }

    /**
     * @return How strong the friction impulse should be relative to the normal impulse
     */
    public double getFrictionCoefficient() {
	return material.getFrictionCoefficient();
    }

    public T getUserData() {
	return userData;
    }
}
