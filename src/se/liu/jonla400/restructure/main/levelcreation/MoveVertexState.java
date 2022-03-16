package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.main.DrawRegion;
import se.liu.jonla400.restructure.math.Vector2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.Optional;
import java.util.Set;

public class MoveVertexState implements LevelCreatorState
{
    private Optional<Vector2D> possibleGrabbedVertex;

    public MoveVertexState() {
	possibleGrabbedVertex = Optional.empty();
    }

    @Override public void enter(final LevelCreator levelCreator) {}

    @Override public void exit(final LevelCreator levelCreator) {
    }

    @Override public void cursorPosChanged(final LevelCreator levelCreator) {}

    @Override public void cursorActionPerformed(final LevelCreator levelCreator) {
	getCommand(levelCreator).ifPresent(levelCreator::execute);
    }

    private Optional<Command> getCommand(final LevelCreator levelCreator) {
	if (possibleGrabbedVertex.isEmpty()) {
	    return getClosestVertexToCursor(levelCreator).map(GrabVertexCommand::new);
	}
	return Optional.of(new ReleaseVertexCommand(possibleGrabbedVertex.get(), levelCreator.getCursorPos()));
    }

    @Override public void draw(final LevelCreator levelCreator, final Graphics2D g, final DrawRegion region) {
	if (possibleGrabbedVertex.isEmpty()) {
	    drawClosestVertexToCursorIfExists(levelCreator, g);
	} else {
	    final Vector2D grabbedVertex = possibleGrabbedVertex.get();

	    g.setColor(new Color(255, 0, 150));
	    final float dashLength = 0.5f;
	    g.setStroke(new BasicStroke(0.1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f,
					new float[]{dashLength}, 0));

	    drawVertex(grabbedVertex, g);

	    final Set<Vector2D> grabbedVertexNeighbours = levelCreator.getNeighboursTo(possibleGrabbedVertex.get());
	    final Vector2D cursorPos = levelCreator.getCursorPos();
	    for (Vector2D neighbour : grabbedVertexNeighbours) {
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
	return ClosestPointFinder.findClosestPoint(levelCreator.getVertices(), levelCreator.getCursorPos());
    }

    private class GrabVertexCommand implements Command {
	private Vector2D vertex;

	private GrabVertexCommand(final Vector2D vertex) {
	    this.vertex = vertex;
	}

	@Override public void execute(final LevelCreator levelCreator) {
	    possibleGrabbedVertex = Optional.of(vertex);
	}

	@Override public void undo(final LevelCreator levelCreator) {
	    possibleGrabbedVertex = Optional.empty();
	}
    }

    private class ReleaseVertexCommand implements Command
    {
	private Vector2D vertex;
	private Vector2D newVertexPos;
	private Vector2D oldVertexPos;

	private ReleaseVertexCommand(final Vector2D vertex, final Vector2D newVertexPos) {
	    this.vertex = vertex;
	    this.newVertexPos = newVertexPos;
	    oldVertexPos = null;
	}

	@Override public void execute(final LevelCreator levelCreator) {
	    oldVertexPos = vertex.copy();
	    vertex.set(newVertexPos);

	    possibleGrabbedVertex = Optional.empty();
	}

	@Override public void undo(final LevelCreator levelCreator) {
	    vertex.set(oldVertexPos);

	    possibleGrabbedVertex = Optional.of(vertex);
	}
    }
}