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

public class GameWorld implements FilmedWorld, LevelListener
{
    private DrawConfiguration drawConfig;
    private List<LevelDefinition> levelDefinitions;
    private int currentLevelIndex;
    private WorldWithMovableCamera<LevelWorld> currentLevelWithMovableCamera;
    private int restartKeyCode;

    public GameWorld(final DrawConfiguration drawConfig, final List<LevelDefinition> levelDefinitions, final int currentLevelIndex,
		     final WorldWithMovableCamera<LevelWorld> currentLevelWithMovableCamera, final int restartKeyCode)
    {
	this.drawConfig = drawConfig;
	this.levelDefinitions = levelDefinitions;
	this.currentLevelIndex = currentLevelIndex;
	this.currentLevelWithMovableCamera = currentLevelWithMovableCamera;
	this.restartKeyCode = restartKeyCode;
    }

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
	if (keyEvent.getKeyCode() == restartKeyCode) {
	    startCurrentLevel();
	} else {
	    currentLevelWithMovableCamera.keyPressed(keyEvent);
	}
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
	final LevelWorld currentLevelWorld = LevelWorld.create(currentLevelDef, drawConfig);
	final RectangularRegion camera = currentLevelDef.getCamera();

	currentLevelWithMovableCamera = WorldWithMovableCamera.create(currentLevelWorld, camera);
	currentLevelWorld.addListener(this);
    }
}
