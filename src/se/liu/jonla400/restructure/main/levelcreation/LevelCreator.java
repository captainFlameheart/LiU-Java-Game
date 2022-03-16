package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.main.DrawRegion;
import se.liu.jonla400.restructure.math.Interval;
import se.liu.jonla400.restructure.math.Vector2D;
import se.liu.jonla400.restructure.physics.implementation.collision.LineSegmentDefinition;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

public class LevelCreator
{
    private List<Command> commands;
    private int currentCommandIndex;

    private List<Vector2D> vertices;

    private LevelCreatorState state;

    private Vector2D cursorPos;
    private DrawRegion camera;

    public LevelCreator() {
	commands = new ArrayList<>();
	currentCommandIndex = -1;
	vertices = new ArrayList<>();
	state = new EmptyState();
	cursorPos = Vector2D.createZeroVector();
	camera = DrawRegion.createFromIntervals(new Interval(-10, 10), new Interval(-10, 10));
    }

    public void execute(final Command command) {
	removeCommandsAfterCurrent();
	commands.add(command);
	currentCommandIndex++;
	command.execute(this);
    }

    private void removeCommandsAfterCurrent() {
	commands.subList(currentCommandIndex + 1, commands.size()).clear();
    }

    public void undo() {
	if (currentCommandIndex == -1) {
	    return;
	}
	commands.get(currentCommandIndex).undo(this);
	currentCommandIndex--;
    }

    public void redo() {
	if (currentCommandIndex == commands.size() - 1) {
	    return;
	}
	currentCommandIndex++;
	commands.get(currentCommandIndex).execute(this);
    }

    public LevelCreatorState getState() {
	return state;
    }

    public void setState(final LevelCreatorState state) {
	this.state.exit(this);
	this.state = state;
	state.enter(this);
    }

    public Vector2D getCursorPos() {
	return cursorPos.copy();
    }

    public void setCursorPos(final Vector2D cursorPos) {
	this.cursorPos.set(cursorPos);
	state.cursorPosChanged(this);
    }

    public void performCursorAction() {
	state.cursorActionPerformed(this);
    }

    public DrawRegion getCamera() {
	return camera;
    }

    public void draw(final Graphics2D g, final DrawRegion region) {
	drawBackground(g, region);
	drawFullLineSegments(g, region);
	state.draw(this, g, region);
    }

    private void drawBackground(final Graphics2D g, final DrawRegion region) {
	g.setColor(Color.WHITE);
	g.fill(new Rectangle2D.Double(region.getLeftX(), region.getBottomY(), region.getWidth(), region.getHeight()));
    }

    private void drawFullLineSegments(final Graphics2D g, final DrawRegion region) {
	g.setColor(Color.BLACK);
	g.setStroke(new BasicStroke(0.1f));

	final Iterator<IndexedLineSegment> lineSegmentIterator = getLineSegmentIterator();
	while (lineSegmentIterator.hasNext()) {
	    final IndexedLineSegment lineSegment = lineSegmentIterator.next();
	    final Vector2D start = lineSegment.getStartPos();
	    final Vector2D end = lineSegment.getEndPos();
	    g.draw(new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY()));
	}
    }

    public List<Vector2D> getVertices() {
	return vertices;
    }

    public Iterator<IndexedLineSegment> getLineSegmentIterator() {
	return new Iterator<>()
	{
	    private int nextVertexIndex = 0;

	    @Override public boolean hasNext() {
		return nextVertexIndex < vertices.size() - 1;
	    }

	    @Override public IndexedLineSegment next() {
		if (!hasNext()) {
		    throw new NoSuchElementException("No more line segments");
		}
		final IndexedVertex start = getNextVertex();
		final IndexedVertex end = getNextVertex();
		return new IndexedLineSegment(start, end);
	    }

	    private IndexedVertex getNextVertex() {
		final IndexedVertex vertex = new IndexedVertex(nextVertexIndex, vertices.get(nextVertexIndex));
		nextVertexIndex++;
		return vertex;
	    }
	};
    }

    public Set<Vector2D> getNeighboursTo(final Vector2D vertex) {
	final Set<Vector2D> neighbours = new HashSet<>();

	final Iterator<IndexedLineSegment> lineSegmentIterator = getLineSegmentIterator();
	while (lineSegmentIterator.hasNext()) {
	    final IndexedLineSegment lineSegment = lineSegmentIterator.next();
	    final Vector2D start = lineSegment.getStartPos();
	    final Vector2D end = lineSegment.getEndPos();

	    if (start.equals(vertex)) {
		neighbours.add(end);
	    } else if (end.equals(vertex)) {
		neighbours.add(start);
	    }
	}
	return neighbours;
    }

    public Optional<IndexedLineSegment> getClosestLineSegmentToCursor() {
	// LOOK FOR CODE REDUNDANCY!

	IndexedLineSegment closestLineSegment = null;
	double minDist = Double.POSITIVE_INFINITY;

	final Iterator<IndexedLineSegment> lineSegmentIterator = getLineSegmentIterator();
	while (lineSegmentIterator.hasNext()) {
	    final IndexedLineSegment lineSegment = lineSegmentIterator.next();
	    final Vector2D closestPoint = lineSegment.getClosestPointTo(cursorPos);
	    final double dist = closestPoint.subtract(cursorPos).getMagnitudeSquared();
	    if (dist < minDist) {
		closestLineSegment = lineSegment;
		minDist = dist;
	    }
	}

	return Optional.ofNullable(closestLineSegment);
    }
}
