package se.liu.jonla400.restructure.main.drawing;

import se.liu.jonla400.restructure.math.Vector2D;
import se.liu.jonla400.restructure.physics.implementation.collision.LineSegment;
import se.liu.jonla400.restructure.physics.implementation.collision.TranslatedCustomShape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

public class CustomShapeDrawer implements Drawer
{
    private TranslatedCustomShape translatedShape;
    private Color color;
    private BasicStroke stroke;

    private CustomShapeDrawer(final TranslatedCustomShape translatedShape, final Color color, final BasicStroke stroke) {
	this.translatedShape = translatedShape;
	this.color = color;
	this.stroke = stroke;
    }

    public static CustomShapeDrawer createWithDefaultColor(final TranslatedCustomShape translatedShape, final float strokeWidth) {
	return new CustomShapeDrawer(translatedShape, Color.BLACK, new BasicStroke(strokeWidth));
    }

    @Override public void draw(final Graphics2D g) {
	final AffineTransform oldTransform = g.getTransform();

	final Vector2D translation = translatedShape.getTranslation();
	g.translate(translation.getX(), translation.getY());

	g.setColor(color);
	g.setStroke(stroke);
	for (LineSegment lineSegment : translatedShape.getShape()) {
	    final Vector2D start = lineSegment.getStart();
	    final Vector2D end = lineSegment.getEnd();
	    g.draw(new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY()));
	}

	g.setTransform(oldTransform);
    }
}
