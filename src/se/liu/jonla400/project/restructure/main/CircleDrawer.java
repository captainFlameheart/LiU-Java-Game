package se.liu.jonla400.project.restructure.main;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class CircleDrawer implements Drawer
{
    private double radius;
    private Color color;
    private BasicStroke stroke;

    public CircleDrawer(final double radius, final Color color, final float strokeWidth) {
	this.radius = radius;
	this.color = color;
	this.stroke = new BasicStroke(strokeWidth);
    }

    public CircleDrawer(final double radius) {
	this(radius, Color.BLACK, 0.03f);
    }

    @Override public void draw(final Graphics2D g) {
	g.setColor(color);
	g.setStroke(stroke);
	final double diameter = 2 * radius;
	g.draw(new Ellipse2D.Double(-radius, -radius, diameter, diameter));
    }
}
