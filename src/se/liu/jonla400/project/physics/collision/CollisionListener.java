package se.liu.jonla400.project.physics.collision;

/**
 * Represents a listener to collisions with a certain type of user data. A collision listener
 * will, after being registered to a {@link CollisionHandler}, receive {@link CollisionData} for each
 * detected collision.
 *
 * @param <T> The type of user data associated with each collision
 */
public interface CollisionListener<T>
{
    /**
     * Reacts to a detected collision
     *
     * @param collision The collision that was detected
     */
    void collisionOccurred(final CollisionData<T> collision);
}
