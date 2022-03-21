package se.liu.jonla400.project.main;

import se.liu.jonla400.project.main.leveldefinition.LevelDefinition;
import se.liu.jonla400.project.main.temp.FilmedWorld;
import se.liu.jonla400.project.main.temp.MovableCameraWorld;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

public class GameWorld implements FilmedWorld, LevelListener
{
    private List<LevelDefinition> levelDefinitions;
    private int currentLevelIndex;
    private MovableCameraWorld<LevelWorld> currentLevelWithMovableCamera;

    private GameWorld(final List<LevelDefinition> levelDefinitions, final int currentLevelIndex,
		      final MovableCameraWorld<LevelWorld> currentLevelWithMovableCamera) {
	this.levelDefinitions = levelDefinitions;
	this.currentLevelIndex = currentLevelIndex;
	this.currentLevelWithMovableCamera = currentLevelWithMovableCamera;
    }

    public static GameWorld createAndStartWithFirstLevel(final List<LevelDefinition> levelDefinitions) {
	if (levelDefinitions.isEmpty()) {
	    throw new IllegalArgumentException("No levels");
	}

	final int currentLevelIndex = 0;
	final GameWorld gameWorld = new GameWorld(new ArrayList<>(levelDefinitions), currentLevelIndex, null);
	gameWorld.startCurrentLevel();
	return gameWorld;
    }

    @Override public RectangularRegion getCamera() {
	return currentLevelWithMovableCamera.getCamera();
    }

    @Override public void updateMousePos(final Vector2D newMousePos) {
	currentLevelWithMovableCamera.updateMousePos(newMousePos);
    }

    @Override public void mousePressed(final MouseEvent mouseEvent) {
	currentLevelWithMovableCamera.mousePressed(mouseEvent);
    }

    @Override public void mouseReleased(final MouseEvent mouseEvent) {
	currentLevelWithMovableCamera.mouseReleased(mouseEvent);
    }

    @Override public void mouseWheelMoved(final MouseWheelEvent mouseWheelEvent) {
	currentLevelWithMovableCamera.mouseWheelMoved(mouseWheelEvent);
    }

    @Override public void keyPressed(final KeyEvent keyEvent) {
	currentLevelWithMovableCamera.keyPressed(keyEvent);
    }

    @Override public void keyReleased(final KeyEvent keyEvent) {
	currentLevelWithMovableCamera.keyReleased(keyEvent);
    }

    @Override public void tick(final double deltaTime) {
	currentLevelWithMovableCamera.tick(deltaTime);
    }

    @Override public void draw(final Graphics2D g, final RectangularRegion region) {
	currentLevelWithMovableCamera.draw(g, region);
    }

    @Override public void levelCompleted() {
	if (currentLevelIndex == levelDefinitions.size() - 1) {
	    return;
	}
	currentLevelIndex++;
	startCurrentLevel();
    }

    @Override public void levelFailed() {
	startCurrentLevel();
    }

    private void startCurrentLevel() {
	final LevelDefinition currentLevelDef = levelDefinitions.get(currentLevelIndex);
	final LevelWorld currentLevelWorld = LevelWorld.createFromDefinition(currentLevelDef);
	final RectangularRegion camera = currentLevelDef.getCamera();

	currentLevelWithMovableCamera = MovableCameraWorld.create(currentLevelWorld, camera);
	currentLevelWorld.addListener(this);
    }
}
