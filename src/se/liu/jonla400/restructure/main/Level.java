package se.liu.jonla400.restructure.main;

import se.liu.jonla400.restructure.main.drawing.BodyDrawerSet;
import se.liu.jonla400.restructure.main.drawing.CrossDrawer;
import se.liu.jonla400.restructure.math.Vector2D;
import se.liu.jonla400.restructure.physics.abstraction.main.Body;
import se.liu.jonla400.restructure.physics.abstraction.main.PhysicsEngine;
import se.liu.jonla400.restructure.physics.implementation.constraint.AngularVelocitySeeker;
import se.liu.jonla400.restructure.physics.implementation.constraint.OffsetVelocitySeeker;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.EnumSet;
import java.util.Set;

public class Level
{
    private DrawRegion preferredDrawRegion;
    private Vector2D cursorPos;
    private boolean pointAtCursorGrabbed;
    private double scaleFactor;

    private PhysicsEngine physicsEngine;
    private BodyDrawerSet bodyDrawers;
    private Body levelBody;

    private OffsetVelocitySeeker velSeeker;
    private double movementSpeed;
    private Set<MovementDirection> activeMovementDirections;

    private AngularVelocitySeeker angularVelSeeker;
    private double angularSpeed;
    private Set<RotationDirection> activeRotationDirections;

    public Level(final DrawRegion preferredDrawRegion, final PhysicsEngine physicsEngine, final BodyDrawerSet bodyDrawers,
                 final Body levelBody, final Vector2D cursorPos, final OffsetVelocitySeeker velSeeker, final double movementSpeed,
                 final Set<MovementDirection> activeMovementDirections, final AngularVelocitySeeker angularVelSeeker,
                 final double angularSpeed, final Set<RotationDirection> activeRotationDirections, final double scaleFactor)
    {
        this.preferredDrawRegion = preferredDrawRegion;
        this.physicsEngine = physicsEngine;
        this.bodyDrawers = bodyDrawers;
        this.levelBody = levelBody;
        this.cursorPos = cursorPos;
        this.velSeeker = velSeeker;
        this.movementSpeed = movementSpeed;
        this.activeMovementDirections = activeMovementDirections;
        this.angularVelSeeker = angularVelSeeker;
        this.angularSpeed = angularSpeed;
        this.activeRotationDirections = activeRotationDirections;
        this.scaleFactor = scaleFactor;
    }

    public static Level createFromDefinition(final LevelDefinition definition) {
        final PhysicsEngine physicsEngine = new PhysicsEngine(10);
        final BodyDrawerSet bodyDrawers = BodyDrawerSet.create();

        final Body levelBody = new Body();
        levelBody.setPos(definition.getPos());
        levelBody.setVel(Vector2D.createZeroVector());
        levelBody.setMass(definition.getLevelMass());

        final Vector2D cursorPos = Vector2D.createZeroVector();

        final double maxForce = definition.getLevelMass() * definition.getMaxMovementAcc();
        final OffsetVelocitySeeker velSeeker = new OffsetVelocitySeeker(
                levelBody, Vector2D.createZeroVector(), Vector2D.createZeroVector(), maxForce);
        final double movementSpeed = definition.getMovementSpeed();
        final Set<MovementDirection> activeMovementDirections = EnumSet.noneOf(MovementDirection.class);

        final double maxTorque = definition.getAngularMass() * definition.getMaxAngularAcc();
        final AngularVelocitySeeker angularVelSeeker = new AngularVelocitySeeker(
                levelBody, 0, maxTorque);
        final double angularSpeed = definition.getAngularSpeed();
        final Set<RotationDirection> activeRotationDirections = EnumSet.noneOf(RotationDirection.class);

        physicsEngine.add(levelBody);
        physicsEngine.add(velSeeker);
        physicsEngine.add(angularVelSeeker);

        bodyDrawers.add(levelBody, CrossDrawer.createWithDefaultColor(1, 0.1f));

        final double scaleFactor = definition.getScaleFactor();

        return new Level(definition.getPreferredDrawRegion(), physicsEngine, bodyDrawers, levelBody, cursorPos, velSeeker,
                         movementSpeed, activeMovementDirections, angularVelSeeker, angularSpeed, activeRotationDirections, scaleFactor);
    }

