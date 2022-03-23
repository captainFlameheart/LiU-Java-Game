package se.liu.jonla400.project.main.drawing;

import se.liu.jonla400.project.main.leveldefinition.LineSegmentDefinition;
import se.liu.jonla400.project.main.leveldefinition.LineSegmentType;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.EnumMap;
import java.util.Map;

public class LineSegmentDrawer
{
    private Map<LineSegmentType, ColorAndStrokePair> typeToColorAndStroke;

    private LineSegmentDrawer(final Map<LineSegmentType, ColorAndStrokePair> typeToColorAndStroke) {
	this.typeToColorAndStroke = typeToColorAndStroke;
    }

    public static LineSegmentDrawer createDefault() {
	final Map<LineSegmentType, ColorAndStrokePair> typeToColorAndStroke = new EnumMap<>(LineSegmentType.class);
	typeToColorAndStroke.put(LineSegmentType.DEFAULT, ColorAndStrokePair.createWithDefaultStroke(Color.BLACK));
	typeToColorAndStroke.put(LineSegmentType.LOOSE, ColorAndStrokePair.createWithDefaultStroke(Color.RED));
	typeToColorAndStroke.put(LineSegmentType.WIN, ColorAndStrokePair.createWithDefaultStroke(Color.GREEN));
	return new LineSegmentDrawer(typeToColorAndStroke);
    }

    public void draw(final Graphics2D g, final LineSegmentDefinition lineSegment) {
	final Vector2D start = lineSegment.getStart();
	final Vector2D end = lineSegment.getEnd();

	final ColorAndStrokePair colorAndStroke = getColorAndStrokeOf(lineSegment);
	g.setColor(colorAndStroke.color);
	g.setStroke(colorAndStroke.stroke);
	g.draw(new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY()));
    }

    private ColorAndStrokePair getColorAndStrokeOf(final LineSegmentDefinition lineSegment) {
	return typeToColorAndStroke.get(lineSegment.getType());
    }

    private static class ColorAndStrokePair {
	private Color color;
	private BasicStroke stroke;

	private ColorAndStrokePair(final Color color, final BasicStroke stroke) {
	    this.color = color;
	    this.stroke = stroke;
	}

	private static ColorAndStrokePair createWithDefaultStroke(final Color color) {
	    return new ColorAndStrokePair(color, new BasicStroke(0.1f));
	}
    }
}
