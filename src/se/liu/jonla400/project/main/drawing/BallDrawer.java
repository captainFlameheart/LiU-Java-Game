package se.liu.jonla400.project.main.drawing;

import se.liu.jonla400.project.constants.Constants;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class BallDrawer implements Drawer
{
    @Override public void draw(final Graphics2D g) {
        final double radius = Constants.getBallRadius();
        final double diameter = 2 * radius;
        final Shape shape = new Ellipse2D.Double(-radius, -radius, diameter, diameter);
        g.setColor(Constants.getBallFillColor());
        g.fill(shape);
        g.setColor(Constants.getBallStrokeColor());
        g.setStroke(new BasicStroke(Constants.getDefaultStrokeWidth()));
        g.draw(shape);
    }
}
