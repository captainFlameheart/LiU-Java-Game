package se.liu.jonla400.project.main.levelcreation;

import se.liu.jonla400.project.main.drawing.DrawConfiguration;
import se.liu.jonla400.project.main.levelcreation.commands.Command;
import se.liu.jonla400.project.main.levelcreation.commands.CommandTimeLine;
import se.liu.jonla400.project.main.levelcreation.modes.Mode;
import se.liu.jonla400.project.main.leveldefinition.LineSegmentType;
import se.liu.jonla400.project.math.RectangularRegion;
import se.liu.jonla400.project.main.world.AdaptingWorld;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Optional;
import java.util.Set;

/**
 * Represents a {@link se.liu.jonla400.project.main.world.World} that represent the
 * current construction of a {@link se.liu.jonla400.project.main.leveldefinition.LevelDefinition}.
 * A {@link LevelBlueprint} is used as the current state of the level, and a level creator also
 * contains a {@link CommandTimeLine} used for undoing and redoing commands, a current {@link Mode},
 * and a {@link CreatorKeyListener} for handling non-mode specific key events.
 */
public class LevelCreator extends AdaptingWorld
{
    private DrawableLevelBlueprint blueprint;
    private CommandTimeLine commandTimeLine;
    private int cursorActionButton;
    private Mode currentMode;
    private Vector2D cursorPos;
    private CreatorKeyListener keyListener;

    public LevelCreator(final DrawableLevelBlueprint blueprint, final CommandTimeLine commandTimeLine, final int cursorActionButton,
			final Mode currentMode, final Vector2D cursorPos, final CreatorKeyListener keyListener)
    {
	this.blueprint = blueprint;
	this.commandTimeLine = commandTimeLine;
	this.cursorActionButton = cursorActionButton;
	this.currentMode = currentMode;
	this.cursorPos = cursorPos;
	this.keyListener = keyListener;
    }

    /**
     * Executes the given command. Any commands that could be redone
     * are forgotten.
     *
     * @param command The command to execute
     */
    public void execute(final Command command) {
	commandTimeLine.execute(this, command);
    }

    /**
     * Undo the current command in the time line, if any
     */
    public void undo() {
	commandTimeLine.undo(this);
    }

    /**
     * Redo the next command in the time line, if any
     */
    public void redo() {
	commandTimeLine.redo(this);
    }

    /**
     * @return The currently active mode
     */
    public Mode getCurrentMode() {
	return currentMode;
    }

    /**
     * @param currentMode The mode to set
     */
    public void setCurrentMode(final Mode currentMode) {
	this.currentMode = currentMode;
    }

    /**
     * @param newMousePos The new mouse position to consider
     */
    @Override public void updateMousePos(final Vector2D newMousePos) {
	this.cursorPos.set(newMousePos);
    }

    /**
     * @return A read-only view of the cursor's position
     */
    public Vector2D getCursorPos() {
	return cursorPos.copy();
    }

    /**
     * If the correct mouse button is pressed, the current mode handles it
     *
     * @param mouseEvent The mouse event containing the pressed mouse
     */
    @Override public void mousePressed(final MouseEvent mouseEvent) {
	if (mouseEvent.getButton() == cursorActionButton) {
	    currentMode.cursorPressed(this);
	}
    }

    /**
     * If the correct mouse button is released, the current mode handles it
     *
     * @param mouseEvent The mouse event containing the released mouse
     */
    @Override public void mouseReleased(final MouseEvent mouseEvent) {
	if (mouseEvent.getButton() == cursorActionButton) {
	    currentMode.cursorReleased(this);
	}
    }

    /**
     * Both the current mode and the non-mode specific {@link java.awt.event.KeyListener} handles
     * the key press
     *
     * @param keyEvent The information about the pressed key
     */
    @Override public void keyPressed(final KeyEvent keyEvent) {
	keyListener.keyPressed(this, keyEvent);
	currentMode.keyPressed(this, keyEvent);
    }

    /**
     * Both the current mode and the non-mode specific {@link java.awt.event.KeyListener} handles
     * the key release
     *
     * @param keyEvent The information about the released key
     */
    @Override public void keyReleased(final KeyEvent keyEvent) {
	keyListener.keyReleased(this, keyEvent);
	currentMode.keyReleased(this, keyEvent);
    }

