package se.liu.jonla400.project.main.world;

import se.liu.jonla400.project.math.RectangularRegion;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Represents a {@link World} that provides empty implementations for each inherited method to simplify
 * the creation of worlds
 */
public class AdaptingWorld implements World
{
    @Override public void updateMousePos(final Vector2D newMousePos) {}

    @Override public void mousePressed(final MouseEvent mouseEvent) {}

    @Override public void mouseReleased(final MouseEvent mouseEvent) {}

    @Override public void mouseWheelMoved(final MouseWheelEvent mouseWheelEvent) {}

    @Override public void keyPressed(final KeyEvent keyEvent) {}

    @Override public void keyReleased(final KeyEvent keyEvent) {}

    @Override public void tick(final double deltaTime) {}

    @Override public void draw(final Graphics2D g, final RectangularRegion region) {}
}
