package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.main.DrawRegion;

import java.awt.*;

// AddVertexState: magnetized, chain
// MoveVertexState
// RemoveLineSegmentState
// ChangeLineSegmentTypeState
public interface LevelCreatorMode
{
    void enter(LevelCreator levelCreator);

    void exit(LevelCreator levelCreator);

    void cursorPosChanged(LevelCreator levelCreator);

    void cursorActionPerformed(LevelCreator levelCreator);

    void draw(LevelCreator levelCreator, Graphics2D g, DrawRegion region);
}