    /**
     * Draws the level creator onto the {@link Graphics2D} object at the region. This method
     * might draw outside the region too
     *
     * @param g The graphics object to draw to
     * @param region The region required to draw to, but not limited by
     */
    @Override public void draw(final Graphics2D g, final RectangularRegion region) {
	blueprint.draw(g, region);
	currentMode.draw(this, g);
    }

    /**
     * @return The level blueprint
     */
    public LevelBlueprint getBlueprint() {
	return blueprint.getBlueprint();
    }

    /**
     * @return How the level blueprint is drawn
     */
    public DrawConfiguration getDrawConfig() {
	return blueprint.getDrawConfig();
    }

    /**
     * @return A read-only set of all the vertices
     */
    public Set<Vector2D> getAllVertices() {
	return blueprint.getAllVertices();
    }

    /**
     * @param vertex The vertex to add. No reference to the vector is kept.
     */
    public void add(final Vector2D vertex) {
	blueprint.addVertex(vertex);
    }

    /**
     * Removes the last added vertex
     */
    public void removeVertex() {
	blueprint.removeVertex();
    }

    /**
     * Adds a line segment at its index
     *
     * @param lineSegment The line segment (including its index) to add
     */
    public void addLineSegment(final IndexedLineSegment lineSegment) {
	blueprint.addLineSegment(lineSegment);
    }

    /**
     * Removes the line segment at the given index
     *
     * @param lineSegmentIndex The index of the line segment to remove
     */
    public void removeLineSegment(final int lineSegmentIndex) {
	blueprint.removeLineSegment(lineSegmentIndex);
    }

    /**
     * @param vertex The vertex to move (equivalently the position of the vertex)
     * @param newPos The new position of the vertex
     */
    public void moveVertex(final Vector2D vertex, final Vector2D newPos) {
	blueprint.moveVertex(vertex, newPos);
    }

    /**
     * Sets the type of the line segment at the given index
     *
     * @param lineSegmentIndex The index of the line segment to set the type of
     * @param type The type to set
     */
    public void setType(final int lineSegmentIndex, final LineSegmentType type) {
	blueprint.setLineSegmentType(lineSegmentIndex, type);
    }

    /**
     * @return A read-only view of the level's center of mass
     */
    public Vector2D getCenterOfMass() {
	return blueprint.getCenterOfMass();
    }

    /**
     * Sets the level's center of mass. No reference to the input vector is kept.
     *
     * @param centerOfMass The new position of the level's center of mass
     */
    public void setCenterOfMass(final Vector2D centerOfMass) {
	blueprint.setCenterOfMass(centerOfMass);
    }

    /**
     * @return A read-only view of the level's camera
     */
    public RectangularRegion getCamera() {
	return blueprint.getCamera();
    }

    /**
     * Sets the level camera. No reference to the input camera is kept.
     *
     * @param camera The new camera
     */
    public void setCamera(final RectangularRegion camera) {
	blueprint.setCamera(camera);
    }

    /**
     * @return Whether there exists a line segment with a start but without an end
     */
    public boolean hasIncompleteLineSegment() {
	return blueprint.hasIncompleteLineSegment();
    }

    /**
     * @return The start of the incomplete line segment, if it exists
     */
    public Optional<Vector2D> getIncompleteLineSegmentStart() {
	return blueprint.getIncompleteLineSegmentStart();
    }

    /**
     * Returns the read-only neighbours to the given vertex
     *
     * @param vertex The vertex to get the neighbours to
     * @return The neighbours to the vertex
     */
    public Set<Vector2D> getNeighboursTo(final Vector2D vertex) {
	return blueprint.getNeighboursTo(vertex);
    }

    /**
     * Returns the line segment that contains the closest point to the cursor.
     * If no line segments exist, returns Optional.empty()
     *
     * @return The closest line segment, if any exists
     */
    public Optional<IndexedLineSegment> getClosestLineSegmentToCursor() {
	return blueprint.getClosestLineSegmentTo(cursorPos);
    }
}
