package se.liu.jonla400.project.main.drawing;

import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class TransformedDrawer
{
    public static void draw(final Graphics2D g, final Transform transform, final Drawer drawerAfterTransform) {
        final AffineTransform oldTransform = g.getTransform();

        final Vector2D translation = transform.getTranslation();
        g.translate(translation.getX(), translation.getY());
        g.rotate(transform.getRotation());
        final double scale = transform.getScale();
        g.scale(scale, scale);
        drawerAfterTransform.draw(g);

        g.setTransform(oldTransform);
    }
}
