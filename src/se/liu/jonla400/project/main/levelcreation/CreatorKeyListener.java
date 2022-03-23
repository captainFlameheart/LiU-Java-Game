package se.liu.jonla400.project.main.levelcreation;

import java.awt.event.KeyEvent;

public interface CreatorKeyListener
{
    void keyPressed(LevelCreator levelCreator, KeyEvent keyEvent);

    // It does not make sense to the remove the parameter levelCreator even though it is currently not used
    void keyReleased(LevelCreator levelCreator, KeyEvent keyEvent);
}
