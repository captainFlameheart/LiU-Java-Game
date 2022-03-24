package se.liu.jonla400.project.main.world;

import se.liu.jonla400.project.math.RectangularRegion;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Represents a {@link World} without the knowledge about a camera. A world reacts to user input,
 * but can also tick forward in time and draw itself onto a {@link Graphics2D} object at a given region.
 */
public interface World
{
    /**
     * Update the considered mouse position
     *
     * @param newMousePos The new mouse position
     */
    void updateMousePos(Vector2D newMousePos);

    /**
     * React to the mouse being pressed
     *
     * @param mouseEvent Contains the information about the mouse press
     */
    void mousePressed(MouseEvent mouseEvent);

    /**
     * React to the mouse being released
     *
     * @param mouseEvent Contains the information about the mouse release
     */
    void mouseReleased(MouseEvent mouseEvent);

    /**
     * React to the mouse wheel being moved
     *
     * @param mouseWheelEvent Contains the information about the mouse wheel movement
     */
    void mouseWheelMoved(MouseWheelEvent mouseWheelEvent);

    /**
     * React to a key being pressed
     *
     * @param keyEvent Contains the information about the key press
     */
    void keyPressed(KeyEvent keyEvent);

    /**
     * React to a key being released
     *
     * @param keyEvent Contains the information about the key release
     */
    void keyReleased(KeyEvent keyEvent);

    /**
     * Ticks forward in time
     *
     * @param deltaTime The amount of time to tick forward
     */
    void tick(double deltaTime);

    /**
     * Draws onto the {@link Graphics2D} object at the given region. This method might
     * draw outside the region too.
     *
     * @param g The graphics object to draw to
     * @param region The region required to draw to, but not limited by
     */
    void draw(Graphics2D g, RectangularRegion region);
}
