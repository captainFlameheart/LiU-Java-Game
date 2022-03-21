package se.liu.jonla400.project.main.levelcreation;

import se.liu.jonla400.project.constants.Constants;
import se.liu.jonla400.project.math.RectangularRegion;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.Optional;

public class RemoveLineSegmentMode extends AdaptingMode
{
    @Override public void cursorPressed(final LevelCreator levelCreator) {
        final Optional<IndexedLineSegment> lineSegmentToRemove = levelCreator.getClosestLineSegmentToCursor();
        lineSegmentToRemove.ifPresent(segment -> levelCreator.execute(new RemoveCommand(segment)));
    }

    @Override public void draw(final LevelCreator levelCreator, final Graphics2D g, final RectangularRegion region) {
        levelCreator.getClosestLineSegmentToCursor().ifPresent(segment -> {
            final Vector2D start = segment.getStart();
            final Vector2D end = segment.getEnd();
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(2 * Constants.getDefaultStrokeWidth()));
            g.draw(new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY()));
        });
    }

    private static class RemoveCommand implements Command {
        private IndexedLineSegment lineSegment;

        private RemoveCommand(final IndexedLineSegment lineSegment) {
            this.lineSegment = lineSegment;
        }

        @Override public void execute(final LevelCreator levelCreator) {
            levelCreator.removeLineSegment(lineSegment.getIndex());
        }

        @Override public void undo(final LevelCreator levelCreator) {
            levelCreator.addLineSegment(lineSegment);
        }
    }
}
