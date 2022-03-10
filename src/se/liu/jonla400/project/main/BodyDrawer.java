package se.liu.jonla400.project.main;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.abstraction.main.Body;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Draws a body onto a graphics object. The drawing procedure relative to the body's
 * position and angle is specified by a {@link Drawer}
 */
public class BodyDrawer implements Drawer
{
    private Body body;
    private Drawer drawer;

    /**
     * Creates a new drawer of the given body
     *
     * @param body The body to draw
     * @param drawer How to draw the body relative to the body's position and angle
     */
    public BodyDrawer(final Body body, final Drawer drawer) {
	this.body = body;
	this.drawer = drawer;
    }

    /**
     * Draws the body onto the graphics object according to the body's position and angle
     *
     * @param g The graphics to draw onto
     */
    @Override public void draw(final Graphics2D g) {
	final AffineTransform oldTransform = g.getTransform();
	// Transform the graphics according to the body's position and angle
	final Vector2D pos = body.getPos();
	g.translate(pos.getX(), pos.getY());
	g.rotate(body.getAngle());

	drawer.draw(g);	// Delegate to the drawer

	g.setTransform(oldTransform);
    }
}
