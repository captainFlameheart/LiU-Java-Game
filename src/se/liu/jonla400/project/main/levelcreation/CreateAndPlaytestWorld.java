package se.liu.jonla400.project.main.levelcreation;

import se.liu.jonla400.project.main.Level;
import se.liu.jonla400.project.main.RectangularRegion;
import se.liu.jonla400.project.main.leveldefinition.LevelDefinition;
import se.liu.jonla400.project.main.temp.FilmedWorld;
import se.liu.jonla400.project.main.temp.MovableCameraWorld;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.nio.file.Path;

public class CreateAndPlaytestWorld implements FilmedWorld
{
    private MovableCameraWorld<LevelCreator> levelCreatorWithMovableCamera;
    private FilmedWorld currentWorld;
    private Path levelFilePath;

    private CreateAndPlaytestWorld(final MovableCameraWorld<LevelCreator> levelCreatorWithMovableCamera, final FilmedWorld currentWorld) {
	this.levelCreatorWithMovableCamera = levelCreatorWithMovableCamera;
	this.currentWorld = currentWorld;
    }

    public static CreateAndPlaytestWorld createFromLevelDef(final LevelDefinition levelDef) {
	final LevelBlueprint blueprint = LevelBlueprint.createFromDefinition(levelDef);
	final LevelCreatorConstructor levelCreatorConstructor = new LevelCreatorConstructor();
	final LevelCreator levelCreator = levelCreatorConstructor.constructLevelCreator(blueprint);

	final RectangularRegion camera = levelDef.getCamera();
	final MovableCameraWorld<LevelCreator> levelCreatorWithMovableCamera = MovableCameraWorld.create(levelCreator, camera);

	return new CreateAndPlaytestWorld(levelCreatorWithMovableCamera, levelCreatorWithMovableCamera);
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

	final int keyCode = keyEvent.getKeyCode();
	if (keyCode == KeyEvent.VK_ENTER) {
	    togglePlaytesting();
	    currentWorld.keyPressed(keyEvent);
	} else if (keyEvent.isControlDown() && keyCode == KeyEvent.VK_S) {
	    System.out.println("Save!");
	}
    }

    private void togglePlaytesting() {
	if (currentWorld.equals(levelCreatorWithMovableCamera)) {
	    final LevelCreator levelCreator = levelCreatorWithMovableCamera.getWorld();
	    final LevelBlueprint levelBlueprint = levelCreator.getBlueprint();
	    final LevelDefinition levelDef = LevelDefinition.createFromBlueprint(levelBlueprint);
	    final Level level = Level.createFromDefinition(levelDef);
	    final RectangularRegion camera = levelDef.getCamera();
	    currentWorld = MovableCameraWorld.create(level, camera);
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
