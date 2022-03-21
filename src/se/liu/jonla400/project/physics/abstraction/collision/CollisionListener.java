package se.liu.jonla400.project.physics.abstraction.collision;

public interface CollisionListener<T>
{
    void collisionOccured(final CollisionData<T> collision);
}
