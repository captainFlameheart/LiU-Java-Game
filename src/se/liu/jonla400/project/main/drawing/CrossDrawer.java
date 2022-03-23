package se.liu.jonla400.project.main.drawing;

import java.awt.*;
import java.awt.geom.Line2D;

public class CrossDrawer
{
    private Color color;
    private BasicStroke stroke;

    private CrossDrawer(final Color color, final BasicStroke stroke) {
	this.color = color;
	this.stroke = stroke;
    }

    public static CrossDrawer create(final Color color, final float strokeWidth) {
	return new CrossDrawer(color, new BasicStroke(strokeWidth));
    }

    public Drawer setRadius(final double radius) {
	return g -> draw(g, radius);
    }

    public void draw(final Graphics2D g, final double radius) {
	g.setColor(color);
	g.setStroke(stroke);
	g.draw(new Line2D.Double(-radius, 0, radius, 0));
	g.draw(new Line2D.Double(0, -radius, 0, radius));
    }
}
