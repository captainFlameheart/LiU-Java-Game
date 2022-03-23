package se.liu.jonla400.project.main.game;

import se.liu.jonla400.project.main.drawing.BodyDrawer;
import se.liu.jonla400.project.main.drawing.DrawConfiguration;
import se.liu.jonla400.project.main.drawing.DrawerList;
import se.liu.jonla400.project.main.leveldefinition.LineSegmentType;
import se.liu.jonla400.project.math.RectangularRegion;
import se.liu.jonla400.project.main.drawing.CustomShapeDrawer;
import se.liu.jonla400.project.main.world.AdaptingWorld;
import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.collision.CollisionHandler;
import se.liu.jonla400.project.physics.main.Body;
import se.liu.jonla400.project.physics.main.PhysicsEngine;
import se.liu.jonla400.project.physics.collision.implementation.CircleCollider;
import se.liu.jonla400.project.physics.collision.implementation.CircleVsCustomCollisionDetector;
import se.liu.jonla400.project.physics.collision.implementation.CustomCollider;
import se.liu.jonla400.project.physics.collision.implementation.CustomShape;
import se.liu.jonla400.project.physics.collision.implementation.TranslatedCustomShape;
import se.liu.jonla400.project.main.leveldefinition.LevelDefinition;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

public class LevelWorld extends AdaptingWorld
{
    private PhysicsEngine physicsEngine;
    private DrawerList bodyDrawers;
    private Body circleBody;

    private CenterOfMassController centerOfMassController;
    private VelocityController velController;

    private LevelEventSender levelEventSender;

    private LevelWorld(final PhysicsEngine physicsEngine, final DrawerList bodyDrawers, final Body circleBody,
                      final CenterOfMassController centerOfMassController, final VelocityController velController,
                      final LevelEventSender levelEventSender)
    {
        this.physicsEngine = physicsEngine;
        this.bodyDrawers = bodyDrawers;
        this.circleBody = circleBody;
        this.centerOfMassController = centerOfMassController;
        this.velController = velController;
        this.levelEventSender = levelEventSender;
    }

    public static LevelWorld create(final LevelDefinition definition, final DrawConfiguration drawConfig) {
        final Body levelBody = createLevelBodyAt(definition.getCenterOfMass());
        final Vector2D shapeTranslation = levelBody.getPos().negate();
        final CustomShape<LineSegmentType> shape = definition.getShape().convertToCollidableShape();
        final TranslatedCustomShape<LineSegmentType> translatedShape = new TranslatedCustomShape<>(shapeTranslation, shape);
        final CustomCollider<LineSegmentType> levelCollider = new CustomCollider<>(levelBody, translatedShape);

        final Body ballBody = createBallBodyAt(definition.getBallPos());
        final double ballRadius = definition.getBallRadius();
        final CircleCollider ballCollider = new CircleCollider(ballBody, ballRadius);

        final VelocityController velController = VelocityController.createWithDefaultConfigFor(levelBody);
        final CenterOfMassController centerOfMassController = CenterOfMassController.createWithDefaultConfigFor(levelCollider);

        final CollisionHandler<LineSegmentType> collisionHandler = CollisionHandler.createWithDefaultConfig(
                CircleVsCustomCollisionDetector.createWithDefaultUniformMaterial(ballCollider, levelCollider)
        );
        final LevelEventSender levelEventSender = LevelEventSender.createWithoutListeners();
        collisionHandler.addListener(levelEventSender);

        final PhysicsEngine physicsEngine = PhysicsEngine.createWithDefaultVelIterations();
        physicsEngine.add(levelBody, ballBody);
        physicsEngine.add(velController, collisionHandler);

        final DrawerList bodyDrawers = DrawerList.create(
                new BodyDrawer(levelBody, DrawerList.create(
                        new CustomShapeDrawer(translatedShape, drawConfig.getLineSegmentDrawer()),
                        drawConfig.getCenterOfMassDrawer()
                )),
                new BodyDrawer(ballBody, drawConfig.getBallDrawer(ballRadius))
        );

        return new LevelWorld(physicsEngine, bodyDrawers, ballBody, centerOfMassController, velController, levelEventSender);
    }

    private static Body createLevelBodyAt(final Vector2D pos) {
        final double levelMass = 50;
        final double levelAngularMass = 50;
        return Body.create(pos, levelMass, levelAngularMass);
    }

    private static Body createBallBodyAt(final Vector2D pos) {
        final double ballMass = 1;
        final double ballAngularMass = 0.01;
        return Body.create(pos, ballMass, ballAngularMass);
    }

    @Override public void updateMousePos(final Vector2D newMousePos) {
        centerOfMassController.updateMousePos(newMousePos);
    }

    @Override public void mousePressed(final MouseEvent mouseEvent) {
        centerOfMassController.mousePressed(mouseEvent);
    }

    @Override public void keyPressed(final KeyEvent keyEvent) {
        velController.keyPressed(keyEvent);
    }

    @Override public void keyReleased(final KeyEvent keyEvent) {
        velController.keyReleased(keyEvent);
    }

    @Override public void tick(final double deltaTime) {
        applyGravityToCircle(deltaTime);
        physicsEngine.tick(deltaTime);
    }

    private void applyGravityToCircle(final double deltaTime) {
        final Vector2D gravityAcceleration = Vector2D.createCartesian(0, -9.82);
        final Vector2D gravityDeltaVel = gravityAcceleration.multiply(deltaTime);
        circleBody.setVel(circleBody.getVel().add(gravityDeltaVel));
    }

    @Override public void draw(final Graphics2D g, final RectangularRegion rectangularRegion) {
        drawBackground(g, rectangularRegion);
        bodyDrawers.draw(g);
    }

    private void drawBackground(final Graphics2D g, final RectangularRegion region) {
        g.setColor(Color.WHITE);
        g.fill(new Rectangle2D.Double(region.getLeftX(), region.getBottomY(), region.getWidth(), region.getHeight()));
    }

    public void addListener(final LevelListener listener) {
        levelEventSender.addListener(listener);
    }
}
