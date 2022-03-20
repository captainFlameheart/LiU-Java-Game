package se.liu.jonla400.restructure.main;

import com.google.gson.JsonSyntaxException;
import se.liu.jonla400.restructure.filehandling.LevelIO;
import se.liu.jonla400.restructure.main.leveldefinition.LevelDefinition;
import se.liu.jonla400.restructure.main.leveldefinition.LevelListener;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameRunner
{
    public static void main(String[] args) {
	final List<LevelDefinition> levelDefinitions = getAtleastOneLevelOrElseQuit();

	final LevelDefinition firstLevelDef = levelDefinitions.get(0);
	final Level firstLevel = Level.createFromDefinition(firstLevelDef);
	final Screen screen = Screen.create(new FilmedWorld(firstLevel, firstLevelDef.getCamera()));
	firstLevel.addListener(new LevelListener()
	{
	    int currentIndex = 0;

	    @Override public void levelCompleted() {
		if (currentIndex == levelDefinitions.size() - 1) {
		    return;
		}
		currentIndex++;
		startCurrentLevel();
	    }

	    @Override public void levelFailed() {
		startCurrentLevel();
	    }

	    private void startCurrentLevel() {
		final LevelDefinition levelDef = levelDefinitions.get(currentIndex);
		final Level level = Level.createFromDefinition(levelDef);
		level.addListener(this);
		screen.setFilmedWorld(new FilmedWorld(level, levelDef.getCamera()));
	    }
	});
	screen.start();
    }

    private static List<LevelDefinition> getAtleastOneLevelOrElseQuit() {
	final Path levelListPath = Paths.get("temp.txt");
	do {
	    final List<LevelDefinition> levelDefs = getLevelsOrElseQuit(levelListPath);
	    if (!levelDefs.isEmpty()) {
		return levelDefs;
	    }
	    verifyWishToRetryOrElseQuit("No levels exist in " + levelListPath, "No levels");
	} while (true);
    }

    private static List<LevelDefinition> getLevelsOrElseQuit(final Path levelListPath) {
	final List<LevelDefinition> levels = new ArrayList<>();

	final List<Path> paths = getLevelPaths();

	for (Path path : paths) {
	    boolean levelLoaded = false;
	    while (!levelLoaded) {
		try {
		    levels.add(LevelIO.loadLevel(path));
		    levelLoaded = true;
		} catch (IOException e) {
		    verifyWishToRetryOrElseQuit("The level file " + path + " could not be read!", "Failed to read level");
		} catch (JsonSyntaxException e) {
		    verifyWishToRetryOrElseQuit("The level file " + path + " contains invalid syntax!", "Invalid syntax");
		}
	    }
	}
	return levels;
    }

    private static List<Path> getLevelPaths() {
	final String[] names = {"level0.txt", "level1.txt", "level2.txt", "level3.txt"};
	final String homeDir = System.getProperty("user.home");
	return Arrays.stream(names).map(name -> Paths.get(homeDir, name)).collect(Collectors.toList());
    }

    private static void verifyWishToRetryOrElseQuit(final String message, final String title) {
	final Object[] options = {"Retry"};
	final int chosenOption = JOptionPane.showOptionDialog(
		null, message, title, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE,
		null, options, options[0]
	);
	if (chosenOption == JOptionPane.CLOSED_OPTION) {
	    System.exit(0);
	}
    }

    private static class LevelStepper
    {
	private List<LevelDefinition> levelDefinitions;
	private int nextLevelIndex;

	private Screen screen;

	private LevelStepper(final List<LevelDefinition> levelDefinitions, final Screen screen) {
	    this.levelDefinitions = levelDefinitions;
	    this.nextLevelIndex = 0;
	    this.screen = screen;
	}

	private void start() {

	}
    }
}
