package se.liu.jonla400.project.main.levelcreation;

import se.liu.jonla400.project.main.LineSegmentType;
import se.liu.jonla400.project.main.RectangularRegion;
import se.liu.jonla400.project.main.World;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public class LevelCreator implements World
{
    private DrawableLevelBlueprint blueprint;
    private CommandTimeLine commandTimeLine;
    private Mode currentMode;
    private Vector2D cursorPos;
    private KeyListener keyListener;

    public LevelCreator(final DrawableLevelBlueprint blueprint, final CommandTimeLine commandTimeLine, final Mode currentMode,
			final Vector2D cursorPos, final KeyListener keyListener)
    {
	this.blueprint = blueprint;
	this.commandTimeLine = commandTimeLine;
	this.currentMode = currentMode;
	this.cursorPos = cursorPos;
	this.keyListener = keyListener;
    }

    public void execute(final Command command) {
	commandTimeLine.execute(this, command);
    }

    public void undo() {
	commandTimeLine.undo(this);
    }

    public void redo() {
	commandTimeLine.redo(this);
    }

    public Mode getCurrentMode() {
	return currentMode;
    }

    public void setCurrentMode(final Mode currentMode) {
	this.currentMode = currentMode;
    }

    @Override public void cursorMoved(final Vector2D newCursorPos) {
	this.cursorPos.set(newCursorPos);
    }

    public Vector2D getCursorPos() {
	return cursorPos.copy();
    }

    @Override public void cursorPressed() {
	currentMode.cursorPressed(this);
    }

    @Override public void cursorReleased() {
	currentMode.cursorReleased(this);
    }

    @Override public void keyPressed(final KeyEvent keyEvent) {
	keyListener.keyPressed(this, keyEvent);
	currentMode.keyPressed(this, keyEvent);
    }

    @Override public void keyReleased(final KeyEvent keyEvent) {
	keyListener.keyReleased(this, keyEvent);
	currentMode.keyReleased(this, keyEvent);
    }

    @Override public void tick(final double deltaTime) {}

    public void draw(final Graphics2D g, final RectangularRegion region) {
	blueprint.draw(g, region);
	currentMode.draw(this, g, region);
    }

    public LevelBlueprint getBlueprint() {
	return blueprint.getBlueprint();
    }

    public Set<Vector2D> getAllVertices() {
	return blueprint.getAllVertices();
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

    public void moveVertex(final Vector2D vertex, final Vector2D newPos) {
	blueprint.moveVertex(vertex, newPos);
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

    public RectangularRegion getCamera() {
	return blueprint.getCamera();
    }

    public void setCamera(final RectangularRegion camera) {
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
