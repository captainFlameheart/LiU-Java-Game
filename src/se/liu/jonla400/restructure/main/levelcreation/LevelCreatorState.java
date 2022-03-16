package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.main.DrawRegion;

import java.awt.*;

// AddVertexState: magnetized,
// MoveVertexState
// RemoveLineSegmentState
// ChangeLineSegmentTypeState
public interface LevelCreatorState
{
    void cursorPosChanged(LevelCreator1 levelCreator);

    void cursorActionPerformed(LevelCreator1 levelCreator);

    void draw(LevelCreator1 levelCreator, Graphics2D g, DrawRegion region);
}
