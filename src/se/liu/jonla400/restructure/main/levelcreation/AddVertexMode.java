package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.main.DrawRegion;
import se.liu.jonla400.restructure.math.Vector2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class AddVertexMode implements LevelCreatorMode
{
    private boolean chainsLineSegments;
    private boolean magnetized;

    public boolean chainsLineSegments() {
	return chainsLineSegments;
    }

    public void setChainsLineSegments(final boolean chainsLineSegments) {
	this.chainsLineSegments = chainsLineSegments;
    }

    public boolean isMagnetized() {
	return magnetized;
    }

    public void setMagnetized(final boolean magnetized) {
	this.magnetized = magnetized;
    }

    @Override public void enter(final LevelCreator levelCreator) {}

    @Override public void exit(final LevelCreator levelCreator) {
    }

    @Override public void cursorPosChanged(final LevelCreator levelCreator) {}

    @Override public void cursorActionPerformed(final LevelCreator levelCreator) {
	final Vector2D newVertex = getUpcomingVertex(levelCreator);
	final AddVertexCommand addVertexCommand = new AddVertexCommand(newVertex);
	levelCreator.execute(addVertexCommand);
	if (chainsLineSegments && !levelCreator.hasIncompleteLineSegment()) {
	    levelCreator.execute(addVertexCommand);
	}
    }

    public void deleteInclompleteLineSegment(final LevelCreator levelCreator) {
	levelCreator.getIncompleteLineSegmentStart().ifPresent(start -> {
	    final Command deleteStartCommand = new ReversedCommand(new AddVertexCommand(start));
	    levelCreator.execute(deleteStartCommand);
	});
    }

    @Override public void draw(final LevelCreator levelCreator, final Graphics2D g, final DrawRegion region) {
	final Vector2D upcomingVertex = getUpcomingVertex(levelCreator);

	final double radius = 0.2;
	final double diameter = 2 * radius;
	g.setColor(Color.BLACK);
	g.setStroke(new BasicStroke(0.1f));
	g.draw(new Ellipse2D.Double(upcomingVertex.getX() - radius, upcomingVertex.getY() - radius, diameter, diameter));

	final Optional<Vector2D> startOfNewLineSegment = levelCreator.getIncompleteLineSegmentStart();
	startOfNewLineSegment.ifPresent(start -> {
	    g.setColor(Color.GREEN);
	    g.setStroke(new BasicStroke(0.1f));
	    g.draw(new Line2D.Double(start.getX(), start.getY(), upcomingVertex.getX(), upcomingVertex.getY()));
	});
    }

    private Vector2D getUpcomingVertex(final LevelCreator levelCreator) {
	return getClosestMagneticVertexToCursor(levelCreator).orElse(levelCreator.getCursorPos());
    }

    private Optional<Vector2D> getClosestMagneticVertexToCursor(final LevelCreator levelCreator) {
	return ClosestPointFinder.findClosestPoint(getMagneticVertices(levelCreator), levelCreator.getCursorPos());
    }

    private Set<Vector2D> getMagneticVertices(final LevelCreator levelCreator) {
	if (!magnetized) {
	    return Collections.emptySet();
	}

	final Set<Vector2D> magneticVertices = levelCreator.getAllVertices();
	levelCreator.getIncompleteLineSegmentStart().ifPresent(newLineSegmentStart -> {
	    magneticVertices.remove(newLineSegmentStart);
	    magneticVertices.removeAll(levelCreator.getNeighboursTo(newLineSegmentStart));
	});
	return magneticVertices;
    }
}
