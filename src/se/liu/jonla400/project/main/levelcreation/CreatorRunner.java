package se.liu.jonla400.project.main.levelcreation;

import com.google.gson.JsonSyntaxException;
import se.liu.jonla400.project.main.drawing.DrawConfiguration;
import se.liu.jonla400.project.main.filehandling.LevelIO;
import se.liu.jonla400.project.main.leveldefinition.LevelDefinition;
import se.liu.jonla400.project.main.world.WorldGUI;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Represents the procedure used to start creating a level. This is done by first asking the
 * user what level file to load from and save to. If the level file doesn't exist, a new level
 * is created. When the user presses CTRL+S the level is saved to the file.
 */
public class CreatorRunner
{
    public static void run(final DrawConfiguration drawConfig) {
	final LevelFile levelFile = askUserForLevelFile();
	final Path path = levelFile.path;
	final LevelDefinition levelDefinition = levelFile.levelDefinition;

	final CreateAndTestWorld createAndTestWorld = CreateAndTestWorld.createFromLevelDefinition(levelDefinition, drawConfig);
	final WorldGUI gui = WorldGUI.createFor(createAndTestWorld);
	enableSaving(gui, createAndTestWorld, path); // CTRL+S to save to the path
	gui.start();
    }

    private static LevelFile askUserForLevelFile() {
	String pathString = "";
	do {
	    pathString = JOptionPane.showInputDialog("Please enter the path to the level file (new path = new level)", pathString);
	    if (pathString == null) {
		// The user selected the close option, exit the program
		System.exit(0);
	    }

	    final Path path = Paths.get(pathString);
	    if (Files.notExists(path)) {
		// Since the file does not exist it implicitly defines an empty level
		return new LevelFile(path, LevelDefinition.createEmpty());
	    }
	    try {
		final LevelDefinition levelDefinition = LevelIO.loadLevelFromFile(path);
		return new LevelFile(path, levelDefinition);
	    } catch (IOException e) {
		e.printStackTrace();
		showErrorMessage("The file could not be read!", "File error");
	    } catch (JsonSyntaxException e) {
		e.printStackTrace();
		showErrorMessage("The file containts invalid syntax!", "Syntax error");
	    }
	} while (true); // Retry until loading the file is successful, or the user quits the program
    }

    private static void enableSaving(final WorldGUI gui, final CreateAndTestWorld createAndTestWorld, final Path path) {
	gui.addKeyListener(new KeyAdapter()
	{
	    @Override public void keyPressed(final KeyEvent e) {
		if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
		    saveLevelOrMessageError(createAndTestWorld.getLevelDefinition(), path);
		}
	    }
	});
    }

    private static void saveLevelOrMessageError(final LevelDefinition levelDefinition, final Path path) {
	try {
	    LevelIO.saveLevelToFile(levelDefinition, path);
	    System.out.println("Level saved at " + path);
	} catch (IOException e) {
	    e.printStackTrace();
	    showErrorMessage("Could not save the level at " + path, "Save error");
	}
    }

    private static void showErrorMessage(final String message, final String title) {
	JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private static class LevelFile
    {
	private Path path;
	private LevelDefinition levelDefinition;

	private LevelFile(final Path path, final LevelDefinition levelDefinition) {
	    this.path = path;
	    this.levelDefinition = levelDefinition;
	}
    }
}