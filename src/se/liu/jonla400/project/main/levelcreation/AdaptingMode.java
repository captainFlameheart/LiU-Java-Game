package se.liu.jonla400.project.main.levelcreation;

import se.liu.jonla400.project.math.RectangularRegion;

import java.awt.*;
import java.awt.event.KeyEvent;

public class AdaptingMode implements Mode
{
    @Override public void keyPressed(final LevelCreator levelCreator, final KeyEvent keyEvent) {}

    @Override public void keyReleased(final LevelCreator levelCreator, final KeyEvent keyEvent) {}

    @Override public void cursorPressed(final LevelCreator levelCreator) {}

    @Override public void cursorReleased(final LevelCreator levelCreator) {}

    @Override public void draw(final LevelCreator levelCreator, final Graphics2D g, final RectangularRegion region) {}
}