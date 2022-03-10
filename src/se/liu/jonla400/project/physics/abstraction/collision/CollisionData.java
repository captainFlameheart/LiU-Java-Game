package se.liu.jonla400.project.physics.abstraction.collision;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.abstraction.main.Body;

public class CollisionData
{
    private Body[] bodies;
    private Vector2D[] contactPointOffsets;

    private Vector2D normal;
    private double penetration;

    private double bounceCoefficient;
    private double frictionCoefficient;

    public CollisionData(final Body bodyA, final Vector2D contactPointOffsetA, final Body bodyB, final Vector2D contactPointOffsetB,
			 final Vector2D normal, final double penetration, final double bounceCoefficient, final double frictionCoefficient)
    {
	bodies = new Body[]{bodyA, bodyB};
	contactPointOffsets = new Vector2D[]{contactPointOffsetA, contactPointOffsetB};
	this.normal = normal.copy();
	this.penetration = penetration;
	this.bounceCoefficient = bounceCoefficient;
	this.frictionCoefficient = frictionCoefficient;
    }

    public Body[] getBodies() {
	return bodies.clone();
    }

    public Vector2D[] getContactPointOffsets() {
	return contactPointOffsets.clone();
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

}
