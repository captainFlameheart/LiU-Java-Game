package se.liu.jonla400.project.main.drawing;

import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * A utility class used to draw any {@link Drawer} after transforming a {@link Graphics2D} according
 * to a {@link Transform}
 */
public class TransformedDrawer
{
    /**
     * Draws the drawer after transforming the graphics according to the transform. The order of
     * the transformation is as follows:
     * 1. Translate
     * 2. Rotate
     * 3. Scale
     * The transform of the graphics object is then reset
     *
     * @param g The graphics to draw to
     * @param transform The transform used to transform the graphics object
     * @param drawerAfterTransform The drawing procedure after the transform
     */
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
