package se.liu.jonla400.project.main.levelcreation;

import java.awt.event.KeyEvent;

/**
 * Represents a listener to key events that occur on a {@link LevelCreator}
 */
public interface CreatorKeyListener
{
    /**
     * Reacts to a key press
     *
     * @param levelCreator The parent level creator
     * @param keyEvent The information about the key press
     */
    void keyPressed(LevelCreator levelCreator, KeyEvent keyEvent);

    /**
     * Reacts to a key release
     *
     * @param levelCreator The parent level creator
     * @param keyEvent The information about the key release
     */
    void keyReleased(LevelCreator levelCreator, KeyEvent keyEvent);
    // The level creator parameter will likely be used in the future
}
