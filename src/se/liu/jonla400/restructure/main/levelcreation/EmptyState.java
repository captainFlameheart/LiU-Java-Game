package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.main.DrawRegion;

import java.awt.*;

public class EmptyState implements LevelCreatorState
{
    @Override public void cursorPosChanged(final LevelCreator1 levelCreator) {}

    @Override public void cursorActionPerformed(final LevelCreator1 levelCreator) {}

    @Override public void draw(final LevelCreator1 levelCreator, final Graphics2D g, final DrawRegion region) {}
}
