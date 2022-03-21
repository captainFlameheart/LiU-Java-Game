package se.liu.jonla400.project.main.levelcreation;

import java.awt.event.KeyEvent;

public interface KeyListener
{
    void keyPressed(LevelCreator levelCreator, KeyEvent keyEvent);

    void keyReleased(LevelCreator levelCreator, KeyEvent keyEvent);
}
