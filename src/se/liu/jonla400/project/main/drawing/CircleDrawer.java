package se.liu.jonla400.project.main.drawing;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Draws a circle onto a {@link Graphics2D} object at the origin. The radius is specified
 * when drawing, which is useful when the radius is not constant.
 */
public class CircleDrawer
{
    private Color fillColor;
    private Color strokeColor;
    private BasicStroke stroke;

    private CircleDrawer(final Color fillColor, final Color strokeColor, final BasicStroke stroke) {
	this.fillColor = fillColor;
	this.strokeColor = strokeColor;
	this.stroke = stroke;
    }

    /**
     * Creates a new CircleDrawer with the given colors and stroke width
     *
     * @param fillColor The color used to fill the circle
     * @param strokeColor The color used to outline the circle
     * @param strokeWidth The width of the outline
     * @return The created CircleDrawer
     */
    public static CircleDrawer create(final Color fillColor, final Color strokeColor, final float strokeWidth) {
	return new CircleDrawer(fillColor, strokeColor, new BasicStroke(strokeWidth));
    }

    /**
     * Draws the circle with the given radius onto the {@link Graphics2D} object centered at (0, 0)
     *
     * @param g The graphics object to draw to
     * @param radius The radius of the circle
     */
    public void draw(final Graphics2D g, final double radius) {
	final double diameter = 2 * radius;
	final Shape circle = new Ellipse2D.Double(-radius, -radius, diameter, diameter);
	g.setColor(fillColor);
	g.fill(circle);
	g.setColor(strokeColor);
	g.setStroke(stroke);
	g.draw(circle);
    }
}
