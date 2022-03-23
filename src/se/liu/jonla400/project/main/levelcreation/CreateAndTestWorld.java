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

    public static CreateAndTestWorld createFromLevelDefinition(final LevelDefinition levelDefinition, final DrawConfiguration drawConfig) {
	final LevelBlueprint blueprint = LevelBlueprint.createFromDefinition(levelDefinition);
	final LevelCreator levelCreator = new LevelCreatorBuilder().buildLevelCreator(blueprint, drawConfig);

	final RectangularRegion camera = levelDefinition.getCamera();
	final WorldWithMovableCamera<LevelCreator> levelCreatorWithMovableCamera = WorldWithMovableCamera.create(levelCreator, camera);

	final int toggleTestingKeyCode = KeyEvent.VK_ENTER;
	return new CreateAndTestWorld(levelCreatorWithMovableCamera, levelCreatorWithMovableCamera, toggleTestingKeyCode);
    }

    public LevelDefinition getLevelDefinition() {
	final LevelBlueprint levelBlueprint = getLevelCreator().getBlueprint();
	return LevelDefinition.createFromBlueprint(levelBlueprint);
    }

    @Override public RectangularRegion getCamera() {
	return currentWorld.getCamera();
    }

    @Override public void updateMousePos(final Vector2D newMousePos) {
	currentWorld.updateMousePos(newMousePos);
    }

    @Override public void mousePressed(final MouseEvent mouseEvent) {
	currentWorld.mousePressed(mouseEvent);
    }

    @Override public void mouseReleased(final MouseEvent mouseEvent) {
	currentWorld.mouseReleased(mouseEvent);
    }

    @Override public void mouseWheelMoved(final MouseWheelEvent mouseWheelEvent) {
	currentWorld.mouseWheelMoved(mouseWheelEvent);
    }

    @Override public void keyPressed(final KeyEvent keyEvent) {
	currentWorld.keyPressed(keyEvent);
	if (keyEvent.getKeyCode() == toggleTestingKeyCode) {
	    toggleTesting();
	    currentWorld.keyPressed(keyEvent);
	}
    }

    private void toggleTesting() {
	if (currentWorld.equals(levelCreatorWithMovableCamera)) {
	    final LevelDefinition levelDefinition = getLevelDefinition();
	    final LevelWorld levelWorld = LevelWorld.create(levelDefinition, getDrawConfig());
	    final RectangularRegion camera = levelDefinition.getCamera();
	    currentWorld = WorldWithMovableCamera.create(levelWorld, camera);
	} else {
	    currentWorld = levelCreatorWithMovableCamera;
	}
    }

    @Override public void keyReleased(final KeyEvent keyEvent) {
	currentWorld.keyReleased(keyEvent);
    }

    @Override public void tick(final double deltaTime) {
	currentWorld.tick(deltaTime);
    }

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
