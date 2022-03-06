package se.liu.jonla400.project.gui;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.PointMass;
import se.liu.jonla400.project.physics.PointMassSpace;
import se.liu.jonla400.project.math.Interval;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Represents a JComponent that draws a certain rectangular region of a PointMassSpace.
 *
 * The content of the region (i.e. the point masses within that region) can be read from
 * left to right or from right to left. The read content will then be displayed
 * graphically from left to right in the order it was read.
 *
 * This means that if the content of the region is read from right to left,
 * point masses with greater x-coordinates will be drawn to the left of those with lesser
 * x-coordinates.
 *
 * Similarly the content of the region can be read from bottom to top or from top to bottom.
 * The read content will be displayed graphically from top to bottom in the order it was read.
 */
public class PointMassSpaceDrawer extends JComponent
{
    private PointMassSpace pointMassSpace;
    private Interval startToEndX;
    private Interval startToEndY;

    /**
     * Creates a drawer for the given PointMassSpace.
     *
     * The intervals represents where to start and where to stop drawing the space.
     * The start x-coordinate will be mapped to the left of the screen and the end
     * x-coordinate will be mapped to the right of the screen. Similarly, the start
     * y-coordinate will be mapped to the top of the screen and the end y-coordinate
     * will be mapped to the bottom of the screen.
     *
     * @param pointMassSpace The point mass space to draw
     * @param startToEndX The start and end x-coordinates to draw, from left to right
     * @param startToEndY The start and end y-coordinates to draw, from top to bottom
     */
    public PointMassSpaceDrawer(final PointMassSpace pointMassSpace, final Interval startToEndX, final Interval startToEndY) {
	this.pointMassSpace = pointMassSpace;
	this.startToEndX = startToEndX;
	this.startToEndY = startToEndY;
    }

    /**
     * Draws the specified region of the point mass space
     *
     * @param g The graphics onto which to draw the point mass space
     */
    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);

	final Graphics2D g2d = (Graphics2D) g;
	drawBackground(g2d);
	drawPointMasses(g2d);
    }

    private void drawBackground(final Graphics2D g) {
	g.setColor(Color.WHITE);
	g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawPointMasses(final Graphics2D g) {
	// Save the current transform so that it can be reset
	AffineTransform oldTransform = g.getTransform();
	// Transform the graphics object so that drawing the point masses onto
	// the graphics object at their positions will display them to the screen
	// at the correct positions according to the start and end x- and y-coordinates
	transformGraphicsToShowRegion(g);
	// Draw each point mass onto the graphics object at their positions
	for (PointMass pointMass : pointMassSpace) {
	    drawPointMass(pointMass, g);
	}

	// Reset the saved transform
	g.setTransform(oldTransform);
    }

    private void transformGraphicsToShowRegion(final Graphics2D g) {
	// Get the scale and translation for both the x- and the y-axis
	final Transform1D xTransform = get1DTransformToShowRegion(getWidth(), startToEndX);
	final Transform1D yTransform = get1DTransformToShowRegion(getHeight(), startToEndY);

	// Scale and translate the graphics object according to the received transforms
	g.scale(xTransform.scale, yTransform.scale);
	g.translate(xTransform.translation, yTransform.translation);
    }

    private Transform1D get1DTransformToShowRegion(final double screenDimension, final Interval startToEndCoordinate) {
	// This is a helper method. See its usage to understand more.

	// Get the displacement of the end coordinate from the start coordinate (can be negative)
	final double startToEndDisplacement = startToEndCoordinate.getStartToEndDisplacement();
	// Set the scale so that distance between the start and end coordinates becomes the screen dimension.
	// If the displacement from the start to the end coordinate is negative, the scale is negative.
	final double scale = screenDimension / startToEndDisplacement;
	// Set the translation so that the start coordinate ends up at the current origin
	final double translation = -startToEndCoordinate.getStart();
	// Return both the scale and the translation
	return new Transform1D(scale, translation);
    }

    private void drawPointMass(final PointMass pointMass, final Graphics2D g) {
	// Save the current transform so that it can be reset
	AffineTransform oldTransform = g.getTransform();

	// Get the information needed to draw the point mass
	final Vector2D pos = pointMass.getPos();
	final double angle = pointMass.getAngle();
	final double lineLength = 0.3;

	// Place the origin at the position of the point mass
	g.translate(pos.getX(), pos.getY());
	// Rotate the graphics to the angle of the point mass
	g.rotate(angle);
	// Scale the graphics to the length of the lines to be drawn
	g.scale(lineLength, lineLength);

	g.setStroke(new BasicStroke(0.1f));
	// Draw two lines, one for the local x-
	g.setColor(Color.RED);
	g.drawLine(0, 0, 1, 0);
	// and the other for the local y-axis of the point mass
	g.setColor(Color.BLUE);
	g.drawLine(0, 0, 0, 1);
	g.drawOval(-1, -1, 2, 2);

	// Reset the saved transform
	g.setTransform(oldTransform);
    }

    /**
     * Represents a 1D transform that contains a scale and a translation
     */
    private static class Transform1D {
	private double scale;
	private double translation;

	private Transform1D(final double scale, final double translation) {
	    this.scale = scale;
	    this.translation = translation;
	}
    }

}
