package se.liu.jonla400.restructure.main;

import se.liu.jonla400.restructure.constants.PhysicsConstants;
import se.liu.jonla400.restructure.main.drawing.BodyDrawerSet;
import se.liu.jonla400.restructure.main.drawing.CircleDrawer;
import se.liu.jonla400.restructure.main.drawing.CrossDrawer;
import se.liu.jonla400.restructure.main.drawing.CustomShapeDrawer;
import se.liu.jonla400.restructure.main.levelcreation.LineSegmentType;
import se.liu.jonla400.restructure.math.Vector2D;
import se.liu.jonla400.restructure.physics.abstraction.collision.CollisionDetector;
import se.liu.jonla400.restructure.physics.abstraction.collision.CollisionHandler;
import se.liu.jonla400.restructure.physics.abstraction.main.Body;
import se.liu.jonla400.restructure.physics.abstraction.main.PhysicsEngine;
import se.liu.jonla400.restructure.physics.implementation.collision.CircleCollider;
import se.liu.jonla400.restructure.physics.implementation.collision.CircleVsCustomCollisionDetector;
import se.liu.jonla400.restructure.physics.implementation.collision.CustomCollider;
import se.liu.jonla400.restructure.physics.implementation.collision.TranslatedCustomShape;
import se.liu.jonla400.restructure.physics.implementation.constraint.AngularVelocitySeeker;
import se.liu.jonla400.restructure.physics.implementation.constraint.OffsetVelocitySeeker;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.EnumSet;
import java.util.Set;

public class Level implements World
{
    private Vector2D cursorPos;

    private PhysicsEngine physicsEngine;
    private BodyDrawerSet bodyDrawers;
    private CustomCollider<LineSegmentType> levelCollider;
    private Body circleBody;

    private OffsetVelocitySeeker velSeeker;
    private double movementSpeed;
    private Set<MovementDirection> activeMovementDirections;

    private AngularVelocitySeeker angularVelSeeker;
    private double angularSpeed;
    private Set<RotationDirection> activeRotationDirections;

    public Level(final Vector2D cursorPos, final PhysicsEngine physicsEngine, final BodyDrawerSet bodyDrawers,
		 final CustomCollider<LineSegmentType> levelCollider, final Body circleBody,
                 final OffsetVelocitySeeker velSeeker,
		 final Set<MovementDirection> activeMovementDirections,
		 final AngularVelocitySeeker angularVelSeeker,
		 final Set<RotationDirection> activeRotationDirections)
    {
        this.cursorPos = cursorPos;
        this.physicsEngine = physicsEngine;
        this.bodyDrawers = bodyDrawers;
        this.levelCollider = levelCollider;
        this.circleBody = circleBody;
        this.velSeeker = velSeeker;
        this.activeMovementDirections = activeMovementDirections;
        this.angularVelSeeker = angularVelSeeker;
        this.activeRotationDirections = activeRotationDirections;
    }

    public static Level createFromDefinition(final LevelDefinition definition) {
        final Vector2D cursorPos = Vector2D.createZero();
        final double scaleFactor = 1.1;

        final PhysicsEngine physicsEngine = new PhysicsEngine(10);
        final BodyDrawerSet bodyDrawers = BodyDrawerSet.create();

        final Body levelBody = new Body();
        levelBody.setPos(definition.getCenterOfMass());
        levelBody.setVel(Vector2D.createZero());
        levelBody.setMass(PhysicsConstants.getLevelMass());
        levelBody.setAngle(0);
        levelBody.setAngularVel(0);
        levelBody.setAngularMass(PhysicsConstants.getLevelAngularMass());

        final Vector2D shapeTranslation = definition.getCenterOfMass().negate();
        final TranslatedCustomShape<LineSegmentType> translatedLevelShape = new TranslatedCustomShape<>(shapeTranslation, definition.getShape());
        final CustomCollider<LineSegmentType> levelCollider = new CustomCollider<>(levelBody, translatedLevelShape);

        final Body circleBody = new Body();
        circleBody.setPos(PhysicsConstants.getBallSpawnPos());
        circleBody.setVel(Vector2D.createZero());
        circleBody.setMass(PhysicsConstants.getBallMass());
        circleBody.setAngle(0);
        circleBody.setAngularVel(0);
        circleBody.setAngularMass(PhysicsConstants.getBallAngularMass());

        final double circleRadius = PhysicsConstants.getBallRadius();
        final CircleCollider circleCollider = new CircleCollider(circleBody, circleRadius);

        final double maxForce = PhysicsConstants.getLevelMass() * PhysicsConstants.getLevelMaxAcc();
        final OffsetVelocitySeeker velSeeker = new OffsetVelocitySeeker(
		levelBody, Vector2D.createZero(), Vector2D.createZero(), maxForce);
        final Set<MovementDirection> activeMovementDirections = EnumSet.noneOf(MovementDirection.class);

        final double maxTorque = PhysicsConstants.getLevelAngularMass() * PhysicsConstants.getLevelMaxAngularAcc();
        final AngularVelocitySeeker angularVelSeeker = new AngularVelocitySeeker(
                levelBody, 0, maxTorque);
        final Set<RotationDirection> activeRotationDirections = EnumSet.noneOf(RotationDirection.class);

        physicsEngine.add(levelBody);
        physicsEngine.add(velSeeker);
        physicsEngine.add(angularVelSeeker);
        physicsEngine.add(circleBody);
        final CollisionHandler<LineSegmentType> collisionHandler = new CollisionHandler<>(
                new CircleVsCustomCollisionDetector<>(circleCollider, levelCollider)
        );
        physicsEngine.add(collisionHandler);

        bodyDrawers.add(levelBody,
                        new CustomShapeDrawer(translatedLevelShape),
                        CrossDrawer.createWithDefaultColor(1, 0.1f)
        );
        bodyDrawers.add(circleBody,
                        CircleDrawer.createWithDefaultColors(circleRadius, 0.1f),
                        CrossDrawer.createWithDefaultColor(0.1, 0.05f)
        );

        return new Level(cursorPos, physicsEngine, bodyDrawers,
			 levelCollider, circleBody, velSeeker, activeMovementDirections, angularVelSeeker,
			 activeRotationDirections);
    }

