package se.liu.jonla400.project.main.game;

import com.google.gson.JsonSyntaxException;
import se.liu.jonla400.project.main.drawing.DrawConfiguration;
import se.liu.jonla400.project.main.filehandling.LevelIO;
import se.liu.jonla400.project.main.leveldefinition.LevelDefinition;
import se.liu.jonla400.project.main.world.WorldGUI;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GameRunner
{
    private final static String[] LEVEL_RESOURCE_NAMES;

    static {
	final String[] levelNames = {
		"level0", "level1", "level2", "level3", "level4", "level5", "level6", "level7", "level8", "level9"
	};
	LEVEL_RESOURCE_NAMES = new String[levelNames.length];
	for (int i = 0; i < levelNames.length; i++) {
	    LEVEL_RESOURCE_NAMES[i] = "levels/" + levelNames[i] + ".json";
	}
    }

    public static void run(final DrawConfiguration drawConfig) {
	final List<LevelDefinition> levelDefinitions = getAtleastOneLevelOrElseQuit();
	final GameWorld gameWorld = GameWorld.createAndStartWithFirstLevel(drawConfig, levelDefinitions);
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

	for (String levelResourceName : LEVEL_RESOURCE_NAMES) {
	    boolean levelLoaded = false;
	    while (!levelLoaded) {
		try {
		    levels.add(LevelIO.loadLevelFromResource(levelResourceName));
		    levelLoaded = true;
		} catch (IOException ignored) {
		    verifyWishToRetryOrElseQuit("The level " + levelResourceName + " could not be read!", "Failed to read level");
		} catch (JsonSyntaxException ignored) {
		    verifyWishToRetryOrElseQuit("The level " + levelResourceName + " contains invalid syntax!", "Invalid syntax");
		}
	    }
	}
	return levels;
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
