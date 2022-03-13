package se.liu.jonla400.project.main;

import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;

public class Game
{
    private LevelDefinition[] levelDefinitions;
    private int currentIndex;
    private Level currentLevel;

    private Game(final LevelDefinition[] levelDefinitions, final int currentIndex, final Level currentLevel) {
        this.levelDefinitions = levelDefinitions;
        this.currentIndex = currentIndex;
        this.currentLevel = currentLevel;
    }

    public static Game createStartingWithFirstLevel(final LevelDefinition... levelDefinitions) {
        if (levelDefinitions.length == 0) {
            throw new IllegalArgumentException("No levels");
        }
        return new Game(levelDefinitions.clone(), 0, Level.createFromDefinition(levelDefinitions[0]));
    }

    public void tick(final double deltaTime) {
        final LevelTimeStepResult timeStepResult = currentLevel.tick(deltaTime);
        if (timeStepResult.levelIsCompleted()) {
            goToNextLevel();
        }
    }

    public void setCursorPos(final Vector2D pos) {
        currentLevel.setCursorPos(pos);
    }

    public void startTranslating() {
        currentLevel.startTranslating();
    }

    public void stopTranslating() {
        currentLevel.stopTranslating();
    }

    public void startRotating() {
        currentLevel.startRotating();
    }

    public void stopRotating() {
        currentLevel.stopRotating();
    }

    public DrawRegion getMinDrawRegion() {
        return currentLevel.getMinDrawRegion();
    }

    public void draw(final Graphics2D g, final DrawRegion region) {
        currentLevel.draw(g, region);
    }

    private void goToNextLevel() {
        currentIndex = (currentIndex + 1) % levelDefinitions.length;
        currentLevel = Level.createFromDefinition(levelDefinitions[currentIndex]);
    }
}
