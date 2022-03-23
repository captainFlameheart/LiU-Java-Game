package se.liu.jonla400.project.main.drawing;

import java.awt.*;
import java.awt.geom.Ellipse2D;

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

    public static CircleDrawer create(final Color fillColor, final Color strokeColor, final float strokeWidth) {
	return new CircleDrawer(fillColor, strokeColor, new BasicStroke(strokeWidth));
    }

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
