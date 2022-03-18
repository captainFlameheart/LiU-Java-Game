package se.liu.jonla400.restructure.main.drawing;

import se.liu.jonla400.restructure.constants.DrawConstants;
import se.liu.jonla400.restructure.main.levelcreation.LineSegmentType;
import se.liu.jonla400.restructure.math.Vector2D;
import se.liu.jonla400.restructure.physics.implementation.collision.LineSegment;
import se.liu.jonla400.restructure.physics.implementation.collision.TranslatedCustomShape;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

public class CustomShapeDrawer implements Drawer
{
    private TranslatedCustomShape<LineSegmentType> translatedShape;
   // private Color color;
    //private BasicStroke stroke;

    public CustomShapeDrawer(final TranslatedCustomShape<LineSegmentType> translatedShape) {
	this.translatedShape = translatedShape;
	//this.color = color;
	//this.stroke = stroke;
    }

    /*public static CustomShapeDrawer createWithDefaultColor(final TranslatedCustomShape translatedShape, final float strokeWidth) {
	return new CustomShapeDrawer(translatedShape, Color.BLACK, new BasicStroke(strokeWidth));
    }*/

    @Override public void draw(final Graphics2D g) {
	final AffineTransform oldTransform = g.getTransform();

	final Vector2D translation = translatedShape.getTranslation();
	g.translate(translation.getX(), translation.getY());

	g.setStroke(new BasicStroke(DrawConstants.getDefaultStrokeWidth()));
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
