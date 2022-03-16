package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.main.DrawRegion;
import se.liu.jonla400.restructure.math.Vector2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class AddVertexState implements LevelCreatorState
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
	if (chainsLineSegments && getNewLineSegmentStart(levelCreator).isEmpty()) {
	    levelCreator.execute(addVertexCommand);
	}
    }

    public void deleteNewLineSegmentStart(final LevelCreator levelCreator) {
	getNewLineSegmentStart(levelCreator).ifPresent(start -> {
	    final Command deleteStartCommand = new ReversedCommand(new AddVertexCommand(start));
	    levelCreator.execute(deleteStartCommand);
	});
    }

    @Override public void draw(final LevelCreator levelCreator, final Graphics2D g, final DrawRegion region) {
	final Vector2D upcomingVertex = getUpcomingVertex(levelCreator);

	final double radius = 0.2;
	final double diameter = 2 * radius;
	g.setColor(Color.BLACK);
	g.draw(new Ellipse2D.Double(upcomingVertex.getX() - radius, upcomingVertex.getY() - radius, diameter, diameter));

	final Optional<Vector2D> startOfNewLineSegment = getNewLineSegmentStart(levelCreator);
	startOfNewLineSegment.ifPresent(start -> {
	    g.setColor(Color.GREEN);
	    g.setStroke(new BasicStroke(0.1f));
	    g.draw(new Line2D.Double(start.getX(), start.getY(), upcomingVertex.getX(), upcomingVertex.getY()));
	});
    }

    private Vector2D getUpcomingVertex(final LevelCreator levelCreator) {
	return getClosestMagneticVertexToCursor(levelCreator).orElse(levelCreator.getCursorPos());
    }

    private Optional<Vector2D> getNewLineSegmentStart(final LevelCreator levelCreator) {
	final List<Vector2D> vertices = levelCreator.getVertices();
	final int vertexCount = vertices.size();
	if (vertexCount % 2 == 0) {
	    return Optional.empty();
	}
	return Optional.of(vertices.get(vertexCount - 1));
    }

    private Optional<Vector2D> getClosestMagneticVertexToCursor(final LevelCreator levelCreator) {
	return ClosestPointFinder.findClosestPoint(getMagneticVertices(levelCreator), levelCreator.getCursorPos());
    }

    private Set<Vector2D> getMagneticVertices(final LevelCreator levelCreator) {
	if (!magnetized) {
	    return Collections.emptySet();
	}

	final Set<Vector2D> magneticVertices = new HashSet<>(levelCreator.getVertices());
	getNewLineSegmentStart(levelCreator).ifPresent(newLineSegmentStart -> {
	    magneticVertices.remove(newLineSegmentStart);
	    magneticVertices.removeAll(levelCreator.getNeighboursTo(newLineSegmentStart));
	});
	return magneticVertices;
    }
}
