package se.liu.jonla400.project.physics.collision;

public interface CollisionListener<T>
{
    void collisionOccured(final CollisionData<T> collision);
}
