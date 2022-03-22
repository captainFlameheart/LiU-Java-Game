package se.liu.jonla400.project.main.game;

import se.liu.jonla400.project.constants.Constants;
import se.liu.jonla400.project.main.leveldefinition.LineSegmentType;
import se.liu.jonla400.project.math.RectangularRegion;
import se.liu.jonla400.project.main.drawing.BodyDrawerSet;
import se.liu.jonla400.project.main.drawing.BallDrawer;
import se.liu.jonla400.project.main.drawing.CrossDrawer;
import se.liu.jonla400.project.main.drawing.CustomShapeDrawer;
import se.liu.jonla400.project.main.world.AdaptingWorld;
import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.abstraction.collision.CollisionData;
import se.liu.jonla400.project.physics.abstraction.collision.CollisionHandler;
import se.liu.jonla400.project.physics.abstraction.collision.CollisionListener;
import se.liu.jonla400.project.physics.abstraction.main.Body;
import se.liu.jonla400.project.physics.abstraction.main.PhysicsEngine;
import se.liu.jonla400.project.physics.implementation.collision.CircleCollider;
import se.liu.jonla400.project.physics.implementation.collision.CircleVsCustomCollisionDetector;
import se.liu.jonla400.project.physics.implementation.collision.CustomCollider;
import se.liu.jonla400.project.physics.implementation.collision.CustomShape;
import se.liu.jonla400.project.physics.implementation.collision.TranslatedCustomShape;
import se.liu.jonla400.project.physics.implementation.constraint.AngularVelocitySeeker;
import se.liu.jonla400.project.main.leveldefinition.LevelDefinition;
import se.liu.jonla400.project.physics.implementation.constraint.VelocitySeeker;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

public class LevelWorld extends AdaptingWorld implements CollisionListener<LineSegmentType>
{
    private final static double LEVEL_MASS = 50;
    private final static double LEVEL_SPEED = 10;
    private final static double LEVEL_MAX_ACC = 20;

    private final static double LEVEL_ANGULAR_MASS = 50;
    private final static double LEVEL_ANGULAR_SPEED = 2;
    private final static double LEVEL_MAX_ANGULAR_ACC = 10;

    private final static double BALL_MASS = 1;
    private final static double BALL_ANGULAR_MASS = 0.01;

    private Vector2D cursorPos;

    private PhysicsEngine physicsEngine;
    private BodyDrawerSet bodyDrawers;
    private CustomCollider<LineSegmentType> levelCollider;
    private Body circleBody;

    private VelocitySeeker velSeeker;
    private double movementSpeed;
    private Set<MovementDirection> activeMovementDirections;

    private AngularVelocitySeeker angularVelSeeker;
    private double angularSpeed;
    private Set<RotationDirection> activeRotationDirections;

    private Collection<LevelListener> listeners;

    public LevelWorld(final Vector2D cursorPos, final PhysicsEngine physicsEngine, final BodyDrawerSet bodyDrawers,
                      final CustomCollider<LineSegmentType> levelCollider, final Body circleBody,
                      final VelocitySeeker velSeeker,
                      final Set<MovementDirection> activeMovementDirections,
                      final AngularVelocitySeeker angularVelSeeker,
                      final Set<RotationDirection> activeRotationDirections,
                      final Collection<LevelListener> listeners
                 )
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
        this.listeners = listeners;
    }

    public static LevelWorld createFromDefinition(final LevelDefinition definition) {
        final Vector2D cursorPos = Vector2D.createZero();
        final double scaleFactor = 1.1;

        final PhysicsEngine physicsEngine = new PhysicsEngine(10);
        final BodyDrawerSet bodyDrawers = BodyDrawerSet.create();

        final Body levelBody = new Body();
        levelBody.setPos(definition.getCenterOfMass());
        levelBody.setVel(Vector2D.createZero());
        levelBody.setMass(Constants.getLevelMass());
        levelBody.setAngle(0);
        levelBody.setAngularVel(0);
        levelBody.setAngularMass(Constants.getLevelAngularMass());

        final Vector2D shapeTranslation = definition.getCenterOfMass().negate();
        final CustomShape<LineSegmentType> levelShape = definition.getShape().convertToCollidableShape();
        final TranslatedCustomShape<LineSegmentType> translatedLevelShape = new TranslatedCustomShape<>(shapeTranslation, levelShape);
        final CustomCollider<LineSegmentType> levelCollider = new CustomCollider<>(levelBody, translatedLevelShape);

        final Body circleBody = new Body();
        circleBody.setPos(Constants.getBallSpawnPos());
        circleBody.setVel(Vector2D.createZero());
        circleBody.setMass(BALL_MASS);
        circleBody.setAngle(0);
        circleBody.setAngularVel(0);
        circleBody.setAngularMass(BALL_ANGULAR_MASS);

        final double circleRadius = Constants.getBallRadius();
        final CircleCollider circleCollider = new CircleCollider(circleBody, circleRadius);

        final double maxForce = Constants.getLevelMass() * Constants.getLevelMaxAcc();
        final VelocitySeeker velSeeker = new VelocitySeeker(levelBody, Vector2D.createZero(), maxForce);
        final Set<MovementDirection> activeMovementDirections = EnumSet.noneOf(MovementDirection.class);

        final double maxTorque = Constants.getLevelAngularMass() * Constants.getLevelMaxAngularAcc();
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
                        new BallDrawer(),
			CrossDrawer.createWithDefaultColor(0.1, 0.05f)
        );

        final Collection<LevelListener> listeners = new ArrayList<>();
        final LevelWorld levelWorld = new LevelWorld(cursorPos, physicsEngine, bodyDrawers,
                                                     levelCollider, circleBody, velSeeker, activeMovementDirections, angularVelSeeker,
                                                     activeRotationDirections, listeners);
        collisionHandler.addListener(levelWorld);
        return levelWorld;
    }

    @Override public void updateMousePos(final Vector2D newMousePos) {
        cursorPos.set(newMousePos);
    }

    @Override public void mousePressed(final MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseEvent.BUTTON1) {
            setCenterOfMassAtCursor();
        }
    }

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

    public void setCenterOfMassAtCursor() {
        final Body levelBody = levelCollider.getBody();
        final TranslatedCustomShape<LineSegmentType> shape = levelCollider.getShape();

        final Vector2D globalShapePos = levelBody.convertLocalPointToGlobalPoint(shape.getTranslation());
        levelBody.setPos(cursorPos);
        shape.setTranslation(levelBody.convertGlobalPointToLocalPoint(globalShapePos));
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
        final Vector2D dirSum = Vector2D.createZero();
        for (MovementDirection activeMovementDir : activeMovementDirections) {
            dirSum.addLocally(activeMovementDir.getDirVector());
        }
        velSeeker.setTargetVel(dirSum.isZero() ? Vector2D.createZero() :
                               dirSum.setMagnitude(Constants.getLevelSpeed()));
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
        angularVelSeeker.setTargetAngularVel(dirSign * Constants.getLevelAngularSpeed());
    }

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

    public void addListener(final LevelListener listener) {
        listeners.add(listener);
    }

    @Override public void collisionOccured(final CollisionData<LineSegmentType> collision) {
        final LineSegmentType lineSegmentType = collision.getUserData();
        if (lineSegmentType == LineSegmentType.LOOSE) {
            listeners.forEach(LevelListener::levelFailed);
        } else if (lineSegmentType == LineSegmentType.WIN) {
            listeners.forEach(LevelListener::levelCompleted);
        }
    }
}
