package se.liu.jonla400.project.main;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class CircleDrawer implements Drawer
{
    private double radius;
    private Color fillColor;
    private Color strokeColor;
    private BasicStroke stroke;

    private CircleDrawer(final double radius, final Color fillColor, final Color strokeColor, final BasicStroke stroke) {
        this.radius = radius;
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.stroke = stroke;
    }

    public static CircleDrawer createWithDefaultColors(final double radius, final float strokeWidth) {
        return new CircleDrawer(radius, Color.RED, Color.BLACK, new BasicStroke(strokeWidth));
    }

    @Override public void draw(final Graphics2D g) {
        final double diameter = 2 * radius;
        final Shape shape = new Ellipse2D.Double(-radius, -radius, diameter, diameter);
        g.setColor(fillColor);
        g.fill(shape);
        g.setColor(strokeColor);
        g.setStroke(stroke);
        g.draw(shape);
    }
}
