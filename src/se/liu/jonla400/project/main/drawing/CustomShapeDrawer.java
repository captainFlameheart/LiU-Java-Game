package se.liu.jonla400.project.main.drawing;

import se.liu.jonla400.project.main.leveldefinition.LineSegmentDefinition;
import se.liu.jonla400.project.main.leveldefinition.LineSegmentType;
import se.liu.jonla400.project.physics.collision.implementation.LineSegment;
import se.liu.jonla400.project.physics.collision.implementation.TranslatedCustomShape;

import java.awt.*;

/**
 * Draws a {@link TranslatedCustomShape} onto a {@link Graphics2D} object according to the shape's
 * translation and line segments. How each {@link LineSegment} is drawn is specified by a {@link LineSegmentDrawer}.
 */
public class CustomShapeDrawer implements Drawer
{
    private TranslatedCustomShape<LineSegmentType> translatedShape;
    private LineSegmentDrawer lineSegmentDrawer;

    /**
     * Returns a new CustomShapeDrawer for the given {@link TranslatedCustomShape} and with the given
     * {@link LineSegmentDrawer} as a specification of how each line segment is to be drawn
     *
     * @param translatedShape The shape to draw
     * @param lineSegmentDrawer How each line segment should be drawn
     */
    public CustomShapeDrawer(final TranslatedCustomShape<LineSegmentType> translatedShape, final LineSegmentDrawer lineSegmentDrawer) {
	this.translatedShape = translatedShape;
	this.lineSegmentDrawer = lineSegmentDrawer;
    }

    /**
     * Draws the {@link TranslatedCustomShape} onto the {@link Graphics2D} object according to
     * the shape's translation and line segments
     *
     * @param g The graphics object to draw to
     */
    @Override public void draw(final Graphics2D g) {
	TransformedDrawer.draw(
		g, Transform.createWithTranslation(translatedShape.getTranslation()),
		this::drawWithoutTranslating
	);
    }

    private void drawWithoutTranslating(final Graphics2D g) {
	for (LineSegment<LineSegmentType> lineSegment : translatedShape.getShape()) {
	    lineSegmentDrawer.draw(g, LineSegmentDefinition.createFromCollidableSegment(lineSegment));
	}
    }
}
