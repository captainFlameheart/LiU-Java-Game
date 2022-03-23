package se.liu.jonla400.project.main.levelcreation.modes;

import se.liu.jonla400.project.main.levelcreation.IndexedLineSegment;
import se.liu.jonla400.project.main.levelcreation.LevelCreator;
import se.liu.jonla400.project.main.levelcreation.commands.Command;
import se.liu.jonla400.project.main.leveldefinition.LineSegmentType;
import se.liu.jonla400.project.math.RectangularRegion;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ChangeTypeMode extends AdaptingMode
{
    @Override public void cursorPressed(final LevelCreator levelCreator) {
        final Optional<IndexedLineSegment> lineSegmentToChange = levelCreator.getClosestLineSegmentToCursor();
        lineSegmentToChange.ifPresent(segment -> {
            final int segmentIndex = segment.getIndex();
            final LineSegmentType oldType = segment.getType();
            final LineSegmentType newType = getTypeAfter(oldType);
            levelCreator.execute(new ChangeTypeCommand(segmentIndex, oldType, newType));
        });
    }

    private LineSegmentType getTypeAfter(final LineSegmentType type) {
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
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(0.1f));
            g.draw(new Ellipse2D.Double(start.getX() - radius, start.getY() - radius, diameter, diameter));
            g.draw(new Ellipse2D.Double(end.getX() - radius, end.getY() - radius, diameter, diameter));
        });
    }

    private static class ChangeTypeCommand implements Command
    {
        private int lineSegmentIndex;
        private LineSegmentType newType;
        private LineSegmentType oldType;

        private ChangeTypeCommand(final int lineSegmentIndex, final LineSegmentType oldType, final LineSegmentType newType) {
            this.lineSegmentIndex = lineSegmentIndex;
            this.oldType = oldType;
            this.newType = newType;
        }

        @Override public void execute(final LevelCreator levelCreator) {
            levelCreator.setType(lineSegmentIndex, newType);
        }

        @Override public void undo(final LevelCreator levelCreator) {
            levelCreator.setType(lineSegmentIndex, oldType);
        }
    }
}