    public void tick(final double deltaTime) {
        physicsEngine.tick(deltaTime);
    }

    public void setCursorPos(final Vector2D cursorPos) {
        if (pointAtCursorGrabbed) {
            final Vector2D deltaCameraPos = this.cursorPos.subtract(cursorPos);
            final Vector2D cameraCenter = preferredDrawRegion.getCenter();
            final Vector2D newCameraCenter = cameraCenter.add(deltaCameraPos);
            final Vector2D cameraSize = preferredDrawRegion.getSize();
            preferredDrawRegion = DrawRegion.createFromCenter(newCameraCenter, cameraSize);
        } else {
            this.cursorPos.set(cursorPos);
        }
    }

    public Vector2D getCursorPos() {
        return cursorPos;
    }

    public void setPointAtCursorGrabbed(final boolean pointAtCursorGrabbed) {
        this.pointAtCursorGrabbed = pointAtCursorGrabbed;
    }

    public void setCenterOfMassAtCursor() {
        levelBody.setPos(cursorPos);
    }

    public void scale(final double count) {
        final double scale = Math.pow(scaleFactor, count);

        final Vector2D size = preferredDrawRegion.getSize();
        final Vector2D newSize = size.multiply(scale);
        final Vector2D center = preferredDrawRegion.getCenter();
        preferredDrawRegion = DrawRegion.createFromCenter(center, newSize);
    }


    public void startMovementInDirection(final MovementDirection direction) {
        if (activeMovementDirections.contains(direction)) {
            return;
        }
        activeMovementDirections.add(direction);
        updateTargetVel();
    }

    public void endMovementInDirection(final MovementDirection direction) {
        if (!activeMovementDirections.contains(direction)) {
            return;
        }
        activeMovementDirections.remove(direction);
        updateTargetVel();
    }

    private void updateTargetVel() {
        final Vector2D dirSum = Vector2D.createZeroVector();
        for (MovementDirection activeMovementDir : activeMovementDirections) {
            dirSum.addLocally(activeMovementDir.getDirVector());
        }
        velSeeker.setTargetVel(dirSum.isZero() ? Vector2D.createZeroVector() : dirSum.setMagnitude(movementSpeed));
    }

    public void startRotationInDirection(final RotationDirection direction) {
        if (activeRotationDirections.contains(direction)) {
            return;
        }
        activeRotationDirections.add(direction);
        updateTargetAngularVel();
    }

    public void endRotationInDirection(final RotationDirection direction) {
        if (!activeRotationDirections.contains(direction)) {
            return;
        }
        activeRotationDirections.remove(direction);
        updateTargetAngularVel();
    }

    private void updateTargetAngularVel() {
        double dirSign = 0;
        for (RotationDirection activeRotationDir : activeRotationDirections) {
            dirSign += activeRotationDir.getSign();
        }
        angularVelSeeker.setTargetAngularVel(dirSign * angularSpeed);
    }

    public DrawRegion getPreferredDrawRegion() {
        return preferredDrawRegion;
    }

    public void draw(final Graphics2D g, final DrawRegion drawRegion) {
        drawBackground(g, drawRegion);
        drawCursor(g);
        bodyDrawers.draw(g);
    }

    private void drawBackground(final Graphics2D g, final DrawRegion drawRegion) {
        g.setColor(Color.WHITE);
        g.fill(new Rectangle2D.Double(drawRegion.getLeftX(), drawRegion.getBottomY(), drawRegion.getWidth(), drawRegion.getHeight()));
    }

    private void drawCursor(final Graphics2D g) {
        final double radius = 0.1;
        final double diameter = 2 * radius;
        g.setColor(Color.BLACK);
        g.fill(new Ellipse2D.Double(cursorPos.getX() - radius, cursorPos.getY() - radius, diameter, diameter));
    }
}
