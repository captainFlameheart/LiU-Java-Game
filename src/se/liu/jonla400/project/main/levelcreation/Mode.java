package se.liu.jonla400.project.main.levelcreation;

import se.liu.jonla400.project.main.RectangularRegion;

import java.awt.*;

public interface Mode extends KeyListener
{
    void cursorPressed(LevelCreator levelCreator);

    void cursorReleased(LevelCreator levelCreator);

    void draw(LevelCreator levelCreator, Graphics2D g, RectangularRegion region);
}
