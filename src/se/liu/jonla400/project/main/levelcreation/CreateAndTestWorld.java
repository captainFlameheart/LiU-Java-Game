package se.liu.jonla400.project.main.levelcreation;

import se.liu.jonla400.project.main.drawing.DrawConfiguration;
import se.liu.jonla400.project.main.game.LevelWorld;
import se.liu.jonla400.project.math.RectangularRegion;
import se.liu.jonla400.project.main.leveldefinition.LevelDefinition;
import se.liu.jonla400.project.main.world.FilmedWorld;
import se.liu.jonla400.project.main.world.WorldWithMovableCamera;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Represents a {@link FilmedWorld} that can toggle between being a world where the user
 * creates a level and a world where the user play-tests the created level
 */
public class CreateAndTestWorld implements FilmedWorld
{
    private WorldWithMovableCamera<LevelCreator> levelCreatorWithMovableCamera;
    private FilmedWorld currentWorld;
    private int toggleTestingKeyCode;

    private CreateAndTestWorld(final WorldWithMovableCamera<LevelCreator> levelCreatorWithMovableCamera, final FilmedWorld currentWorld,
			       final int toggleTestingKeyCode)
    {
	this.levelCreatorWithMovableCamera = levelCreatorWithMovableCamera;
	this.currentWorld = currentWorld;
	this.toggleTestingKeyCode = toggleTestingKeyCode;
    }

    /**
     * Creates a CreateAndTestWorld that starts in create-mode where the given {@link LevelDefinition}
     * specifies the level to start working on. The enter key is used to toggle between creating and testing.
     *
     * @param levelDefinition The level to start working on
     * @param drawConfig Specifies how the ball, line segments and level's center of mass will be drawn
     * @return The created CreateAndTestWorld
     */
    public static CreateAndTestWorld createFromLevelDefinition(final LevelDefinition levelDefinition, final DrawConfiguration drawConfig) {
	final LevelBlueprint blueprint = LevelBlueprint.createFromDefinition(levelDefinition);
	final LevelCreator levelCreator = new LevelCreatorBuilder().buildLevelCreator(blueprint, drawConfig);

	final RectangularRegion camera = levelDefinition.getCamera();
	final WorldWithMovableCamera<LevelCreator> levelCreatorWithMovableCamera = WorldWithMovableCamera.create(levelCreator, camera);

	final int toggleTestingKeyCode = KeyEvent.VK_ENTER;
	return new CreateAndTestWorld(levelCreatorWithMovableCamera, levelCreatorWithMovableCamera, toggleTestingKeyCode);
    }

    /**
     * Returns the current {@link LevelDefinition}
     *
     * @return The current level definition
     */
    public LevelDefinition getLevelDefinition() {
	final LevelBlueprint levelBlueprint = getLevelCreator().getBlueprint();
	return LevelDefinition.createFromBlueprint(levelBlueprint);
    }

    /**
     * Returns the current camera of this world
     *
     * @return The current camera
     */
    @Override public RectangularRegion getCamera() {
	return currentWorld.getCamera();
    }

    /**
     * Updates the considered mouse position
     *
     * @param newMousePos The new mouse position
     */
    @Override public void updateMousePos(final Vector2D newMousePos) {
	currentWorld.updateMousePos(newMousePos);
    }

    /**
     * Reacts to the mouse being pressed, which depends on if this world
     * is in create or test mode
     *
     * @param mouseEvent The information about the mouse press
     */
    @Override public void mousePressed(final MouseEvent mouseEvent) {
	currentWorld.mousePressed(mouseEvent);
    }

    /**
     * Reacts to the mouse being released, which depends on if this world
     * is in create or test mode
     *
     * @param mouseEvent The information about the mouse release
     */
    @Override public void mouseReleased(final MouseEvent mouseEvent) {
	currentWorld.mouseReleased(mouseEvent);
    }

    /**
     * Reacts to mouse wheel being moved
     *
     * @param mouseWheelEvent The information of the mouse wheel movement
     */
    @Override public void mouseWheelMoved(final MouseWheelEvent mouseWheelEvent) {
	currentWorld.mouseWheelMoved(mouseWheelEvent);
    }

    /**
     * Reacts to the key being released. The behaviour depends on the current mode.
     * If the right key is pressed, the current mode is toggled.
     *
     * @param keyEvent The information about the key being pressed
     */
    @Override public void keyPressed(final KeyEvent keyEvent) {
	currentWorld.keyPressed(keyEvent);
	if (keyEvent.getKeyCode() == toggleTestingKeyCode) {
	    toggleTesting();
	    currentWorld.keyPressed(keyEvent); // The world has changed, so tell the new world about the key press
	}
    }

    private void toggleTesting() {
	if (currentWorld.equals(levelCreatorWithMovableCamera)) {
	    // Instantiate the level and set it as the current world (with a camera)
	    final LevelDefinition levelDefinition = getLevelDefinition();
	    final LevelWorld levelWorld = LevelWorld.create(levelDefinition, getDrawConfig());
	    final RectangularRegion camera = levelDefinition.getCamera();
	    currentWorld = WorldWithMovableCamera.create(levelWorld, camera);
	} else {
	    currentWorld = levelCreatorWithMovableCamera;
	}
    }

    /**
     * Reacts to the key being release, which depends on the current mode
     *
     * @param keyEvent The information about the key release
     */
    @Override public void keyReleased(final KeyEvent keyEvent) {
	currentWorld.keyReleased(keyEvent);
    }

    /**
     * Ticks time forward, which depends on the current mode
     *
     * @param deltaTime The amount of time to tick forward
     */
    @Override public void tick(final double deltaTime) {
	currentWorld.tick(deltaTime);
    }

    /**
     * Draws this world onto the {@link Graphics2D} object at the given region.
     * This method might draw outside the region too.
     *
     * @param g The graphics object to draw to
     * @param region The region required to draw to, but not limited by
     */
    @Override public void draw(final Graphics2D g, final RectangularRegion region) {
	currentWorld.draw(g, region);
    }

    private DrawConfiguration getDrawConfig() {
	return getLevelCreator().getDrawConfig();
    }

    private LevelCreator getLevelCreator() {
	return levelCreatorWithMovableCamera.getWorld();
    }
}
