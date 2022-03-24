package se.liu.jonla400.project.main.drawing;

import java.awt.*;

/**
 * Represents an object that can be drawn onto a {@link Graphics2D} object without further
 * parameterization
 */
public interface Drawer
{
    /**
     * Draws this object onto the {@link Graphics2D} object
     *
     * @param g The graphics object to draw to
     */
    void draw(Graphics2D g);
}
