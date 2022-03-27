package se.liu.jonla400.project.main.game;

import se.liu.jonla400.project.main.drawing.DrawConfiguration;
import se.liu.jonla400.project.math.RectangularRegion;
import se.liu.jonla400.project.main.leveldefinition.LevelDefinition;
import se.liu.jonla400.project.main.world.FilmedWorld;
import se.liu.jonla400.project.main.world.WorldWithMovableCamera;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Represents the game world, including the camera (from which the
 * {@link se.liu.jonla400.project.main.world.WorldGUI} determines what to show of the world).
 * The game world has a list of levels and a currently active level. It also has a button
 * for restarting the current level. The game world resets the current level if the player loses,
 * and moves onto the next level if the player wins.
 */
public class GameWorld implements FilmedWorld, LevelListener
{
    private final static Logger LOGGER = Logger.getLogger(GameWorld.class.getName());

    private DrawConfiguration drawConfig;
    private List<LevelDefinition> levelDefinitions;
    private int currentLevelIndex;
    private WorldWithMovableCamera<LevelWorld> currentLevelWithMovableCamera;
    private int restartKeyCode;

    private GameWorld(final DrawConfiguration drawConfig, final List<LevelDefinition> levelDefinitions, final int currentLevelIndex,
		     final WorldWithMovableCamera<LevelWorld> currentLevelWithMovableCamera, final int restartKeyCode)
    {
	this.drawConfig = drawConfig;
	this.levelDefinitions = levelDefinitions;
	this.currentLevelIndex = currentLevelIndex;
	this.currentLevelWithMovableCamera = currentLevelWithMovableCamera;
	this.restartKeyCode = restartKeyCode;
    }

    /**
     * Creates a new GameWorld that starts on the first level. The restart key is set to enter.
     *
     * @param drawConfig A specification of how the ball, line segments and the level's center of mass is to be drawn
     * @param levelDefinitions The list of levels, must be non-empty
     * @return The created game world that has started the first level
     */
    public static GameWorld createAndStartWithFirstLevel(final DrawConfiguration drawConfig, final List<LevelDefinition> levelDefinitions) {
	if (levelDefinitions.isEmpty()) {
	    throw new IllegalArgumentException("No levels");
	}

	final int currentLevelIndex = 0;
	final int restartKeyCode = KeyEvent.VK_ENTER;
	final GameWorld gameWorld = new GameWorld(
		drawConfig, new ArrayList<>(levelDefinitions), currentLevelIndex, null, restartKeyCode);
	gameWorld.startCurrentLevel();
	return gameWorld;
    }

    /**
     * @return The camera of the current level
     */
    @Override public RectangularRegion getCamera() {
	return currentLevelWithMovableCamera.getCamera();
    }

    /**
     * Updates the considered mouse position for the current level
     *
     * @param newMousePos The new mouse position
     */
    @Override public void updateMousePos(final Vector2D newMousePos) {
	currentLevelWithMovableCamera.updateMousePos(newMousePos);
    }

    /**
     * Lets the current level handle the mouse press
     *
     * @param mouseEvent The mouse event of the press
     */
    @Override public void mousePressed(final MouseEvent mouseEvent) {
	currentLevelWithMovableCamera.mousePressed(mouseEvent);
    }

    /**
     * Lets the current level handle the mouse release
     *
     * @param mouseEvent The mouse event of the release
     */
    @Override public void mouseReleased(final MouseEvent mouseEvent) {
	currentLevelWithMovableCamera.mouseReleased(mouseEvent);
    }

    /**
     * Lets the current level handle the movement of the mouse wheel
     *
     * @param mouseWheelEvent The mouse wheel event
     */
    @Override public void mouseWheelMoved(final MouseWheelEvent mouseWheelEvent) {
	currentLevelWithMovableCamera.mouseWheelMoved(mouseWheelEvent);
    }

    /**
     * Restarts the level if the correct key is pressed.
     * Otherwise lets the current level handle the key press
     *
     * @param keyEvent The key event of the press
     */
    @Override public void keyPressed(final KeyEvent keyEvent) {
	if (keyEvent.getKeyCode() == restartKeyCode) {
	    LOGGER.info("Level " + currentLevelIndex + " was restarted");
	    startCurrentLevel();
	} else {
	    currentLevelWithMovableCamera.keyPressed(keyEvent);
	}
    }

    /**
     * Lets the current level handle the key release
     *
     * @param keyEvent The key event of the release
     */
    @Override public void keyReleased(final KeyEvent keyEvent) {
	currentLevelWithMovableCamera.keyReleased(keyEvent);
    }

    /**
     * Ticks time forward
     *
     * @param deltaTime The amount of time to tick forward
     */
    @Override public void tick(final double deltaTime) {
	currentLevelWithMovableCamera.tick(deltaTime);
    }

    /**
     * Draws the current level
     *
     * @param g The graphics to draw to
     * @param region The region required to draw to, but not limited by
     */
    @Override public void draw(final Graphics2D g, final RectangularRegion region) {
	currentLevelWithMovableCamera.draw(g, region);
    }

    /**
     * Advances to the next level if one exists
     */
    @Override public void onLevelCompleted() {
	if (currentLevelIndex == levelDefinitions.size() - 1) {
	    LOGGER.info("The last level was completed");
	    return;
	}
	LOGGER.info("Level " + currentLevelIndex + " was completed");
	currentLevelIndex++;
	startCurrentLevel();
    }

    /**
     * Restarts the current level
     */
    @Override public void onLevelFailed() {
	LOGGER.info("Level " + currentLevelIndex + " was failed");
	startCurrentLevel();
    }

    private void startCurrentLevel() {
	final LevelDefinition currentLevelDefinition = levelDefinitions.get(currentLevelIndex);
	final LevelWorld currentLevelWorld = LevelWorld.create(currentLevelDefinition, drawConfig);
	final RectangularRegion camera = currentLevelDefinition.getCamera();

	currentLevelWithMovableCamera = WorldWithMovableCamera.create(currentLevelWorld, camera);
	currentLevelWorld.addListener(this); // We want to be notified when the level is failed or completed
    }
}
