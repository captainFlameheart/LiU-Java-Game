package se.liu.jonla400.project.physics.collision.collisionlistening;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.PointMass;
import se.liu.jonla400.project.physics.collision.collisioninfo.Collision;
import se.liu.jonla400.project.physics.constraints.types.CollisionVelocityConstrainer;

/**
 * Represents a solver of collisions by constraining the positions and velocities
 * of the point masses involved. When constraining the positions, this solver
 * enforces a given separation (which might remove the risk of a collision detection
 * algorithm repeatedly detecting the same collision)
 */
public class CollisionSolver implements CollisionListener
{
    private double enforcedSeparation;

    /**
     * Creates a new collision solver that enforces the given separation.
     *
     * @param enforcedSeparation The enforced separation
     */
    public CollisionSolver(final double enforcedSeparation) {
        this.enforcedSeparation = enforcedSeparation;
    }

    /**
     * Solves the collision by constraining the velocities and positions of the bodies
     * involved
     *
     * @param collision The collision that occurs
     * @param deltaTime The size of the time step after the collision
     */
    @Override public void onCollision(final Collision collision, final double deltaTime) {
        constrainVel(collision, deltaTime);
        constrainPos(collision);
    }

    private void constrainVel(final Collision collision, final double deltaTime) {
        final CollisionVelocityConstrainer velConstrainer = new CollisionVelocityConstrainer(collision);
        velConstrainer.initActiveVelConstraint(deltaTime).updateSolution();
    }

    private void constrainPos(final Collision collision) {
        final double deltaSeperation = enforcedSeparation - collision.getSeparation();
        final Vector2D deltaPos = collision.getNormal().multiply(deltaSeperation);

        final PointMass pointMass = collision.getPointMass();
        pointMass.setPos(pointMass.getPos().add(deltaPos));
    }
}
