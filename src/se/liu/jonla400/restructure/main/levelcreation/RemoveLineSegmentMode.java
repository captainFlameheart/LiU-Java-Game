package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.main.RectangularRegion;
import se.liu.jonla400.restructure.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.util.Optional;

public class RemoveLineSegmentMode implements LevelCreatorMode
{
    @Override public void cursorPressed(final LevelCreator levelCreator) {
        final Optional<IndexedLineSegment> lineSegmentToRemove = levelCreator.getClosestLineSegmentToCursor();
        lineSegmentToRemove.ifPresent(segment -> levelCreator.execute(new RemoveCommand(segment)));
    }

    @Override public void cursorReleased(final LevelCreator levelCreator) {}

    @Override public void keyPressed(final LevelCreator levelCreator, final KeyEvent keyEvent) {}

    @Override public void keyReleased(final LevelCreator levelCreator, final KeyEvent keyEvent) {}

    @Override public void draw(final LevelCreator levelCreator, final Graphics2D g, final RectangularRegion region) {
        levelCreator.getClosestLineSegmentToCursor().ifPresent(segment -> {
            final Vector2D start = segment.getStart();
            final Vector2D end = segment.getEnd();
            g.setColor(Color.RED);
            g.setStroke(new BasicStroke(0.2f));
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
