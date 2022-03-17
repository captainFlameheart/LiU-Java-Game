package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.main.drawing.Drawer;
import se.liu.jonla400.restructure.math.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class TranslatedDrawer implements Drawer
{
    private Vector2D translation;
    private Drawer drawerAtTranslation;

    public TranslatedDrawer(final Vector2D translation, final Drawer drawerAtTranslation) {
        this.translation = translation;
        this.drawerAtTranslation = drawerAtTranslation;
    }

    @Override public void draw(final Graphics2D g) {
        final AffineTransform oldTransform = g.getTransform();
        g.translate(translation.getX(), translation.getY());
        drawerAtTranslation.draw(g);
        g.setTransform(oldTransform);
    }

}
