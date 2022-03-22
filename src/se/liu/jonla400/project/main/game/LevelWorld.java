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
import se.liu.jonla400.project.physics.abstraction.collision.CollisionHandler;
import se.liu.jonla400.project.physics.abstraction.main.Body;
import se.liu.jonla400.project.physics.abstraction.main.PhysicsEngine;
import se.liu.jonla400.project.physics.implementation.collision.CircleCollider;
import se.liu.jonla400.project.physics.implementation.collision.CircleVsCustomCollisionDetector;
import se.liu.jonla400.project.physics.implementation.collision.CustomCollider;
import se.liu.jonla400.project.physics.implementation.collision.CustomShape;
import se.liu.jonla400.project.physics.implementation.collision.TranslatedCustomShape;
import se.liu.jonla400.project.main.leveldefinition.LevelDefinition;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;

public class LevelWorld extends AdaptingWorld
{
    private PhysicsEngine physicsEngine;
    private BodyDrawerSet bodyDrawers;
    private Body circleBody;

    private CenterOfMassController centerOfMassController;
    private VelocityController velController;

    private LevelEventSender levelEventSender;

    public LevelWorld(final PhysicsEngine physicsEngine, final BodyDrawerSet bodyDrawers, final Body circleBody,
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

    public static LevelWorld createFromDef(final LevelDefinition def) {
        final Body levelBody = Body.create(def.getCenterOfMass(), 50, 50);
        final Vector2D shapeTranslation = levelBody.getPos().negate();
        final CustomShape<LineSegmentType> shape = def.getShape().convertToCollidableShape();
        final TranslatedCustomShape<LineSegmentType> translatedShape = new TranslatedCustomShape<>(shapeTranslation, shape);
        final CustomCollider<LineSegmentType> levelCollider = new CustomCollider<>(levelBody, translatedShape);

        final Body circleBody = Body.create(Constants.getBallSpawnPos(), 1, 0.01);
        final CircleCollider circleCollider = new CircleCollider(circleBody, Constants.getBallRadius());

        final VelocityController velController = VelocityController.createWithDefaultConfigFor(levelBody);
        final CenterOfMassController centerOfMassController = CenterOfMassController.createWithDefaultConfigFor(levelCollider);

        final CollisionHandler<LineSegmentType> collisionHandler = CollisionHandler.createWithDefaultConfig(
                CircleVsCustomCollisionDetector.createWithDefaultUniformMaterial(circleCollider, levelCollider)
        );
        final LevelEventSender levelEventSender = LevelEventSender.createWithoutListeners();
        collisionHandler.addListener(levelEventSender);

        final PhysicsEngine physicsEngine = PhysicsEngine.createWithDefaultVelIterations();
        physicsEngine.add(levelBody, circleBody);
        physicsEngine.add(velController, collisionHandler);

        final BodyDrawerSet bodyDrawers = BodyDrawerSet.create();
        bodyDrawers.add(levelBody, new CustomShapeDrawer(translatedShape), CrossDrawer.createWithDefaultColor(1, 0.1f));
        bodyDrawers.add(circleBody, new BallDrawer(), CrossDrawer.createWithDefaultColor(0.1, 0.05f));

        final Collection<LevelListener> listeners = new ArrayList<>();
        return new LevelWorld(physicsEngine, bodyDrawers, circleBody, centerOfMassController, velController, levelEventSender);
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
        final Vector2D gravityAcc = Vector2D.createCartesian(0, -9.82);
        final Vector2D gravityDeltaVel = gravityAcc.multiply(deltaTime);
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
