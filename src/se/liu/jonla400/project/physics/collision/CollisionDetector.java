package se.liu.jonla400.project.physics.collision;

import java.util.Collection;

/**
 * Represents a detector of collisions and their {@link CollisionData}.
 * A collision detector does not handle the collisions it detects.
 */
public interface CollisionDetector<T>
{
    /**
     * Returns a collection of the current collisions and their {@link CollisionData}.
     *
     * @return The data of each collision
     */
    Collection<CollisionData<T>> detectCollisions();
}
