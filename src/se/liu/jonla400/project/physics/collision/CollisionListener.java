package se.liu.jonla400.project.physics.collision;

public interface CollisionListener<T>
{
    void collisionOccurred(final CollisionData<T> collision);
}
