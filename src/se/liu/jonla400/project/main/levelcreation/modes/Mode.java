package se.liu.jonla400.project.main.levelcreation.modes;

import se.liu.jonla400.project.main.levelcreation.CreatorKeyListener;
import se.liu.jonla400.project.main.levelcreation.LevelCreator;

import java.awt.*;

/**
 * Represents a mode of a {@link LevelCreator}. A mode gets to know when
 * the "cursor" (mouse) is pressed and when a key is pressed and can change the state of
 * the level creator at those events. A mode might have its own state, which the level creator
 * thus indirectly also has (since the level creator points to its current mode). A mode can
 * specify its own drawing procedure on top of the drawing procedure directly performed by the
 * level creator.
 */
public interface Mode extends CreatorKeyListener
{
    /**
     * The cursor was pressed. Which mouse button this maps to is determined by the level creator.
     *
     * @param levelCreator The parent level creator
     */
    void cursorPressed(LevelCreator levelCreator);

    /**
     * The cursor was released. Which mouse button this maps to is determined by the level creator.
     *
     * @param levelCreator The parent level creator
     */
    void cursorReleased(LevelCreator levelCreator);

    /**
     * Draw ontop of the normal drawing performed by the level creator
     *
     * @param levelCreator The parent level creator
     * @param g The graphics object to draw to
     */
    void draw(LevelCreator levelCreator, Graphics2D g);
}
