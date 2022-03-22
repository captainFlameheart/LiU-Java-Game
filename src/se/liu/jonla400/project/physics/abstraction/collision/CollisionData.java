package se.liu.jonla400.project.physics.abstraction.collision;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.abstraction.constraint.OffsetBodyPoint;
import se.liu.jonla400.project.physics.abstraction.constraint.OffsetBodyPointPair;
import se.liu.jonla400.project.physics.abstraction.main.Body;

public class CollisionData<T>
{
    private OffsetBodyPointPair contactPoints;

    private Vector2D normal;
    private double penetration;

    private double bounceCoefficient;
    private double frictionCoefficient;

    private T userData;

    public CollisionData(final OffsetBodyPointPair contactPoints, final Vector2D normal, final double penetration,
			 final double bounceCoefficient, final double frictionCoefficient, final T userData)
    {
	this.contactPoints = contactPoints;
	this.normal = normal.copy();
	this.penetration = penetration;
	this.bounceCoefficient = bounceCoefficient;
	this.frictionCoefficient = frictionCoefficient;
	this.userData = userData;
    }

    public static <T> CollisionData<T> create(
	    final Body bodyA, final Vector2D contactPointOffsetA, final Body bodyB, final Vector2D contactPointOffsetB,
	    final Vector2D normal, final double penetration, final double bounceCoefficient, final double frictionCoefficient,
	    final T userData)
    {
	final OffsetBodyPointPair contactPoints = new OffsetBodyPointPair(
		OffsetBodyPoint.copyOffset(bodyA, contactPointOffsetA),
		OffsetBodyPoint.copyOffset(bodyB, contactPointOffsetB)
	);
	return new CollisionData<>(contactPoints, normal.copy(), penetration, bounceCoefficient, frictionCoefficient, userData);
    }

    public OffsetBodyPointPair getContactPoints() {
	return contactPoints;
    }

    public Vector2D getNormal() {
	return normal.copy();
    }

    public double getPenetration() {
	return penetration;
    }

    public double getBounceCoefficient() {
	return bounceCoefficient;
    }

    public double getFrictionCoefficient() {
	return frictionCoefficient;
    }

    public T getUserData() {
	return userData;
    }
}
