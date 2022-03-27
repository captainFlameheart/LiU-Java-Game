package se.liu.jonla400.project.main.game;

import com.google.gson.JsonSyntaxException;
import se.liu.jonla400.project.main.drawing.DrawConfiguration;
import se.liu.jonla400.project.main.filehandling.LevelIO;
import se.liu.jonla400.project.main.leveldefinition.LevelDefinition;
import se.liu.jonla400.project.main.world.WorldGUI;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents the procedure that starts the main game with a sequence of levels for the
 * player to complete
 */
public class GameRunner
{
    private final static Logger LOGGER = Logger.getLogger(GameRunner.class.getName());

    /**
     * Starts the game
     *
     * @param drawConfig Defines how the ball, line segments and the level's center of mass are to be drawn
     */
    public static void run(final DrawConfiguration drawConfig) {
	final List<LevelDefinition> levelDefinitions = getLevelsOrElseQuit();
	final GameWorld gameWorld = GameWorld.createAndStartWithFirstLevel(drawConfig, levelDefinitions);
	final WorldGUI gui = WorldGUI.createFor(gameWorld);
	gui.start();
    }

    /**
     * Tries to fetch the levels of the game, but if unsuccessful quits the program
     *
     * @return The list of {@link LevelDefinition} for the game
     */
    private static List<LevelDefinition> getLevelsOrElseQuit() {
	final List<LevelDefinition> levels = new ArrayList<>();

	for (String levelResourceName : getLevelResourceNames()) {
	    boolean levelLoaded = false;
	    while (!levelLoaded) {
		try {
		    levels.add(LevelIO.loadLevelFromResource(levelResourceName));
		    levelLoaded = true;
		} catch (IOException e) {
		    // Logging is done in the called method
		    reportErrorAndPossiblyQuit("The level \"" + levelResourceName + "\" could not be read!",
					       "Failed to read level", e);
		} catch (JsonSyntaxException e) {
		    // Logging is done in the called method
		    reportErrorAndPossiblyQuit("The level \"" + levelResourceName + "\" contains invalid syntax!",
					       "Invalid syntax", e);
		}
	    }
	}
	return levels;
    }

    private static String[] getLevelResourceNames() {
	// Declare each level and their order by identifiying them with their names
	final String[] levelNames = {
		"level0", "level1", "level2", "level3", "level4", "level5", "level6", "level7", "level8", "level9"
	};
	// Convert the level names to full resource names
	final String[] levelResourceNames = new String[levelNames.length];
	for (int i = 0; i < levelNames.length; i++) {
	    levelResourceNames[i] = "levels/" + levelNames[i] + ".json"; // (Is a resource URL)
	}
	return levelResourceNames;
    }

    private static void reportErrorAndPossiblyQuit(final String message, final String title, final Throwable thrown) {
	LOGGER.log(Level.SEVERE, message, thrown);
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
