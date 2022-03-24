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

/**
 * A {@link Mode} that adds vertices. By pressing a key, this mode can "magnetize" towards
 * the closest vertices, making it easier to create line segments that go from or to vertices
 * that already exist. From key input the user can also toggle whether ending a line segment
 * immediately begins another. A third key enables removing a line segment in progress.
 */
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

    /**
     * Creates a new AddVertexMode with line segment chaining set to true and magnetized
     * set to false. The key C is used to toggle line segment chaining, the shift key is used
     * to toggle the magnetize feature and the escape key is used to remove incomplete line segments.
     *
     * @return The created AddVertexMode
     */
    public static AddVertexMode createWithDefaultConfigAndKeys() {
	final boolean chainsLineSegments = true;
	final int chainsLineSegmentsKeyCode = KeyEvent.VK_C;
	final boolean magnetized = false;
	final int magnetizedKeyCode = KeyEvent.VK_SHIFT;
	final int removeIncompleteKeyCode = KeyEvent.VK_ESCAPE;
	return new AddVertexMode(chainsLineSegments, chainsLineSegmentsKeyCode, magnetized, magnetizedKeyCode, removeIncompleteKeyCode);
    }

    /**
     * Adds a new vertex. If the line segment is completed and line segment chaining is turned on,
     * a new line segment is started.
     *
     * @param levelCreator The level creator to add vertices to
     */
    @Override public void cursorPressed(final LevelCreator levelCreator) {
	final Vector2D newVertex = getUpcomingVertex(levelCreator);
	final AddVertexCommand addVertexCommand = new AddVertexCommand(newVertex);
	levelCreator.execute(addVertexCommand);
	if (chainsLineSegments && !levelCreator.hasIncompleteLineSegment()) {
	    levelCreator.execute(addVertexCommand); // Add it twice to start a new line segment
	}
    }

    /**
     * Depending on the key does one or none of the following:
     * - Toggles line segment chaining
     * - Sets magnetizing to true
     * - Removes the incomplete line segment, if it exists
     *
     * @param levelCreator The level creator to potentially change the state of
     * @param keyEvent The key event of the pressed key
     */
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

    /**
     * If the correct key is released, the magnetize feature is turned off
     *
     * @param levelCreator ignored
     * @param keyEvent The key event containing the released key
     */
    @Override public void keyReleased(final LevelCreator levelCreator, final KeyEvent keyEvent) {
	if (keyEvent.getKeyCode() == magnetizedKeyCode) {
	    magnetized = false;
	}
    }

    private void removeIncompleteLineSegment(final LevelCreator levelCreator) {
	// Remove it only if it exists
	levelCreator.getIncompleteLineSegmentStart().ifPresent(start -> {
	    final Command removeStartCommand = new ReversedCommand(new AddVertexCommand(start));
	    levelCreator.execute(removeStartCommand);
	});
    }

    @Override public void draw(final LevelCreator levelCreator, final Graphics2D g) {
	final Vector2D upcomingVertex = getUpcomingVertex(levelCreator);

	// Draw the upcoming vertex
	final double radius = 0.2;
	final double diameter = 2 * radius;
	g.setColor(Color.BLACK);
	g.setStroke(new BasicStroke(0.1f));
	g.draw(new Ellipse2D.Double(upcomingVertex.getX() - radius, upcomingVertex.getY() - radius, diameter, diameter));

	// Draw the upcoming line segment, if it exists
	levelCreator.getIncompleteLineSegmentStart().ifPresent(start -> {
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
	    // Don't magnetize towards the start of the line segment...
	    magneticVertices.remove(newLineSegmentStart);
	    // nor to any of its neighbours
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
