package se.liu.jonla400.restructure.main;

import se.liu.jonla400.restructure.main.drawing.BodyDrawerSet;
import se.liu.jonla400.restructure.main.drawing.CircleDrawer;
import se.liu.jonla400.restructure.main.drawing.CrossDrawer;
import se.liu.jonla400.restructure.main.drawing.CustomShapeDrawer;
import se.liu.jonla400.restructure.math.Vector2D;
import se.liu.jonla400.restructure.physics.abstraction.main.Body;
import se.liu.jonla400.restructure.physics.abstraction.main.PhysicsEngine;
import se.liu.jonla400.restructure.physics.implementation.collision.CircleCollider;
import se.liu.jonla400.restructure.physics.implementation.collision.CircleVsCustomCollisionDetector;
import se.liu.jonla400.restructure.physics.implementation.collision.CustomCollider;
import se.liu.jonla400.restructure.physics.implementation.collision.CustomShape;
import se.liu.jonla400.restructure.physics.implementation.collision.TranslatedCustomShape;
import se.liu.jonla400.restructure.physics.implementation.collision.TranslatedCustomShapeDefinition;
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
    private CustomCollider levelCollider;
    private Body circleBody;

    private OffsetVelocitySeeker velSeeker;
    private double movementSpeed;
    private Set<MovementDirection> activeMovementDirections;

    private AngularVelocitySeeker angularVelSeeker;
    private double angularSpeed;
    private Set<RotationDirection> activeRotationDirections;

    public Level(final DrawRegion preferredDrawRegion, final Vector2D cursorPos, final boolean pointAtCursorGrabbed,
                 final double scaleFactor, final PhysicsEngine physicsEngine, final BodyDrawerSet bodyDrawers,
                 final CustomCollider levelCollider, final Body circleBody, final OffsetVelocitySeeker velSeeker,
                 final double movementSpeed, final Set<MovementDirection> activeMovementDirections,
                 final AngularVelocitySeeker angularVelSeeker, final double angularSpeed,
                 final Set<RotationDirection> activeRotationDirections)
    {
        this.preferredDrawRegion = preferredDrawRegion;
        this.cursorPos = cursorPos;
        this.pointAtCursorGrabbed = pointAtCursorGrabbed;
        this.scaleFactor = scaleFactor;
        this.physicsEngine = physicsEngine;
        this.bodyDrawers = bodyDrawers;
        this.levelCollider = levelCollider;
        this.circleBody = circleBody;
        this.velSeeker = velSeeker;
        this.movementSpeed = movementSpeed;
        this.activeMovementDirections = activeMovementDirections;
        this.angularVelSeeker = angularVelSeeker;
        this.angularSpeed = angularSpeed;
        this.activeRotationDirections = activeRotationDirections;
    }

    public static Level createFromDefinition(final LevelDefinition definition) {
        final Vector2D cursorPos = Vector2D.createZeroVector();
        final double scaleFactor = definition.getScaleFactor();

        final PhysicsEngine physicsEngine = new PhysicsEngine(10);
        final BodyDrawerSet bodyDrawers = BodyDrawerSet.create();

        final Body levelBody = new Body();
        levelBody.setPos(definition.getLevelPos());
        levelBody.setVel(Vector2D.createZeroVector());
        levelBody.setMass(definition.getLevelMass());
        levelBody.setAngle(0);
        levelBody.setAngularVel(0);
        levelBody.setAngularMass(definition.getAngularMass());

        final TranslatedCustomShapeDefinition translatedLevelShapeDef = TranslatedCustomShapeDefinition.create(
                Vector2D.createCartesian(0, 1), definition.getLevelShapeDefinition());
        final TranslatedCustomShape translatedLevelShape = TranslatedCustomShape.createFromDefinition(translatedLevelShapeDef);
        final CustomCollider levelCollider = new CustomCollider(levelBody, translatedLevelShape);

        final Body circleBody = new Body();
        circleBody.setPos(definition.getCirclePos());
        circleBody.setVel(Vector2D.createZeroVector());
        circleBody.setMass(definition.getCircleMass());
        circleBody.setAngle(0);
        circleBody.setAngularVel(0);
        circleBody.setAngularMass(definition.getCircleAngularMass());

        final double circleRadius = definition.getCircleRadius();
        final CircleCollider circleCollider = new CircleCollider(circleBody, circleRadius);

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
        physicsEngine.add(circleBody);
        physicsEngine.add(new CircleVsCustomCollisionDetector(circleCollider, levelCollider));

        bodyDrawers.add(levelBody,
                        CustomShapeDrawer.createWithDefaultColor(translatedLevelShape, 0.1f),
                        CrossDrawer.createWithDefaultColor(1, 0.1f)
        );
        bodyDrawers.add(circleBody,
                        CircleDrawer.createWithDefaultColors(circleRadius, 0.1f),
                        CrossDrawer.createWithDefaultColor(0.1, 0.05f)
        );

        return new Level(definition.getPreferredDrawRegion(), cursorPos, false, scaleFactor, physicsEngine, bodyDrawers,
                         levelCollider, circleBody, velSeeker, movementSpeed, activeMovementDirections, angularVelSeeker, angularSpeed,
                         activeRotationDirections);
    }

    public void tick(final double deltaTime) {
        final Vector2D gravityAcc = Vector2D.createCartesian(0, -9.82);
        final Vector2D gravityDeltaVel = gravityAcc.multiply(deltaTime);
        circleBody.setVel(circleBody.getVel().add(gravityDeltaVel));
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
        final Body levelBody = levelCollider.getBody();
        final TranslatedCustomShape shape = levelCollider.getShape();

        final Vector2D globalShapePos = levelBody.convertLocalPointToGlobalPoint(shape.getTranslation());
        levelBody.setPos(cursorPos);
        shape.setTranslation(levelBody.convertGlobalPointToLocalPoint(globalShapePos));

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
