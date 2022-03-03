package se.liu.jonla400.project.gui;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.PointMass;
import se.liu.jonla400.project.physics.PointMassSpace;
import se.liu.jonla400.project.math.Interval;
import se.liu.jonla400.project.physics.constraints.ActiveVelocityConstraint;
import se.liu.jonla400.project.physics.constraints.IterativeVelocityConstrainer;
import se.liu.jonla400.project.physics.constraints.types.AngularFrictionApplier;
import se.liu.jonla400.project.physics.constraints.types.FrictionApplier;
import se.liu.jonla400.project.physics.constraints.types.Pin;
import se.liu.jonla400.project.physics.constraints.types.TranslationalFrictionApplier;

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
	pointMassA.setPos(Vector2D.createCartesianVector(1, 5));
	pointMassA.setVel(Vector2D.createCartesianVector(5, 0));
	pointMassA.setAngularVel(5);

	// Add point masses
	pointMassSpace.addPointMass(pointMassA);

	// Create constraints
	final double maxFrictionForce = 3;
	final double maxFrictionTorque = 3;
	final FrictionApplier frictionApplier = new FrictionApplier(pointMassA, maxFrictionForce, maxFrictionTorque);

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

	final Vector2D gravityAcceleration = Vector2D.createCartesianVector(0, 0);
	final Vector2D gravityDeltaVelPerTick = gravityAcceleration.multiply(deltaTimeSeconds);

	Timer clockTimer = new Timer(deltaTimeMilliseconds, e -> {
	    for (PointMass pointMass : pointMassSpace) {
		Vector2D vel = pointMass.getVel();
		vel.addLocally(gravityDeltaVelPerTick);
		pointMass.setVel(vel);
	    }

	    ActiveVelocityConstraint activeVelConstraint = iterativeVelConstrainer.initActiveVelConstraint(deltaTimeSeconds);
	    activeVelConstraint.updateSolution();
	    pointMassSpace.tick(deltaTimeSeconds);
	    pointMassSpaceDrawer.repaint();
	});
	clockTimer.setCoalesce(false);
	clockTimer.start();
    }
}
