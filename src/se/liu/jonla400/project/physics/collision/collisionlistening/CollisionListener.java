package se.liu.jonla400.project.physics.collision.collisionlistening;

import se.liu.jonla400.project.physics.collision.collisioninfo.Collision;

/**
 * Represents a listener to collisions that occur. A collision listener also
 * gets to know how big the time step is after the collision
 */
public interface CollisionListener
{
    /**
     * Takes part of a collision, after which a time step of the given size
     * will take place
     *
     * @param collision The collision that occurs
     * @param deltaTime The size of the time step after the collision
     */
    void onCollision(Collision collision, double deltaTime);
}
