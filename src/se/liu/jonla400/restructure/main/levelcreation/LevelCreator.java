package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.main.DrawRegion;
import se.liu.jonla400.restructure.main.drawing.CrossDrawer;
import se.liu.jonla400.restructure.math.Interval;
import se.liu.jonla400.restructure.math.Vector2D;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class LevelCreator
{
    private List<Command> commands;
    private int currentCommandIndex;

    private LevelBlueprint blueprint;

    private LevelCreatorMode mode;

    private Vector2D cursorPos;
    private DrawRegion camera;

    public LevelCreator() {
	commands = new ArrayList<>();
	currentCommandIndex = -1;
	blueprint = new LevelBlueprint();
	mode = new EmptyMode();
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

    public LevelCreatorMode getMode() {
	return mode;
    }

    public void setMode(final LevelCreatorMode mode) {
	this.mode.exit(this);
	this.mode = mode;
	mode.enter(this);
    }

    public Vector2D getCursorPos() {
	return cursorPos.copy();
    }

    public void setCursorPos(final Vector2D cursorPos) {
	this.cursorPos.set(cursorPos);
	mode.cursorPosChanged(this);
    }

    public void performCursorAction() {
	mode.cursorActionPerformed(this);
    }

    public DrawRegion getCamera() {
	return camera;
    }

    public void draw(final Graphics2D g, final DrawRegion region) {
	drawBackground(g, region);
	drawFullLineSegments(g, region);
	drawCenterOfMass(g, region);
	drawLevelCamera(g, region);
	mode.draw(this, g, region);
    }

    private void drawBackground(final Graphics2D g, final DrawRegion region) {
	g.setColor(Color.WHITE);
	g.fill(new Rectangle2D.Double(region.getLeftX(), region.getBottomY(), region.getWidth(), region.getHeight()));
    }

    private void drawFullLineSegments(final Graphics2D g, final DrawRegion region) {
	g.setStroke(new BasicStroke(0.1f));

	final Iterator<IndexedLineSegment> lineSegmentIterator = getLineSegmentIterator();
	while (lineSegmentIterator.hasNext()) {
	    final IndexedLineSegment lineSegment = lineSegmentIterator.next();
	    final Vector2D start = lineSegment.getStart();
	    final Vector2D end = lineSegment.getEnd();
	    g.setColor(lineSegment.getType().getColor());
	    g.draw(new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY()));
	}
    }

    private void drawCenterOfMass(final Graphics2D g, final DrawRegion region) {
	final Vector2D pos = blueprint.getCenterOfMass();
	final CrossDrawer drawerAtPos = CrossDrawer.createWithDefaultColor(1, 0.1f);
	final TranslatedDrawer drawer = new TranslatedDrawer(pos, drawerAtPos);
	drawer.draw(g);
    }

    private void drawLevelCamera(final Graphics2D g, final DrawRegion region) {
	final CameraDrawer cameraDrawer = CameraDrawer.createDashed(blueprint.getCamera(), Color.BLACK, 0.1f);
	cameraDrawer.draw(g);
    }

    public Set<Vector2D> getAllVertices() {
	return blueprint.getAllVertices();
    }

    public Vector2D getVertex(final int index) {
	return blueprint.getVertex(index);
    }

    public void add(final Vector2D vertex) {
	blueprint.addVertex(vertex);
    }

    public void removeVertex() {
	blueprint.removeVertex();
    }

    public void addLineSegment(final IndexedLineSegment lineSegment) {
	blueprint.addLineSegment(lineSegment);
    }

    public void removeLineSegment(final int lineSegmentIndex) {
	blueprint.removeLineSegment(lineSegmentIndex);
    }

    public LineSegmentType getType(final int lineSegmentIndex) {
	return blueprint.getType(lineSegmentIndex);
    }

    public void setType(final int lineSegmentIndex, final LineSegmentType type) {
	blueprint.setType(lineSegmentIndex, type);
    }

    public Vector2D getCenterOfMass() {
	return blueprint.getCenterOfMass();
    }

    public void setCenterOfMass(final Vector2D centerOfMass) {
	blueprint.setCenterOfMass(centerOfMass);
    }

    public DrawRegion getLevelCamera() {
	return blueprint.getCamera();
    }

    public void setLevelCamera(final DrawRegion camera) {
	blueprint.setCamera(camera);
    }

    public boolean hasIncompleteLineSegment() {
	return blueprint.hasIncompleteLineSegment();
    }

    public Optional<Vector2D> getIncompleteLineSegmentStart() {
	return blueprint.getIncompleteLineSegmentStart();
    }

    public Iterator<IndexedLineSegment> getLineSegmentIterator() {
	return blueprint.getLineSegmentIterator();
    }

    public Set<Vector2D> getNeighboursTo(final Vector2D vertex) {
	return blueprint.getNeighboursTo(vertex);
    }

    public Optional<IndexedLineSegment> getClosestLineSegmentToCursor() {
	return blueprint.getClosestLineSegmentTo(cursorPos);
    }
}
