package se.liu.jonla400.project.main;

import se.liu.jonla400.project.math.Interval;
import se.liu.jonla400.project.physics.abstraction.main.Body;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BodyGroupDrawer extends JComponent
{
    private Interval startToEndX;
    private Interval startToEndY;
    private List<BodyDrawer> bodyDrawers;

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
    public BodyGroupDrawer(final Interval startToEndX, final Interval startToEndY, final BodyDrawer... bodyDrawers) {
	this.startToEndX = startToEndX;
	this.startToEndY = startToEndY;
	this.bodyDrawers = new ArrayList<>(Arrays.asList(bodyDrawers));
    }

    public void add(final Body body, final Drawer shapeDrawer) {
	bodyDrawers.add(new BodyDrawer(body, shapeDrawer));
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
	drawBodies(g2d);
    }

    private void drawBackground(final Graphics2D g) {
	g.setColor(Color.WHITE);
	g.fillRect(0, 0, getWidth(), getHeight());
    }

    private void drawBodies(final Graphics2D g) {
	// Save the current transform so that it can be reset
	AffineTransform oldTransform = g.getTransform();
	// Transform the graphics object so that drawing the point masses onto
	// the graphics object at their positions will display them to the screen
	// at the correct positions according to the start and end x- and y-coordinates
	transformGraphicsToShowRegion(g);
	// Draw each point mass onto the graphics object at their positions
	for (BodyDrawer bodyDrawer : bodyDrawers) {
	    bodyDrawer.draw(g);
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
