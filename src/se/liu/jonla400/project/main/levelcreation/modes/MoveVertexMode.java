package se.liu.jonla400.project.main.levelcreation.modes;

import se.liu.jonla400.project.main.levelcreation.LevelCreator;
import se.liu.jonla400.project.main.levelcreation.commands.ReversedCommand;
import se.liu.jonla400.project.main.levelcreation.commands.CombinedCommand;
import se.liu.jonla400.project.main.levelcreation.commands.Command;
import se.liu.jonla400.project.math.ClosestPointFinder;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Optional;
import java.util.Set;

/**
 * Represents a {@link Mode} that moves vertices. There exists a button to deselect a vertex
 * which is about to be moved.
 */
public class MoveVertexMode extends AdaptingMode
{
    private Optional<Vector2D> possibleSelectedVertex;
    private int deselectVertexKeyCode;

    private MoveVertexMode(final Optional<Vector2D> possibleSelectedVertex, final int deselectVertexKeyCode) {
	this.possibleSelectedVertex = possibleSelectedVertex;
	this.deselectVertexKeyCode = deselectVertexKeyCode;
    }

    /**
     * Creates a new MoveVertexMode with the key escape used to deselect a vertex about to move
     *
     * @return The created MoveVertexMode
     */
    public static MoveVertexMode createWithDefaultDeselectKey() {
	return new MoveVertexMode(Optional.empty(), KeyEvent.VK_ESCAPE);
    }

    /**
     * Selects or moves a vertex. If no vertex exists, does nothing.
     *
     * @param levelCreator The considered level creator
     */
    @Override public void cursorPressed(final LevelCreator levelCreator) {
	getCursorPressedCommand(levelCreator).ifPresent(levelCreator::execute);
    }

    private Optional<Command> getCursorPressedCommand(final LevelCreator levelCreator) {
	if (possibleSelectedVertex.isEmpty()) {
	    return getClosestVertexToCursor(levelCreator).map(SelectVertexCommand::new);
	}
	final Vector2D selectedVertex = possibleSelectedVertex.get();
	final Command moveAndDeselectVertexCommand = CombinedCommand.create(
		new MoveVertexCommand(selectedVertex, levelCreator.getCursorPos()),
		createDeselectVertexCommand(selectedVertex)
	);
	return Optional.of(moveAndDeselectVertexCommand);
    }

    /**
     * If the correct key is pressed, deselects the vertex about to move (if any)
     *
     * @param levelCreator The considered level creator
     * @param keyEvent The key event containing the pressed key
     */
    @Override public void keyPressed(final LevelCreator levelCreator, final KeyEvent keyEvent) {
	if (keyEvent.getKeyCode() == deselectVertexKeyCode) {
	    possibleSelectedVertex.ifPresent(selectedVertex -> levelCreator.execute(createDeselectVertexCommand(selectedVertex)));
	}
    }

    private Command createDeselectVertexCommand(final Vector2D vertex) {
	return new ReversedCommand(new SelectVertexCommand(vertex));
    }

    /**
     * Highlights the vertex to be selected, or if one already is selected highlights how the line segments
     * containing that vertex will change.
     *
     * @param levelCreator The considered level creator
     * @param g The graphics object to draw to
     */
    @Override public void draw(final LevelCreator levelCreator, final Graphics2D g) {
	if (possibleSelectedVertex.isEmpty()) {
	    drawClosestVertexToCursorIfExists(levelCreator, g);
	} else {
	    g.setColor(new Color(255, 0, 150));
	    final float dashLength = 0.5f;
	    g.setStroke(new BasicStroke(0.1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f,
					new float[]{dashLength}, 0));

	    final Vector2D selectedVertex = possibleSelectedVertex.get();
	    drawVertex(selectedVertex, g);
	    // Highlight how the segments containing the selected vertex will change
	    final Set<Vector2D> selectedVertexNeighbours = levelCreator.getNeighboursTo(selectedVertex);
	    final Vector2D cursorPos = levelCreator.getCursorPos();
	    for (Vector2D neighbour : selectedVertexNeighbours) {
		g.draw(new Line2D.Double(neighbour.getX(), neighbour.getY(), cursorPos.getX(), cursorPos.getY()));
	    }
	}
    }

    private void drawClosestVertexToCursorIfExists(final LevelCreator levelCreator, final Graphics2D g) {
	getClosestVertexToCursor(levelCreator).ifPresent(closest -> {
	    g.setColor(Color.BLACK);
	    drawVertex(closest, g);
	});
    }

    private void drawVertex(final Vector2D vertex, Graphics2D g) {
	final double radius = 0.3;
	final double diameter = 2 * radius;
	g.fill(new Ellipse2D.Double(vertex.getX() - radius, vertex.getY() - radius, diameter, diameter));
    }

    private Optional<Vector2D> getClosestVertexToCursor(final LevelCreator levelCreator) {
	return ClosestPointFinder.findClosestObject(levelCreator.getAllVertices(), levelCreator.getCursorPos());
    }

    private class SelectVertexCommand implements Command {
	private Vector2D vertex;

	private SelectVertexCommand(final Vector2D vertex) {
	    this.vertex = vertex;
	}

	@Override public void execute(final LevelCreator levelCreator) {
	    possibleSelectedVertex = Optional.of(vertex);
	}

	@Override public void undo(final LevelCreator levelCreator) {
	    possibleSelectedVertex = Optional.empty();
	}
    }

    private static class MoveVertexCommand implements Command
    {
	private Vector2D oldVertex;
	private Vector2D newVertex;

	private MoveVertexCommand(final Vector2D oldVertex, final Vector2D newVertex) {
	    this.oldVertex = oldVertex;
	    this.newVertex = newVertex;
	}

	@Override public void execute(final LevelCreator levelCreator) {
	    levelCreator.moveVertex(oldVertex, newVertex);
	}

	@Override public void undo(final LevelCreator levelCreator) {
	    levelCreator.moveVertex(newVertex, oldVertex);
	}
    }
}
