package se.liu.jonla400.project.main;

import se.liu.jonla400.project.math.Interval;
import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.abstraction.main.Body;
import se.liu.jonla400.project.physics.abstraction.main.PhysicsEngine;
import se.liu.jonla400.project.physics.implementation.collision.CircleCollider;
import se.liu.jonla400.project.physics.implementation.collision.CircleVsCustomCollisionDetector;
import se.liu.jonla400.project.physics.implementation.collision.CustomCollider;
import se.liu.jonla400.project.physics.implementation.collision.CustomShape;
import se.liu.jonla400.project.physics.implementation.collision.LineSegment;

import javax.swing.*;
import java.awt.*;

public class Runner
{
    public static void main(String[] args) {
        final PhysicsEngine physicsEngine = new PhysicsEngine(10);

        final Interval leftToRightX = new Interval(-10, 10);
        final Interval topToBottomY = new Interval(10, -10);
        final BodyGroupDrawer bodyGroupDrawer = new BodyGroupDrawer(leftToRightX, topToBottomY);

        final Body bodyA = new Body();
        bodyA.setPos(Vector2D.createCartesianVector(0, 0));
        bodyA.setVel(Vector2D.createCartesianVector(0, 0));
        bodyA.setAngle(0);
        bodyA.setAngularVel(10);
        bodyA.setMass(1);
        bodyA.setAngularMass(1);

        physicsEngine.add(bodyA);
        bodyGroupDrawer.add(bodyA, new CrossDrawer());

        final Body bodyB = new Body();
        bodyB.setPos(Vector2D.createCartesianVector(0, -2));
        bodyB.setVel(Vector2D.createCartesianVector(0, 0));
        bodyB.setAngle(0);
        bodyB.setAngularVel(0);
        bodyB.setMass(1000);
        bodyB.setAngularMass(1000);

        physicsEngine.add(bodyB);
        bodyGroupDrawer.add(bodyB, new CrossDrawer());

        final double radius = 1;
        final CustomShape customShape = new CustomShape(createSineWave(new Interval(-10, 10), 0, 1, 1, 100));

        final CircleCollider circleCollider = new CircleCollider(bodyA, radius);
        final CustomCollider customCollider = new CustomCollider(bodyB, customShape);
        physicsEngine.add(new CircleVsCustomCollisionDetector(circleCollider, customCollider));

        bodyGroupDrawer.add(bodyA, new CircleDrawer(radius));
        bodyGroupDrawer.add(bodyB, new CustomShapeDrawer(customShape));

        // Create a frame
        final JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(bodyGroupDrawer);
        bodyGroupDrawer.setPreferredSize(new Dimension(600, 600));
        frame.pack();
        frame.setVisible(true);

        // Create a timer that advances time forward on the space
        final int tickRate = 90;
        final int deltaMilliseconds = 1000 / tickRate;
        final double deltaSeconds = 1.0 / tickRate;

        Timer clockTimer = new Timer(deltaMilliseconds, e -> {
            bodyA.setVel(bodyA.getVel().add(Vector2D.createCartesianVector(0, -9.82).multiply(deltaSeconds)));
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
}
