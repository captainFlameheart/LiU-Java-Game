package se.liu.jonla400.project.main.game;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.constraint.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.constraint.ActiveVelocityConstraintList;
import se.liu.jonla400.project.physics.constraint.VelocityConstrainer;
import se.liu.jonla400.project.physics.main.Body;
import se.liu.jonla400.project.physics.constraint.implementation.AngularVelocitySeeker;
import se.liu.jonla400.project.physics.constraint.implementation.VelocitySeeker;

import java.awt.event.KeyEvent;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Controlls the velocity and angular velocity of a {@link Body} based on keys that are
 * being pressed. To avoid quick velocity changes that are unrealistic from a physical stand-point,
 * a maximum force and torque are put in place.
 */
public class VelocityController implements VelocityConstrainer
{
    private Map<Integer, MovementDirection> keyToMovementDir;
    private double speed;
    private Set<MovementDirection> activeMovementDirections;
    private VelocitySeeker velSeeker;

    private Map<Integer, RotationDirection> keyToRotationDir;
    private double angularSpeed;
    private Set<RotationDirection> activeRotationDirections;
    private AngularVelocitySeeker angularVelSeeker;

    public VelocityController(final Map<Integer, MovementDirection> keyToMovementDir, final double speed,
                              final Set<MovementDirection> activeMovementDirections, final VelocitySeeker velSeeker,
                              final Map<Integer, RotationDirection> keyToRotationDir, final double angularSpeed,
                              final Set<RotationDirection> activeRotationDirections, final AngularVelocitySeeker angularVelSeeker)
    {
        this.keyToMovementDir = keyToMovementDir;
        this.speed = speed;
        this.activeMovementDirections = activeMovementDirections;
        this.velSeeker = velSeeker;
        this.keyToRotationDir = keyToRotationDir;
        this.angularSpeed = angularSpeed;
        this.activeRotationDirections = activeRotationDirections;
        this.angularVelSeeker = angularVelSeeker;
    }

    /**
     * Creates a VelocityController with default controlls and speeds. The controlls are
     * as follows:
     * A -> move left
     * D -> move right
     * W -> move up
     * S -> move down
     * Q -> rotate left
     * E -> rotate right
     *
     * @param body The body to steer
     * @return The create VelocityController
     */
    public static VelocityController createWithDefaultConfigFor(final Body body) {
        final Map<Integer, MovementDirection> keyToMovementDir = Map.of(
                KeyEvent.VK_A, MovementDirection.LEFT,
                KeyEvent.VK_D, MovementDirection.RIGHT,
                KeyEvent.VK_W, MovementDirection.UP,
                KeyEvent.VK_S, MovementDirection.DOWN
        );
        final double speed = 10;
        final double maxAcceleration = 20;

        final Map<Integer, RotationDirection> keyToRotationDir = Map.of(
                KeyEvent.VK_Q, RotationDirection.LEFT,
                KeyEvent.VK_E, RotationDirection.RIGHT
        );
        final double angularSpeed = 2;
        final double maxAngularAcceleration = 10;

        final Set<MovementDirection> activeMovementDirections = EnumSet.noneOf(MovementDirection.class);
        final double maxForce = body.getMass() * maxAcceleration;
        final VelocitySeeker velSeeker = VelocitySeeker.createStartingAsFriction(body, maxForce);

        final Set<RotationDirection> activeRotationDirections = EnumSet.noneOf(RotationDirection.class);
        final double maxTorque = body.getAngularMass() * maxAngularAcceleration;
        final AngularVelocitySeeker angularVelSeeker = AngularVelocitySeeker.createStartingAsAngularFriction(body, maxTorque);

        return new VelocityController(keyToMovementDir, speed, activeMovementDirections, velSeeker,
                                      keyToRotationDir, angularSpeed, activeRotationDirections, angularVelSeeker);
    }

    /**
     * Potentially starts moving or rotating based on the pressed key
     *
     * @param keyEvent The key event containing the pressed key
     */
    public void keyPressed(final KeyEvent keyEvent) {
        getMovementDirectionOfKey(keyEvent).ifPresent(this::startMovementInDirection);
        getRotationDirectionOfKey(keyEvent).ifPresent(this::startRotationInDirection);
    }

    /**
     * Potentially stops moving or rotating in a direction based on the released key
     *
     * @param keyEvent The event containing the released key
     */
    public void keyReleased(final KeyEvent keyEvent) {
        getMovementDirectionOfKey(keyEvent).ifPresent(this::endMovementInDirection);
        getRotationDirectionOfKey(keyEvent).ifPresent(this::endRotationInDirection);
    }

    private Optional<MovementDirection> getMovementDirectionOfKey(final KeyEvent keyEvent) {
        return Optional.ofNullable(keyToMovementDir.get(keyEvent.getKeyCode()));
    }

    private Optional<RotationDirection> getRotationDirectionOfKey(final KeyEvent keyEvent) {
        return Optional.ofNullable(keyToRotationDir.get(keyEvent.getKeyCode()));
    }

    private void startMovementInDirection(final MovementDirection direction) {
        if (activeMovementDirections.contains(direction)) {
            return;
        }
        activeMovementDirections.add(direction);
        updateTargetVel();
    }

    private void endMovementInDirection(final MovementDirection direction) {
        if (!activeMovementDirections.contains(direction)) {
            return;
        }
        activeMovementDirections.remove(direction);
        updateTargetVel();
    }

    private void updateTargetVel() {
        // Sum all direction vectors
        final Vector2D dirSum = Vector2D.createZero();
        for (MovementDirection activeMovementDir : activeMovementDirections) {
            dirSum.addLocally(activeMovementDir.getDirVector());
        }
        // The sum of the vectors has the correct angle, but its magnitude must be set to the target speed
        velSeeker.setTargetVel(dirSum.isZero() ? Vector2D.createZero() :
                               dirSum.setMagnitude(speed));
    }

    private void startRotationInDirection(final RotationDirection direction) {
        if (activeRotationDirections.contains(direction)) {
            return;
        }
        activeRotationDirections.add(direction);
        updateTargetAngularVel();
    }

    private void endRotationInDirection(final RotationDirection direction) {
        if (!activeRotationDirections.contains(direction)) {
            return;
        }
        activeRotationDirections.remove(direction);
        updateTargetAngularVel();
    }

    private void updateTargetAngularVel() {
        // {} -> 0, {left} -> 1, {right} -> -1, {left, right} -> 0
        double dirSign = 0;
        for (RotationDirection activeRotationDir : activeRotationDirections) {
            dirSign += activeRotationDir.getSign();
        }
        angularVelSeeker.setTargetAngularVel(dirSign * angularSpeed);
    }

    /**
     * Generates a velocity constraint that tries to set the desired velocity and angular velocity,
     * but is limited by the maximum force and torque
     *
     * @param deltaTime The size of the upcoming time step
     * @return The generated velocity constraint
     */
    @Override public ActiveVelocityConstraint generateConstraint(final double deltaTime) {
        // Combine the velocity and the angular velocity constraints
        return ActiveVelocityConstraintList.createWithSingleIteration(
                velSeeker.generateConstraint(deltaTime),
                angularVelSeeker.generateConstraint(deltaTime)
        );
    }
}
