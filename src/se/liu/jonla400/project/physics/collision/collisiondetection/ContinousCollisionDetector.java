package se.liu.jonla400.project.physics.collision.collisiondetection;

import se.liu.jonla400.project.physics.collision.collisioninfo.UpcomingCollision;

import java.util.Optional;

/**
 * Represents a detector of the next collision within an upper time limit.
 * It is allowed to return a collision after the upper time limit, but it is not required.
 */
public interface ContinousCollisionDetector
{
    /**
     * Returns the time and information of the next collision within
     * the upper time limit, if any. It is allowed to return a collision
     * after the upper time limit, but it is not required.
     *
     * @param upperTimeLimit The upper time limit before which a collision is requested
     * @return The time and information of the upcoming collision, if any
     */
    Optional<UpcomingCollision> detectCollision(double upperTimeLimit);
}
