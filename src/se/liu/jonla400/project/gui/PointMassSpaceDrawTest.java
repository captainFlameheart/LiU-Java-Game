package se.liu.jonla400.project.gui;

import se.liu.jonla400.project.math.Interval;
import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.PointMass;
import se.liu.jonla400.project.physics.PointMassSpace;
import se.liu.jonla400.project.physics.collision.CollisionInterruptGenerator;
import se.liu.jonla400.project.physics.collision.collisiondetection.CollisionDetectorQueue;
import se.liu.jonla400.project.physics.collision.collisiondetection.ContinousCollisionDetector;
import se.liu.jonla400.project.physics.collision.collisiondetection.types.CircleVsPlaneCollisionDetector;
import se.liu.jonla400.project.physics.collision.collisiondetection.types.CircleVsPointCollisionDetector;
import se.liu.jonla400.project.physics.collision.collisionlistening.CollisionListener;
import se.liu.jonla400.project.physics.collision.collisionlistening.CollisionSolver;
import se.liu.jonla400.project.physics.constraints.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.constraints.IterativeVelocityConstrainer;
import se.liu.jonla400.project.physics.constraints.types.FrictionApplier;
import se.liu.jonla400.project.physics.constraints.types.Pin;
import se.liu.jonla400.project.timestepping.InterruptableTimeStepper;
import se.liu.jonla400.project.timestepping.TimeStepper;

import javax.swing.*;
import java.awt.*;

/**
 * Animates and draws a point mass space
 */
public class PointMassSpaceDrawTest
{
    public static void main(String[] args) {
	final PointMassSpace pointMassSpace = new PointMassSpace();

	// Set the x-coordinates of the space to be drawn from left to right
	final Interval startToEndX = new Interval(0, 10);
	// Set the y-coordinates of the space to be drawn from top to bottom
	final Interval startToEndY = new Interval(10, 0);

	// Create point masses
	final PointMass pointMassA = new PointMass();
	pointMassA.setPos(Vector2D.createCartesianVector(0, 5));
	pointMassA.setVel(Vector2D.createCartesianVector(6, 0));
	pointMassA.setAngularVel(0);
	pointMassA.setMass(1);
	pointMassA.setAngularMass(0.01);

	final PointMass pointMassB = new PointMass();
	pointMassB.setPos(Vector2D.createCartesianVector(9, 10));
	pointMassB.setVel(Vector2D.createCartesianVector(-10, 0));
	pointMassB.setAngularVel(0);
	pointMassB.setMass(1);
	pointMassB.setAngularMass(0.01);

	// Add point masses
	pointMassSpace.addPointMass(pointMassA);

	// Create constraints
	final double maxFrictionForce = 0;
	final double maxFrictionTorque = 0;
	final FrictionApplier frictionApplier = new FrictionApplier(pointMassA, maxFrictionForce, maxFrictionTorque);

	final Vector2D pinnedPoint = Vector2D.createCartesianVector(0, 0);
	final Vector2D pointPinnedTo = pointMassA.convertLocalPointToGlobalPoint(pinnedPoint);
	final Pin pin = new Pin(pointMassA, pinnedPoint, pointPinnedTo);

	final int velConstraintIterations = 10;
	final IterativeVelocityConstrainer iterativeVelConstrainer = new IterativeVelocityConstrainer(
		velConstraintIterations, frictionApplier
	);

	// Create a frame
	final JFrame frame = new JFrame("Test");
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	// Create and add a drawer of the point mass space
	final PointMassSpaceDrawer pointMassSpaceDrawer = new PointMassSpaceDrawer(pointMassSpace, startToEndX, startToEndY);
	pointMassSpaceDrawer.setPreferredSize(new Dimension(600, 600));
	frame.setContentPane(pointMassSpaceDrawer);

	// Adjust the size of the frame and show it
	frame.pack();
	frame.setVisible(true);

	// Create a timer that advances time forward on the space
	final int tickRate = 90;
	final int deltaTimeMilliseconds = 1000 / tickRate;
	final double deltaTimeSeconds = 1.0 / tickRate;

	final Vector2D gravityAcceleration = Vector2D.createCartesianVector(0, -9.82);
	final Vector2D gravityDeltaVelPerTick = gravityAcceleration.multiply(deltaTimeSeconds);

	final double radius = 0.3;

	final int pointCount = 60;
	final ContinousCollisionDetector[] detectors = new ContinousCollisionDetector[pointCount];
	for (int i = 0; i < pointCount; i++) {
	    double x = new Interval(0, pointCount - 1).mapValueToOtherInterval(i, new Interval(0, 10));
	    double y = 3 - Math.signum(i % 3);//Math.random() * 10;
	    Vector2D point = Vector2D.createCartesianVector(x, y);
	    detectors[i] = new CircleVsPointCollisionDetector(
		    pointMassA, radius, point
	    );
	}

	final ContinousCollisionDetector collisionDetector = new CollisionDetectorQueue(detectors);

	/*
	final ContinousCollisionDetector collisionDetector = new CollisionDetectorQueue(
		new CircleVsPointCollisionDetector(pointMassA, radius, Vector2D.createCartesianVector(5, 5))
	);*/

	final double enforcedSeparation = 0.001;
	final CollisionListener collisionListener = new CollisionSolver(enforcedSeparation);

	final int maxTickIterations = 50;
	final TimeStepper timeStepper = new InterruptableTimeStepper(
		pointMassSpace,
		new CollisionInterruptGenerator(collisionDetector, collisionListener),
		maxTickIterations
	);

	Timer clockTimer = new Timer(deltaTimeMilliseconds, e -> {
	    for (PointMass pointMass : pointMassSpace) {
		final Vector2D vel = pointMass.getVel();
		vel.addLocally(gravityDeltaVelPerTick);
		pointMass.setVel(vel);
	    }

	    ActiveVelocityConstraint activeVelConstraint = iterativeVelConstrainer.initActiveVelConstraint(deltaTimeSeconds);
	    activeVelConstraint.updateSolution();
	    timeStepper.tick(deltaTimeSeconds);
	    pointMassSpaceDrawer.repaint();
	});
	clockTimer.setCoalesce(false);
	clockTimer.start();
    }

}
