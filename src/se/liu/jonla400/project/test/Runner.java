package se.liu.jonla400.project.test;

import se.liu.jonla400.project.math.Interval;
import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.abstraction.main.Body;
import se.liu.jonla400.project.physics.abstraction.main.PhysicsEngine;
import se.liu.jonla400.project.physics.implementation.collision.CircleCollider;
import se.liu.jonla400.project.physics.implementation.collision.CircleVsCustomCollisionDetector;
import se.liu.jonla400.project.physics.implementation.collision.CustomCollider;
import se.liu.jonla400.project.physics.implementation.collision.CustomShape;
import se.liu.jonla400.project.physics.implementation.collision.LineSegment;
import se.liu.jonla400.project.physics.implementation.constraint.Friction;
import se.liu.jonla400.project.physics.implementation.constraint.Translator;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;

public class Runner
{
    private static double FORCE = 40000;
    private static double FRICTION = 35000;

    public static void main(String[] args) {
        final PhysicsEngine physicsEngine = new PhysicsEngine(10);

        final Interval leftToRightX = new Interval(-20, 20);
        final Interval topToBottomY = new Interval(10, -10);
        final BodyGroupDrawer bodyGroupDrawer = new BodyGroupDrawer(leftToRightX, topToBottomY);
        final CrossDrawer defaultCrossDrawer = new CrossDrawer(0.1, Color.BLACK, 0.03f);

        final Body bodyA = new Body();
        bodyA.setPos(Vector2D.createCartesianVector(0, -9));
        bodyA.setVel(Vector2D.createCartesianVector(0, 0));
        bodyA.setAngle(0);
        bodyA.setAngularVel(0);
        bodyA.setMass(100);
        bodyA.setAngularMass(1000);

        physicsEngine.add(bodyA);
        physicsEngine.add(new Friction(bodyA, FRICTION, 10000));
        bodyGroupDrawer.add(bodyA, defaultCrossDrawer);

        final Body bodyB = new Body();
        bodyB.setPos(Vector2D.createCartesianVector(0, 0));
        bodyB.setVel(Vector2D.createCartesianVector(0, 0));
        bodyB.setAngle(0);
        bodyB.setAngularVel(0);
        bodyB.setMass(1);
        bodyB.setAngularMass(1);

        physicsEngine.add(bodyB);
        bodyGroupDrawer.add(bodyB, defaultCrossDrawer);

        final CustomShape customShape = new CustomShape(
                new LineSegment(Vector2D.createCartesianVector(-1, 0), Vector2D.createCartesianVector(1, 0)),
                new LineSegment(Vector2D.createCartesianVector(2, 10), Vector2D.createCartesianVector(2, 15)),
                new LineSegment(Vector2D.createCartesianVector(-10, 10), Vector2D.createCartesianVector(-10, 11))
        );
        final double radius = 1;

        final CustomCollider customCollider = new CustomCollider(bodyA, customShape);
        final CircleCollider circleCollider = new CircleCollider(bodyB, radius);
        physicsEngine.add(new CircleVsCustomCollisionDetector(circleCollider, customCollider));
        bodyGroupDrawer.add(bodyA, new CustomShapeDrawer(customShape));
        bodyGroupDrawer.add(bodyB, new CircleDrawer(radius));

        // Create a frame
        final JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(bodyGroupDrawer);
        bodyGroupDrawer.setPreferredSize(new Dimension(1200, 600));
        frame.pack();
        frame.setVisible(true);

        // Listen for mouse events
        final MouseHandler mouseHandler = new MouseHandler(physicsEngine, leftToRightX, topToBottomY, bodyA);
        bodyGroupDrawer.addMouseMotionListener(mouseHandler);
        bodyGroupDrawer.addMouseListener(mouseHandler);

        // Create a timer that advances time forward on the space
        final int tickRate = 90;
        final int deltaMilliseconds = 1000 / tickRate;
        final double deltaSeconds = 1.0 / tickRate;

        Timer clockTimer = new Timer(deltaMilliseconds, e -> {
            bodyB.setVel(bodyB.getVel().add(Vector2D.createCartesianVector(0, -9.82).multiply(deltaSeconds)));
            physicsEngine.tick(deltaSeconds);
            bodyGroupDrawer.repaint();
        });
        clockTimer.setCoalesce(false);
        clockTimer.start();
    }

    private static LineSegment[] createSineWave(final Interval xInterval, final double y, final double angularFrequency,
                                                final double amplitude, final int count)
    {
        LineSegment[] lineSegments = new LineSegment[count];
        final double xStepSize = xInterval.getStartToEndDisplacement() / count;

        double prevX = xInterval.getStart();
        double prevY = y + amplitude * Math.sin(angularFrequency * prevX);
        for (int i = 1; i <= count; i++) {
            final double newX = xInterval.getStart() + i * xStepSize;
            final double newY = y + amplitude * Math.sin(angularFrequency * newX);

            lineSegments[i - 1] = new LineSegment(Vector2D.createCartesianVector(prevX, prevY), Vector2D.createCartesianVector(newX, newY));

            prevX = newX;
            prevY = newY;
        }
        return lineSegments;
    }

    private static class MouseHandler extends MouseInputAdapter {
        private PhysicsEngine physicsEngine;
        private Interval leftToRightX;
        private Interval topToBottomY;
        private Body controlledBody;
        private Translator translator;

        private MouseHandler(final PhysicsEngine physicsEngine, final Interval leftToRightX, final Interval topToBottomY,
                            final Body controlledBody)
        {
            this.physicsEngine = physicsEngine;
            this.leftToRightX = leftToRightX;
            this.topToBottomY = topToBottomY;
            this.controlledBody = controlledBody;

            translator = null;
        }

        @Override public void mousePressed(final MouseEvent e) {
            if (translator != null) {
                physicsEngine.remove(translator);
            }

            translator = Translator.createAtGlobalPoint(controlledBody, getMousePos(e), FORCE);
            physicsEngine.add(translator);
        }

        @Override public void mouseReleased(final MouseEvent e) {
            if (translator != null) {
                physicsEngine.remove(translator);
                translator = null;
            }
        }

        @Override public void mouseDragged(final MouseEvent e) {
            translator.setGlobalPointPulledTowards(getMousePos(e));
        }

        private Vector2D getMousePos(final MouseEvent e) {
            final Component component = e.getComponent();
            return convertComponentPointToPhysicsPoint(e.getX(), e.getY(), component.getWidth(), component.getHeight());
        }

        private Vector2D convertComponentPointToPhysicsPoint(final int x, final int y, final int componentWidth, final int componentHeight) {
            final Interval componentXInterval = new Interval(0, componentWidth);
            final Interval componentYInterval = new Interval(0, componentHeight);

            return Vector2D.createCartesianVector(
                    componentXInterval.mapValueToOtherInterval(x, leftToRightX),
                    componentYInterval.mapValueToOtherInterval(y, topToBottomY)
            );
        }
    }

    private static class Rotator {
        private Body body;
        private Vector2D localPoint;

        private Vector2D prevGlobalPoint;

        private Rotator(final Body body, final Vector2D localPoint) {
            this.body = body;
            this.localPoint = localPoint;

            prevGlobalPoint = body.convertLocalPointToGlobalPoint(localPoint);
        }

        private void moveGlobalPoint(final Vector2D newGlobalPoint) {

        }
    }
}
