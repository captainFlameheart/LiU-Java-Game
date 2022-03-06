package se.liu.jonla400.project.physics.collision.collisioninfo;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.PointMass;

public class UpcomingCollision extends Collision
{
    private double timeOfImpact;

    public UpcomingCollision(final PointMass pointMass, final Vector2D contactPointOffset, final Vector2D collisionNormal,
			     final double separation, final double bounceCoefficient, final double frictionCoefficient,
			     final double timeOfImpact)
    {
	super(pointMass, contactPointOffset, collisionNormal, separation, bounceCoefficient, frictionCoefficient);
	this.timeOfImpact = timeOfImpact;
    }

    public double getTimeOfImpact() {
	return timeOfImpact;
    }
}
