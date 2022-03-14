package se.liu.jonla400.project.main;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.abstraction.main.Body;
import se.liu.jonla400.project.physics.abstraction.main.PhysicsEngine;
import se.liu.jonla400.project.physics.implementation.collision.CircleCollider;
import se.liu.jonla400.project.physics.implementation.collision.CircleVsCustomCollisionDetector;
import se.liu.jonla400.project.physics.implementation.collision.CustomCollider;
import se.liu.jonla400.project.physics.implementation.collision.CustomShape;
import se.liu.jonla400.project.physics.implementation.constraint.AngularVelocitySeeker;
import se.liu.jonla400.project.physics.implementation.constraint.Friction;
import se.liu.jonla400.project.physics.implementation.constraint.Translator;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class Level
{
    private DrawRegion minDrawRegion;
    private PhysicsEngine physicsEngine;
    private BodyDrawerSet bodyDrawers;
    private Body circleBody;
    private Body levelBody;
    private Translator translator;
    private Map<RotationDirection, AngularVelocitySeeker> directionToAngularVelSeeker;
    private Vector2D curserPos;

    private Level(final DrawRegion minDrawRegion, final PhysicsEngine physicsEngine, final BodyDrawerSet bodyDrawers, final Body circleBody,
                  final Body levelBody)
    {
        this.minDrawRegion = minDrawRegion;
        this.physicsEngine = physicsEngine;
        this.bodyDrawers = bodyDrawers;
        this.circleBody = circleBody;
        this.levelBody = levelBody;

        translator = null;
        directionToAngularVelSeeker = new EnumMap<>(RotationDirection.class);

        curserPos = Vector2D.createZeroVector();
    }

    public static Level createFromDefinition(final LevelDefinition definition) {
        final DrawRegion minDrawRegion = definition.getMinDrawRegion();
        final PhysicsEngine physicsEngine = new PhysicsEngine(definition.getVelConstraintIterations());
        final BodyDrawerSet bodyDrawerSet = BodyDrawerSet.create();

        final Body circleBody = Body.createFromDefinition(definition.getCircleBodyDefinition());
        final double circleRadius = definition.getCircleRadius();
        final Body levelBody = Body.createFromDefinition(definition.getLevelBodyDefinition());
        final CustomShape levelShape = CustomShape.createFromDefinition(definition.getLevelShapeDefinition());

        {
            physicsEngine.add(circleBody);
            physicsEngine.add(new Friction(circleBody, 1, 1));
            physicsEngine.add(levelBody);
            physicsEngine.add(new Friction(levelBody, 2000, 300));
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
        final Vector2D gravityAcc = Vector2D.createCartesianVector(0, -9.82 * 2);
        final Vector2D gravityDeltaVel = gravityAcc.multiply(deltaTime);
        circleBody.setVel(circleBody.getVel().add(gravityDeltaVel));
    }

    public void setCursorPos(final Vector2D pos) {
        curserPos.set(pos);
        if (translator != null) {
            translator.setGlobalPointPulledTowards(pos);
        }
    }

    public void startTranslation() {
        if (translator != null) {
            return;
        }



        final double maxForce = 4000;

        translator = Translator.createAtGlobalPoint(levelBody, curserPos, maxForce);
        physicsEngine.add(translator);
    }

    public void endTranslation() {
        if (translator == null) {
            return;
        }
        physicsEngine.remove(translator);
        translator = null;
    }

    public void startRotation(final RotationDirection direction) {
        if (directionToAngularVelSeeker.get(direction) != null) {
            return;
        }

        final double targetAngularSpeed = 3;
        final double maxTorque = 1000;

        final double targetAngularVel = -direction.getSign() * targetAngularSpeed;
        final AngularVelocitySeeker angularVelSeeker = new AngularVelocitySeeker(levelBody, targetAngularVel, maxTorque);
        directionToAngularVelSeeker.put(direction, angularVelSeeker);
        physicsEngine.add(angularVelSeeker);
    }

    public void endRotation(final RotationDirection direction) {
        if (directionToAngularVelSeeker.get(direction) == null) {
            return;
        }
        physicsEngine.remove(directionToAngularVelSeeker.get(direction));
        directionToAngularVelSeeker.remove(direction);
    }

    public DrawRegion getMinDrawRegion() {
	return minDrawRegion;
    }

    public void draw(final Graphics2D g, final DrawRegion region) {
        drawBackground(g, region);
        bodyDrawers.draw(g);

        if (translator != null) {
            final Vector2D grabbedPoint = levelBody.convertLocalPointToGlobalPoint(translator.getPulledLocalPoint());
            g.draw(new Line2D.Double(grabbedPoint.getX(), grabbedPoint.getY(), curserPos.getX(), curserPos.getY()));
        }
    }

    private void drawBackground(final Graphics2D g, final DrawRegion region) {
        g.setColor(Color.WHITE);
        g.fill(new Rectangle2D.Double(region.getLeftX(), region.getBottomY(), region.getWidth(), region.getHeight()));
    }
}
