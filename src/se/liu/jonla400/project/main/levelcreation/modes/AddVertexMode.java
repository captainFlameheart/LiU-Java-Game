package se.liu.jonla400.project.main.levelcreation.modes;

import se.liu.jonla400.project.main.levelcreation.LevelCreator;
import se.liu.jonla400.project.main.levelcreation.commands.ReversedCommand;
import se.liu.jonla400.project.main.levelcreation.commands.Command;
import se.liu.jonla400.project.math.ClosestPointFinder;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class AddVertexMode extends AdaptingMode
{
    private boolean chainsLineSegments;
    private int chainsLineSegmentsKeyCode;

    private boolean magnetized;
    private int magnetizedKeyCode;

    private int removeIncompleteKeyCode;

    private AddVertexMode(final boolean chainsLineSegments, final int chainsLineSegmentsKeyCode, final boolean magnetized,
			 final int magnetizedKeyCode, final int removeIncompleteKeyCode)
    {
	this.chainsLineSegments = chainsLineSegments;
	this.chainsLineSegmentsKeyCode = chainsLineSegmentsKeyCode;
	this.magnetized = magnetized;
	this.magnetizedKeyCode = magnetizedKeyCode;
	this.removeIncompleteKeyCode = removeIncompleteKeyCode;
    }

    public static AddVertexMode createWithDefaultConfigAndKeys() {
	final boolean chainsLineSegments = true;
	final int chainsLineSegmentsKeyCode = KeyEvent.VK_C;
	final boolean magnetized = false;
	final int magnetizedKeyCode = KeyEvent.VK_SHIFT;
	final int removeIncompleteKeyCode = KeyEvent.VK_ESCAPE;
	return new AddVertexMode(chainsLineSegments, chainsLineSegmentsKeyCode, magnetized, magnetizedKeyCode, removeIncompleteKeyCode);
    }

    @Override public void cursorPressed(final LevelCreator levelCreator) {
	final Vector2D newVertex = getUpcomingVertex(levelCreator);
	final AddVertexCommand addVertexCommand = new AddVertexCommand(newVertex);
	levelCreator.execute(addVertexCommand);
	if (chainsLineSegments && !levelCreator.hasIncompleteLineSegment()) {
	    levelCreator.execute(addVertexCommand);
	}
    }

    @Override public void keyPressed(final LevelCreator levelCreator, final KeyEvent keyEvent) {
	final int keyCode = keyEvent.getKeyCode();
	if (keyCode == chainsLineSegmentsKeyCode) {
	    toggleChainsLineSegments(levelCreator);
	} else if (keyCode == magnetizedKeyCode) {
	    magnetized = true;
	} else if (keyCode == removeIncompleteKeyCode) {
	    removeIncompleteLineSegment(levelCreator);
	}
    }

    private void toggleChainsLineSegments(final LevelCreator levelCreator) {
	if (chainsLineSegments) {
	    removeIncompleteLineSegment(levelCreator);
	    chainsLineSegments = false;
	} else {
	    chainsLineSegments = true;
	}
    }

    @Override public void keyReleased(final LevelCreator levelCreator, final KeyEvent keyEvent) {
	if (keyEvent.getKeyCode() == magnetizedKeyCode) {
	    magnetized = false;
	}
    }

    public void removeIncompleteLineSegment(final LevelCreator levelCreator) {
	levelCreator.getIncompleteLineSegmentStart().ifPresent(start -> {
	    final Command removeStartCommand = new ReversedCommand(new AddVertexCommand(start));
	    levelCreator.execute(removeStartCommand);
	});
    }

    @Override public void draw(final LevelCreator levelCreator, final Graphics2D g) {
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
	return ClosestPointFinder.findClosestObject(getMagneticVertices(levelCreator), levelCreator.getCursorPos());
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

    private static class AddVertexCommand implements Command
    {
	private Vector2D vertex;

	private AddVertexCommand(final Vector2D vertex) {
	    this.vertex = vertex;
	}

	@Override public void execute(final LevelCreator levelCreator) {
	    levelCreator.add(vertex);
	}

	@Override public void undo(final LevelCreator levelCreator) {
	    levelCreator.removeVertex();
	}
    }
}
