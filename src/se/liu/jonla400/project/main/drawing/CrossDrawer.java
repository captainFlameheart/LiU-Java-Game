package se.liu.jonla400.project.main.drawing;

import se.liu.jonla400.project.physics.main.Body;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Draws a cross onto a {@link Graphics2D} object at the origin. The radius
 * can be specified when drawing, which is useful if the radius can change at any time. The
 * radius can also be set if it is constant each time the cross is drawn.
 */
public class CrossDrawer
{
    private Color color;
    private BasicStroke stroke;

    private CrossDrawer(final Color color, final BasicStroke stroke) {
	this.color = color;
	this.stroke = stroke;
    }

    /**
     * Creates a new CrossDrawer with the given {@link Color} and stroke width
     *
     * @param color The color of the cross
     * @param strokeWidth The stroke width of the cross
     * @return The created CrossDrawer
     */
    public static CrossDrawer create(final Color color, final float strokeWidth) {
	return new CrossDrawer(color, new BasicStroke(strokeWidth));
    }

    /**
     * Fixates the radius by returning a {@link Drawer} that can draw the cross with the given radius.
     * When the draw method of the {@link Drawer} is called, it is the same as calling the
     * {@link #draw(Graphics2D, double) draw} method of this CrossDrawer with the given radius.
     *
     * @param radius The radius of the cross
     * @return The {@link Drawer} used to draw the cross with the given radius at (0, 0)
     */
    public Drawer setRadius(final double radius) {
	return g -> draw(g, radius);
    }

    /**
     * Draws the cross onto the {@link Graphics2D} object centered at (0, 0) and with the given radius
     *
     * @param g The graphics object to draw to
     * @param radius The radius of the cross
     */
    public void draw(final Graphics2D g, final double radius) {
	g.setColor(color);
	g.setStroke(stroke);
	g.draw(new Line2D.Double(-radius, 0, radius, 0));
	g.draw(new Line2D.Double(0, -radius, 0, radius));
    }
}
