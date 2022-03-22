package se.liu.jonla400.project.main.drawing;

import java.awt.*;
import java.awt.geom.Line2D;

public class CrossDrawer implements Drawer
{
    private double radius;
    private Color color;
    private BasicStroke stroke;

    private CrossDrawer(final double radius, final Color color, final BasicStroke stroke) {
	this.radius = radius;
	this.color = color;
	this.stroke = stroke;
    }

    public static CrossDrawer create(final double radius, final Color color, final float strokeWidth) {
	return new CrossDrawer(radius, color, new BasicStroke(strokeWidth));
    }

    public static CrossDrawer createWithDefaultColor(final double radius, final float strokeWidth) {
	return create(radius, Color.BLACK, strokeWidth);
    }

    @Override public void draw(final Graphics2D g) {
	g.setColor(color);
	g.setStroke(stroke);
	g.draw(new Line2D.Double(-radius, 0, radius, 0));
	g.draw(new Line2D.Double(0, -radius, 0, radius));
    }
}