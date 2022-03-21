package se.liu.jonla400.project.main.drawing;

import se.liu.jonla400.project.constants.Constants;
import se.liu.jonla400.project.main.leveldefinition.LineSegmentType;
import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.implementation.collision.LineSegment;
import se.liu.jonla400.project.physics.implementation.collision.TranslatedCustomShape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

public class CustomShapeDrawer implements Drawer
{
    private TranslatedCustomShape<LineSegmentType> translatedShape;

    public CustomShapeDrawer(final TranslatedCustomShape<LineSegmentType> translatedShape) {
	this.translatedShape = translatedShape;
    }

    @Override public void draw(final Graphics2D g) {
	final AffineTransform oldTransform = g.getTransform();

	final Vector2D translation = translatedShape.getTranslation();
	g.translate(translation.getX(), translation.getY());

	g.setStroke(new BasicStroke(Constants.getDefaultStrokeWidth()));
	for (LineSegment<LineSegmentType> lineSegment : translatedShape.getShape()) {
	    final Vector2D start = lineSegment.getStart();
	    final Vector2D end = lineSegment.getEnd();
	    final LineSegmentType type = lineSegment.getUserData();
	    g.setColor(type.getColor());
	    g.draw(new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY()));
	}

	g.setTransform(oldTransform);
    }
}
