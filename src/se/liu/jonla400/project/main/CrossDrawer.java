package se.liu.jonla400.project.main;

import java.awt.*;
import java.awt.geom.Line2D;

public class CrossDrawer implements Drawer
{
    private double radius;
    private Color color;
    private BasicStroke stroke;

    public CrossDrawer(final double radius, final Color color, final float strokeWidth) {
	this.radius = radius;
	this.color = color;
	stroke = new BasicStroke(strokeWidth);
    }

    public CrossDrawer(final double radius) {
	this(radius, Color.BLACK, 0.03f);
    }

    public CrossDrawer() {
	this(0.1);
    }

    @Override public void draw(final Graphics2D g) {
	g.setColor(color);
	g.setStroke(stroke);
	g.draw(new Line2D.Double(-radius, 0, radius, 0));
	g.draw(new Line2D.Double(0, -radius, 0, radius));
    }
}
