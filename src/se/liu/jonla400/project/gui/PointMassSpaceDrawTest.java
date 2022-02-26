package se.liu.jonla400.project.gui;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.PointMass;
import se.liu.jonla400.project.physics.PointMassSpace;
import se.liu.jonla400.project.math.Interval;

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
	pointMassA.setPos(Vector2D.createCartesianVector(0, 0));
	pointMassA.setVel(Vector2D.createPolarVector(0.4 * Math.PI, 12));
	pointMassA.setAngularVel(0.2 * Math.PI);

	final PointMass pointMassB = new PointMass();
	pointMassB.setPos(Vector2D.createCartesianVector(10, 0));
	pointMassB.setVel(Vector2D.createPolarVector(0.6 * Math.PI, 12));
	pointMassB.setAngularVel(0.2 * Math.PI);

	// Add point masses
	pointMassSpace.addPointMass(pointMassA);
	pointMassSpace.addPointMass(pointMassB);

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

	Timer clockTimer = new Timer(deltaTimeMilliseconds, e -> {
	    for (PointMass pointMass : pointMassSpace) {
		Vector2D vel = pointMass.getVel();
		vel.addLocally(gravityDeltaVelPerTick);
		pointMass.setVel(vel);
	    }
	    pointMassSpace.tick(deltaTimeSeconds);
	    pointMassSpaceDrawer.repaint();
	});
	clockTimer.setCoalesce(false);
	clockTimer.start();
    }
}
