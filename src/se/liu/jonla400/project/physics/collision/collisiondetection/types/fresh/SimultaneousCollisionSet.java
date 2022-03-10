package se.liu.jonla400.project.physics.collision.collisiondetection.types.fresh;


import se.liu.jonla400.project.physics.collision.collisioninfo.Collision;

import java.util.List;

public class SimultaneousCollisionSet
{
    private List<Collision> collisions;
    private double timeOfImpact;

    public SimultaneousCollisionSet(final List<Collision> collisions, final double timeOfImpact) {
	this.collisions = collisions;
	this.timeOfImpact = timeOfImpact;
    }
}
