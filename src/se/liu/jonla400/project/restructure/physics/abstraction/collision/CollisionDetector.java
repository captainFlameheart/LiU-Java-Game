package se.liu.jonla400.project.restructure.physics.abstraction.collision;

import java.util.Collection;

public interface CollisionDetector
{
    Collection<CollisionData> detectCollisions();
}
