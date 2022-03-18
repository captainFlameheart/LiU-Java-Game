package se.liu.jonla400.restructure.physics.abstraction.collision;

public interface CollisionListener<T>
{
    void collisionOccured(final CollisionData<T> collision);
}
