package se.liu.jonla400.project.main.game;

import com.google.gson.JsonSyntaxException;
import se.liu.jonla400.project.main.filehandling.LevelIO;
import se.liu.jonla400.project.main.leveldefinition.LevelDefinition;
import se.liu.jonla400.project.main.world.WorldGUI;

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
	final GameWorld gameWorld = GameWorld.createAndStartWithFirstLevel(levelDefinitions);
	final WorldGUI gui = WorldGUI.createFor(gameWorld);
	gui.start();
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
		} catch (IOException ignored) {
		    verifyWishToRetryOrElseQuit("The level file " + path + " could not be read!", "Failed to read level");
		} catch (JsonSyntaxException ignored) {
		    verifyWishToRetryOrElseQuit("The level file " + path + " contains invalid syntax!", "Invalid syntax");
		}
	    }
	}
	return levels;
    }

    private static List<Path> getLevelPaths() {
	final String[] names = {"level0.txt", "level1.txt", "level2.txt", "level3.txt", "level4.txt"};
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
}
