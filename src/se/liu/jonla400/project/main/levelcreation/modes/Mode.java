package se.liu.jonla400.project.main.levelcreation.modes;

import se.liu.jonla400.project.main.levelcreation.CreatorKeyListener;
import se.liu.jonla400.project.main.levelcreation.LevelCreator;

import java.awt.*;

public interface Mode extends CreatorKeyListener
{
    void cursorPressed(LevelCreator levelCreator);

    void cursorReleased(LevelCreator levelCreator);

    void draw(LevelCreator levelCreator, Graphics2D g);
}
