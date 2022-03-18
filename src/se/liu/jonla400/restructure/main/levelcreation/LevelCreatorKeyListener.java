package se.liu.jonla400.restructure.main.levelcreation;

import java.awt.event.KeyEvent;

public interface LevelCreatorKeyListener
{
    void keyPressed(LevelCreator levelCreator, KeyEvent keyEvent);

    void keyReleased(LevelCreator levelCreator, KeyEvent keyEvent);
}
