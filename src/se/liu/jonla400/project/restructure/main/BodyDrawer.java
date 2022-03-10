package se.liu.jonla400.project.restructure.main;

import se.liu.jonla400.project.restructure.math.Vector2D;
import se.liu.jonla400.project.restructure.physics.abstraction.main.Body;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class BodyDrawer implements Drawer
{
    private Body body;
    private Drawer shapeDrawer;

    public BodyDrawer(final Body body, final Drawer shapeDrawer) {
	this.body = body;
	this.shapeDrawer = shapeDrawer;
    }

    @Override public void draw(final Graphics2D g) {
	final Vector2D pos = body.getPos();
	final double angle = body.getAngle();

	final AffineTransform oldTransform = g.getTransform();
	g.translate(pos.getX(), pos.getY());
	g.rotate(angle);

	shapeDrawer.draw(g);

	g.setTransform(oldTransform);
    }
}
