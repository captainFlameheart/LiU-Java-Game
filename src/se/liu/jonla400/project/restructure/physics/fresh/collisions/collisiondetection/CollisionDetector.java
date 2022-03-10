package se.liu.jonla400.project.restructure.physics.fresh.collisions.collisiondetection;

import se.liu.jonla400.project.restructure.physics.fresh.collisions.CollisionData;

import java.util.Collection;

public interface CollisionDetector
{
    Collection<CollisionData> detectCollisions();
}
