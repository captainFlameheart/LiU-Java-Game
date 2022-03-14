package se.liu.jonla400.restructure.main.drawing;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.implementation.collision.CustomShape;
import se.liu.jonla400.project.physics.implementation.collision.LineSegment;

import java.awt.*;
import java.awt.geom.Line2D;

public class CustomShapeDrawer implements Drawer
{
    private CustomShape shape;
    private Color color;
    private BasicStroke stroke;

    private CustomShapeDrawer(final CustomShape shape, final Color color, final BasicStroke stroke) {
	this.shape = shape;
	this.color = color;
	this.stroke = stroke;
    }

    public static CustomShapeDrawer createWithDefaultColor(final CustomShape shape, final float strokeWidth) {
	return new CustomShapeDrawer(shape, Color.BLACK, new BasicStroke(strokeWidth));
    }

    @Override public void draw(final Graphics2D g) {
	g.setColor(color);
	g.setStroke(stroke);
	for (LineSegment lineSegment : shape) {
	    final Vector2D start = lineSegment.getStart();
	    final Vector2D end = lineSegment.getEnd();
	    g.draw(new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY()));
	}
    }
}