    @Override public void cursorMoved(final Vector2D newCursorPos) {
        cursorPos.set(newCursorPos);
    }

    @Override public void cursorPressed() {
        setCenterOfMassAtCursor();
    }

    @Override public void cursorReleased() {}

    @Override public void keyPressed(final KeyEvent keyEvent) {
        final int keyCode = keyEvent.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_A -> startMovementInDirection(MovementDirection.LEFT);
            case KeyEvent.VK_D -> startMovementInDirection(MovementDirection.RIGHT);
            case KeyEvent.VK_S -> startMovementInDirection(MovementDirection.DOWN);
            case KeyEvent.VK_W -> startMovementInDirection(MovementDirection.UP);
            case KeyEvent.VK_Q -> startRotationInDirection(RotationDirection.LEFT);
            case KeyEvent.VK_E -> startRotationInDirection(RotationDirection.RIGHT);
        }
    }

    @Override public void keyReleased(final KeyEvent keyEvent) {
        final int keyCode = keyEvent.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_A -> endMovementInDirection(MovementDirection.LEFT);
            case KeyEvent.VK_D -> endMovementInDirection(MovementDirection.RIGHT);
            case KeyEvent.VK_S -> endMovementInDirection(MovementDirection.DOWN);
            case KeyEvent.VK_W -> endMovementInDirection(MovementDirection.UP);
            case KeyEvent.VK_Q -> endRotationInDirection(RotationDirection.LEFT);
            case KeyEvent.VK_E -> endRotationInDirection(RotationDirection.RIGHT);
        }
    }

    public void tick(final double deltaTime) {
        final Vector2D gravityAcc = Vector2D.createCartesian(0, -9.82);
        final Vector2D gravityDeltaVel = gravityAcc.multiply(deltaTime);
        circleBody.setVel(circleBody.getVel().add(gravityDeltaVel));
        physicsEngine.tick(deltaTime);
    }

    /*public void setCursorPos(final Vector2D cursorPos) {
        if (pointAtCursorGrabbed) {
            final Vector2D deltaCameraPos = this.cursorPos.subtract(cursorPos);
            final Vector2D cameraCenter = preferredRectangularRegion.getCenter();
            final Vector2D newCameraCenter = cameraCenter.add(deltaCameraPos);
            final Vector2D cameraSize = preferredRectangularRegion.getSize();
	    preferredRectangularRegion = RectangularRegion.createFromCenter(newCameraCenter, cameraSize);
        } else {
            this.cursorPos.set(cursorPos);
        }
    }*/

    /*public Vector2D getCursorPos() {
        return cursorPos;
    }*/

    /*public void setPointAtCursorGrabbed(final boolean pointAtCursorGrabbed) {
        this.pointAtCursorGrabbed = pointAtCursorGrabbed;
    }*/

    public void setCenterOfMassAtCursor() {
        final Body levelBody = levelCollider.getBody();
        final TranslatedCustomShape<LineSegmentType> shape = levelCollider.getShape();

        final Vector2D globalShapePos = levelBody.convertLocalPointToGlobalPoint(shape.getTranslation());
        levelBody.setPos(cursorPos);
        shape.setTranslation(levelBody.convertGlobalPointToLocalPoint(globalShapePos));
    }

    /*public void scale(final double count) {
        final double scale = Math.pow(scaleFactor, count);

        final Vector2D size = preferredRectangularRegion.getSize();
        final Vector2D newSize = size.multiply(scale);
        final Vector2D center = preferredRectangularRegion.getCenter();
	preferredRectangularRegion = RectangularRegion.createFromCenter(center, newSize);
    }*/


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
        final Vector2D dirSum = Vector2D.createZero();
        for (MovementDirection activeMovementDir : activeMovementDirections) {
            dirSum.addLocally(activeMovementDir.getDirVector());
        }
        velSeeker.setTargetVel(dirSum.isZero() ? Vector2D.createZero() :
                               dirSum.setMagnitude(PhysicsConstants.getLevelSpeed()));
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
        angularVelSeeker.setTargetAngularVel(dirSign * PhysicsConstants.getLevelAngularSpeed());
    }

    /*public RectangularRegion getPreferredDrawRegion() {
        return preferredRectangularRegion;
    }*/

    public void draw(final Graphics2D g, final RectangularRegion rectangularRegion) {
        drawBackground(g, rectangularRegion);
        drawCursor(g);
        bodyDrawers.draw(g);
    }

    private void drawBackground(final Graphics2D g, final RectangularRegion rectangularRegion) {
        g.setColor(Color.WHITE);
        g.fill(new Rectangle2D.Double(rectangularRegion.getLeftX(), rectangularRegion.getBottomY(), rectangularRegion.getWidth(), rectangularRegion.getHeight()));
    }

    private void drawCursor(final Graphics2D g) {
        final double radius = 0.1;
        final double diameter = 2 * radius;
        g.setColor(Color.BLACK);
        g.fill(new Ellipse2D.Double(cursorPos.getX() - radius, cursorPos.getY() - radius, diameter, diameter));
    }
}
