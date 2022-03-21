package se.liu.jonla400.project.main.drawing;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.abstraction.main.Body;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class BodyDrawer implements Drawer
{
    private Body body;
    private Drawer localSpaceDrawer;

    public BodyDrawer(final Body body, final Drawer localSpaceDrawer) {
        this.body = body;
        this.localSpaceDrawer = localSpaceDrawer;
    }

    @Override public void draw(final Graphics2D g) {
        final AffineTransform oldTransform = g.getTransform();

        final Vector2D pos = body.getPos();
        g.translate(pos.getX(), pos.getY());
        g.rotate(body.getAngle());
        localSpaceDrawer.draw(g);

        g.setTransform(oldTransform);
    }
}
