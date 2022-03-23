package se.liu.jonla400.project.main.drawing;

import se.liu.jonla400.project.main.leveldefinition.LineSegmentDefinition;
import se.liu.jonla400.project.main.leveldefinition.LineSegmentType;
import se.liu.jonla400.project.physics.implementation.collision.LineSegment;
import se.liu.jonla400.project.physics.implementation.collision.TranslatedCustomShape;

import java.awt.*;

public class CustomShapeDrawer implements Drawer
{
    private TranslatedCustomShape<LineSegmentType> translatedShape;
    private LineSegmentDrawer lineSegmentDrawer;

    public CustomShapeDrawer(final TranslatedCustomShape<LineSegmentType> translatedShape, final LineSegmentDrawer lineSegmentDrawer) {
	this.translatedShape = translatedShape;
	this.lineSegmentDrawer = lineSegmentDrawer;
    }

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
