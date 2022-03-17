package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.main.DrawRegion;

import java.awt.*;

public class EmptyMode implements LevelCreatorMode
{
    @Override public void enter(final LevelCreator levelCreator) {}

    @Override public void exit(final LevelCreator levelCreator) {}

    @Override public void cursorPosChanged(final LevelCreator levelCreator) {}

    @Override public void cursorActionPerformed(final LevelCreator levelCreator) {}

    @Override public void draw(final LevelCreator levelCreator, final Graphics2D g, final DrawRegion region) {}
}
