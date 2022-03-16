package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.main.DrawRegion;
import se.liu.jonla400.restructure.math.Vector2D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DeleteLineSegmentState implements LevelCreatorState
{
    @Override public void enter(final LevelCreator levelCreator) {
    }

    @Override public void exit(final LevelCreator levelCreator) {
    }

    @Override public void cursorPosChanged(final LevelCreator levelCreator) {
    }

    @Override public void cursorActionPerformed(final LevelCreator levelCreator) {
        final Optional<IndexedLineSegment> lineSegmentToDelete = levelCreator.getClosestLineSegmentToCursor();
        lineSegmentToDelete.ifPresent(segment -> levelCreator.execute(new DeleteCommand(segment)));
    }

    @Override public void draw(final LevelCreator levelCreator, final Graphics2D g, final DrawRegion region) {
        final Optional<IndexedLineSegment> closestLineSegment = levelCreator.getClosestLineSegmentToCursor();
        closestLineSegment.ifPresent(segment -> {
            final Vector2D start = segment.getStartPos();
            final Vector2D end = segment.getEndPos();
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(0.2f));
            g.draw(new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY()));
        });
    }

    private static class DeleteCommand implements Command {
        private IndexedLineSegment lineSegment;

        private DeleteCommand(final IndexedLineSegment lineSegment) {
            this.lineSegment = lineSegment;
        }

        @Override public void execute(final LevelCreator levelCreator) {
            final List<Vector2D> vertices = levelCreator.getVertices();
            vertices.subList(lineSegment.getStartIndex(), lineSegment.getEndIndex() + 1).clear();
        }

        @Override public void undo(final LevelCreator levelCreator) {
            final List<Vector2D> vertices = levelCreator.getVertices();
            vertices.addAll(lineSegment.getStartIndex(), Arrays.asList(lineSegment.getStartPos(), lineSegment.getEndPos()));
        }
    }
}
