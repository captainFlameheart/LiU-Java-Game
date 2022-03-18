package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.main.RectangularRegion;
import se.liu.jonla400.restructure.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ChangeTypeMode implements LevelCreatorMode
{
    @Override public void cursorPressed(final LevelCreator levelCreator) {
        final Optional<IndexedLineSegment> lineSegmentToChange = levelCreator.getClosestLineSegmentToCursor();
        lineSegmentToChange.ifPresent(segment -> {
            final int index = segment.getIndex();
            final LineSegmentType newType = getNextType(segment.getType());
            levelCreator.execute(new SetTypeCommand(levelCreator, index, newType));
        });
    }

    @Override public void cursorReleased(final LevelCreator levelCreator) {}

    @Override public void keyPressed(final LevelCreator levelCreator, final KeyEvent keyEvent) {}

    @Override public void keyReleased(final LevelCreator levelCreator, final KeyEvent keyEvent) {}

    private LineSegmentType getNextType(final LineSegmentType type) {
        final List<LineSegmentType> orderedTypes = Arrays.asList(LineSegmentType.values());
        final int currentIndex = orderedTypes.indexOf(type);
        final int newIndex = (currentIndex + 1) % orderedTypes.size();
        return orderedTypes.get(newIndex);
    }

    @Override public void draw(final LevelCreator levelCreator, final Graphics2D g, final RectangularRegion region) {
        levelCreator.getClosestLineSegmentToCursor().ifPresent(segment -> {
            final Vector2D start = segment.getStart();
            final Vector2D end = segment.getEnd();
            final double radius = 0.3;
            final double diameter = 2 * radius;
            g.setColor(segment.getColor());
            g.setStroke(new BasicStroke(0.1f));
            g.draw(new Ellipse2D.Double(start.getX() - radius, start.getY() - radius, diameter, diameter));
            g.draw(new Ellipse2D.Double(end.getX() - radius, end.getY() - radius, diameter, diameter));
        });
    }

    private static class SetTypeCommand implements Command
    {
        private int lineSegmentIndex;
        private LineSegmentType type;
        private LineSegmentType typeBefore;

        private SetTypeCommand(final LevelCreator levelCreator, final int lineSegmentIndex, final LineSegmentType type) {
            this.lineSegmentIndex = lineSegmentIndex;
            this.type = type;
            typeBefore = levelCreator.getType(lineSegmentIndex);
        }

        @Override public void execute(final LevelCreator levelCreator) {
            levelCreator.setType(lineSegmentIndex, type);
        }

        @Override public void undo(final LevelCreator levelCreator) {
            levelCreator.setType(lineSegmentIndex, typeBefore);
        }
    }
}
