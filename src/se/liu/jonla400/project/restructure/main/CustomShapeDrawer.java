package se.liu.jonla400.project.restructure.main;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.restructure.physics.fresh.collisions.collisiondetection.CustomShape;
import se.liu.jonla400.project.restructure.physics.fresh.collisions.collisiondetection.LineSegment;

import java.awt.*;
import java.awt.geom.Line2D;

public class CustomShapeDrawer implements Drawer
{
    private CustomShape shape;
    private Color color;
    private BasicStroke stroke;

    public CustomShapeDrawer(final CustomShape shape, final Color color, final float strokeWidth) {
	this.shape = shape;
	this.color = color;
	stroke = new BasicStroke(strokeWidth);
    }

    public CustomShapeDrawer(final CustomShape shape) {
	this(shape, Color.BLACK, 0.05f);
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
