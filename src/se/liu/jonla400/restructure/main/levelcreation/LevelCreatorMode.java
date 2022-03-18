package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.main.RectangularRegion;

import java.awt.*;

public interface LevelCreatorMode extends LevelCreatorKeyListener
{
    void cursorPressed(LevelCreator levelCreator);

    void cursorReleased(LevelCreator levelCreator);

    void draw(LevelCreator levelCreator, Graphics2D g, RectangularRegion region);
}
