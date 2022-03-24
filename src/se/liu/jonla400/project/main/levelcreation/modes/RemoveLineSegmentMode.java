package se.liu.jonla400.project.main.levelcreation.modes;

import se.liu.jonla400.project.main.levelcreation.IndexedLineSegment;
import se.liu.jonla400.project.main.levelcreation.LevelCreator;
import se.liu.jonla400.project.main.levelcreation.commands.Command;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.Optional;

/**
 * Represents a {@link Mode} that removes the closest line segment to the cursor when the
 * cursor is pressed
 */
public class RemoveLineSegmentMode extends AdaptingMode
{
    /**
     * Removes the line segment closest to the cursor, if there exists any line segments
     *
     * @param levelCreator The considered level creator
     */
    @Override public void cursorPressed(final LevelCreator levelCreator) {
        final Optional<IndexedLineSegment> lineSegmentToRemove = levelCreator.getClosestLineSegmentToCursor();
        lineSegmentToRemove.ifPresent(segment -> levelCreator.execute(new RemoveCommand(segment)));
    }

    /**
     * Highlights the line segment about to be removed, if any line segments exist
     *
     * @param levelCreator The level creator containing the line segments
     * @param g The graphics object to draw to
     */
    @Override public void draw(final LevelCreator levelCreator, final Graphics2D g) {
        levelCreator.getClosestLineSegmentToCursor().ifPresent(segment -> {
            final Vector2D start = segment.getStart();
            final Vector2D end = segment.getEnd();
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(0.3f));
            g.draw(new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY()));
        });
    }

    private static class RemoveCommand implements Command
    {
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
