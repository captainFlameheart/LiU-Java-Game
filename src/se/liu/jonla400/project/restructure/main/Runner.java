package se.liu.jonla400.project.restructure.main;

import se.liu.jonla400.project.restructure.math.Interval;
import se.liu.jonla400.project.restructure.math.Vector2D;
import se.liu.jonla400.project.restructure.physics.abstraction.main.Body;
import se.liu.jonla400.project.restructure.physics.abstraction.main.PhysicsEngine;
import se.liu.jonla400.project.restructure.physics.implementation.constraint.FrictionApplier;
import se.liu.jonla400.project.restructure.physics.implementation.constraint.Pin;
import se.liu.jonla400.project.restructure.physics.abstraction.collision.CollisionHandler;
import se.liu.jonla400.project.restructure.physics.implementation.collision.CircleCollider;
import se.liu.jonla400.project.restructure.physics.implementation.collision.CircleVsCustomCollisionDetector;
import se.liu.jonla400.project.restructure.physics.abstraction.collision.CollisionDetector;
import se.liu.jonla400.project.restructure.physics.implementation.collision.CustomCollider;
import se.liu.jonla400.project.restructure.physics.implementation.collision.CustomShape;
import se.liu.jonla400.project.restructure.physics.implementation.collision.LineSegment;

import javax.swing.*;
import java.awt.*;

public class Runner
{
    public static void main(String[] args) {
        final Interval startToEndX = new Interval(-10, 10);
        final Interval startToEndY = new Interval(10, -10);

        //final BodyTimeStepper bodyTimeStepper = new BodyTimeStepper();
        //final IterativeVelocityConstrainer velConstrainer = new IterativeVelocityConstrainer(10);
        final PhysicsEngine physicsEngine = new PhysicsEngine(10);

        final PhysicsWorldDrawer physicsWorldDrawer = new PhysicsWorldDrawer(startToEndX, startToEndY);


            final Body bodyA = new Body();
            bodyA.setPos(Vector2D.createCartesianVector(0, 0.4));
            bodyA.setVel(Vector2D.createCartesianVector(30, 0));
            bodyA.setAngle(0);
            bodyA.setAngularVel(0);
            bodyA.setMass(1);
            bodyA.setAngularMass(0.01);

            physicsEngine.add(bodyA);//bodyTimeStepper.add(bodyA);
            physicsWorldDrawer.add(new BodyDrawer(bodyA, new CrossDrawer()));

            final Body bodyB = new Body();
            bodyB.setPos(Vector2D.createCartesianVector(0, 2));
            bodyB.setVel(Vector2D.createCartesianVector(0, 0));
            bodyB.setAngle(0);
            bodyB.setAngularVel(0);
            bodyB.setMass(10000);
            bodyB.setAngularMass(10000);

            physicsEngine.add(bodyB);
            physicsEngine.add(new FrictionApplier(bodyB, 100, 100));
            physicsWorldDrawer.add(new BodyDrawer(bodyB, new CrossDrawer()));

            final Body bodyC = new Body();
            bodyC.setPos(Vector2D.createCartesianVector(0, 0));
            bodyC.setVel(Vector2D.createCartesianVector(0, 0));
            bodyC.setAngle(0);
            bodyC.setAngularVel(0);
            bodyC.setMass(1);
            bodyC.setAngularMass(1);

            final double radius = 0.4;
            final CircleCollider circleCollider = new CircleCollider(bodyA, radius);


            physicsEngine.add(bodyC);
            //velConstrainer.add(new FrictionApplier(bodyB, 100, 1000));
            physicsWorldDrawer.add(new BodyDrawer(bodyC, new CrossDrawer()));
            final CustomShape customShape2 = new CustomShape(
                    new LineSegment(Vector2D.createCartesianVector(-1, 1), Vector2D.createCartesianVector(1, 1)),
                    new LineSegment(Vector2D.createCartesianVector(1, 1), Vector2D.createCartesianVector(1, -1)),
                    new LineSegment(Vector2D.createCartesianVector(-1, -1), Vector2D.createCartesianVector(1, -1)),
                    new LineSegment(Vector2D.createCartesianVector(-1, -1), Vector2D.createCartesianVector(-1, 1))
            );
            physicsEngine.add(new Pin(bodyC, Vector2D.createCartesianVector(0, 5), Vector2D.createCartesianVector(0, 5)));
            final CustomCollider customCollider2 = new CustomCollider(bodyC, customShape2);
            final CollisionDetector collisionDetector2 = new CircleVsCustomCollisionDetector(circleCollider, customCollider2);
            physicsEngine.add(new CollisionHandler(collisionDetector2));
            physicsWorldDrawer.add(new BodyDrawer(bodyC, new CustomShapeDrawer(customShape2)));

            final CustomShape customShape = new CustomShape(
                    new LineSegment(Vector2D.createCartesianVector(-10, 0), Vector2D.createCartesianVector(10, 0)),
                    new LineSegment(Vector2D.createCartesianVector(2, 0), Vector2D.createCartesianVector(6, 1)),
                    new LineSegment(Vector2D.createCartesianVector(2, 5), Vector2D.createCartesianVector(6, 5))
            );

            final CustomCollider customCollider = new CustomCollider(bodyB, customShape);
            final CollisionDetector collisionDetector = new CircleVsCustomCollisionDetector(circleCollider, customCollider);
            physicsEngine.add(new CollisionHandler(collisionDetector));

            physicsWorldDrawer.add(new BodyDrawer(bodyA, new CircleDrawer(radius)));
            physicsWorldDrawer.add(new BodyDrawer(bodyB, new CustomShapeDrawer(customShape)));


        // Create a frame
        final JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(physicsWorldDrawer);
        physicsWorldDrawer.setPreferredSize(new Dimension(600, 600));
        frame.pack();
        frame.setVisible(true);

        // Create a timer that advances time forward on the space
        final int tickRate = 90;
        final int deltaMilliseconds = 1000 / tickRate;
        final double deltaSeconds = 1.0 / tickRate;

        Timer clockTimer = new Timer(deltaMilliseconds, e -> {
            bodyA.setVel(bodyA.getVel().add(Vector2D.createCartesianVector(0, -9.82).multiply(deltaSeconds)));
            physicsEngine.tick(deltaSeconds);
            physicsWorldDrawer.repaint();
        });
        clockTimer.setCoalesce(false);
        clockTimer.start();
    }
}
