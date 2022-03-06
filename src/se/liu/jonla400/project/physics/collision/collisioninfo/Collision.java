package se.liu.jonla400.project.physics.collision.collisioninfo;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.PointMass;

public class Collision
{
    private PointMass pointMass;
    private Vector2D contactPointOffset;
    private Vector2D normal;
    private Vector2D tangent;
    private double separation;

    private double bounceCoefficient;
    private double frictionCoefficient;

    public Collision(final PointMass pointMass, final Vector2D contactPointOffset, final Vector2D normal,
                     final double separation, final double bounceCoefficient, final double frictionCoefficient)
    {
        this.pointMass = pointMass;
        this.contactPointOffset = contactPointOffset.copy();
        this.normal = normal.copy();
        this.tangent = normal.rotate90Degrees(Vector2D.RotationDirection.Y_TO_X); // Precomputed
        this.separation = separation;
        this.bounceCoefficient = bounceCoefficient;
        this.frictionCoefficient = frictionCoefficient;
    }

    public PointMass getPointMass() {
        return pointMass;
    }

    public Vector2D getContactPointOffset() {
        return contactPointOffset.copy();
    }

    public Vector2D getNormal() {
        return normal.copy();
    }

    public Vector2D getTangent() {
        return tangent;
    }

    public double getSeparation() {
        return separation;
    }

    public double getBounceCoefficient() {
        return bounceCoefficient;
    }

    public double getFrictionCoefficient() {
        return frictionCoefficient;
    }
}
