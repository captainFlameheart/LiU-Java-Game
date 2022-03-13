package se.liu.jonla400.project.main;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.abstraction.main.Body;
import se.liu.jonla400.project.physics.abstraction.main.PhysicsEngine;
import se.liu.jonla400.project.physics.implementation.collision.CircleCollider;
import se.liu.jonla400.project.physics.implementation.collision.CircleVsCustomCollisionDetector;
import se.liu.jonla400.project.physics.implementation.collision.CustomCollider;
import se.liu.jonla400.project.physics.implementation.collision.CustomShape;
import se.liu.jonla400.project.physics.implementation.collision.LineSegment;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Level
{
    private DrawRegion minDrawRegion;
    private PhysicsEngine physicsEngine;
    private BodyDrawerSet bodyDrawers;
    private Body circleBody;
    private Body levelBody;

    private Level(final DrawRegion minDrawRegion, final PhysicsEngine physicsEngine, final BodyDrawerSet bodyDrawers, final Body circleBody,
                  final Body levelBody)
    {
        this.minDrawRegion = minDrawRegion;
        this.physicsEngine = physicsEngine;
        this.bodyDrawers = bodyDrawers;
        this.circleBody = circleBody;
        this.levelBody = levelBody;
    }

    public static Level createFromDefinition(final LevelDefinition definition) {
        final DrawRegion minDrawRegion = definition.getMinDrawRegion();
        final PhysicsEngine physicsEngine = new PhysicsEngine(definition.getVelConstraintIterations());
        final BodyDrawerSet bodyDrawerSet = BodyDrawerSet.create();

        final Body circleBody = Body.createFromDefinition(definition.getCircleBodyDefinition());
        final double circleRadius = definition.getCircleRadius();
        final Body levelBody = Body.createFromDefinition(definition.getLevelBodyDefinition());
        levelBody.setAngle(-0.3);
        final CustomShape levelShape = CustomShape.createFromDefinition(definition.getLevelShapeDefinition());

        {
            physicsEngine.add(circleBody);
            physicsEngine.add(levelBody);
            final CircleCollider circleCollider = new CircleCollider(circleBody, circleRadius);
            final CustomCollider levelCollider = new CustomCollider(levelBody, levelShape);
            physicsEngine.add(new CircleVsCustomCollisionDetector(circleCollider, levelCollider));
        }

        {
            final CrossDrawer crossDrawer = CrossDrawer.createWithDefaultColor(0.1, 0.07f);
            final CircleDrawer circleDrawer = CircleDrawer.createWithDefaultColors(circleRadius, 0.03f);
            bodyDrawerSet.add(circleBody, circleDrawer, crossDrawer);
            final CustomShapeDrawer levelDrawer = CustomShapeDrawer.createWithDefaultColor(levelShape, 0.03f);
            bodyDrawerSet.add(levelBody, levelDrawer, crossDrawer);
        }

        return new Level(minDrawRegion, physicsEngine, bodyDrawerSet, circleBody, levelBody);
    }

    public LevelTimeStepResult tick(final double deltaTime) {
        applyGravityToCircle(deltaTime);
        physicsEngine.tick(deltaTime);
        return new LevelTimeStepResult(false);
    }

    private void applyGravityToCircle(final double deltaTime) {
        final Vector2D gravityAcc = Vector2D.createCartesianVector(0, -9.82);
        final Vector2D gravityDeltaVel = gravityAcc.multiply(deltaTime);
        circleBody.setVel(circleBody.getVel().add(gravityDeltaVel));
    }

    public void setCursorPos(final Vector2D pos) {

    }

    public void startTranslating() {

    }

    public void stopTranslating() {

    }

    public void startRotating() {

    }

    public void stopRotating() {

    }

    public DrawRegion getMinDrawRegion() {
	return minDrawRegion;
    }

    public void draw(final Graphics2D g, final DrawRegion region) {
        drawBackground(g, region);
        bodyDrawers.draw(g);
    }

    private void drawBackground(final Graphics2D g, final DrawRegion region) {
        g.setColor(Color.WHITE);
        g.fill(new Rectangle2D.Double(region.getLeftX(), region.getBottomY(), region.getWidth(), region.getHeight()));
    }
}
