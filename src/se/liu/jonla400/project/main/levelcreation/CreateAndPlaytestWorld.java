package se.liu.jonla400.project.main.levelcreation;

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

public class CreateAndPlaytestWorld implements FilmedWorld
{
    private WorldWithMovableCamera<LevelCreator> levelCreatorWithMovableCamera;
    private FilmedWorld currentWorld;
    private int togglePlaytestingKeyCode;

    private CreateAndPlaytestWorld(final WorldWithMovableCamera<LevelCreator> levelCreatorWithMovableCamera, final FilmedWorld currentWorld,
				   final int togglePlaytestingKeyCode)
    {
	this.levelCreatorWithMovableCamera = levelCreatorWithMovableCamera;
	this.currentWorld = currentWorld;
	this.togglePlaytestingKeyCode = togglePlaytestingKeyCode;
    }

    public static CreateAndPlaytestWorld createFromLevelDef(final LevelDefinition levelDef) {
	final LevelBlueprint blueprint = LevelBlueprint.createFromDefinition(levelDef);
	final LevelCreator levelCreator = new LevelCreatorBuilder().buildLevelCreator(blueprint);

	final RectangularRegion camera = levelDef.getCamera();
	final WorldWithMovableCamera<LevelCreator> levelCreatorWithMovableCamera = WorldWithMovableCamera.create(levelCreator, camera);

	final int togglePlaytestingMouseButton = KeyEvent.VK_ENTER;
	return new CreateAndPlaytestWorld(levelCreatorWithMovableCamera, levelCreatorWithMovableCamera, togglePlaytestingMouseButton);
    }

    public LevelDefinition getLevelDef() {
	final LevelBlueprint levelBlueprint = levelCreatorWithMovableCamera.getWorld().getBlueprint();
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
	if (keyEvent.getKeyCode() == togglePlaytestingKeyCode) {
	    togglePlaytesting();
	    currentWorld.keyPressed(keyEvent);
	}
    }

    private void togglePlaytesting() {
	if (currentWorld.equals(levelCreatorWithMovableCamera)) {
	    final LevelDefinition levelDef = getLevelDef();
	    final LevelWorld levelWorld = LevelWorld.createFromDef(levelDef);
	    final RectangularRegion camera = levelDef.getCamera();
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
}
