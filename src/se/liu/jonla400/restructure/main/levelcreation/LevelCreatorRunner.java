package se.liu.jonla400.restructure.main.levelcreation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import se.liu.jonla400.restructure.constants.CameraConstants;
import se.liu.jonla400.restructure.filehandling.LevelIO;
import se.liu.jonla400.restructure.main.FilmedWorld;
import se.liu.jonla400.restructure.main.Level;
import se.liu.jonla400.restructure.main.leveldefinition.LevelDefinition;
import se.liu.jonla400.restructure.main.RectangularRegion;
import se.liu.jonla400.restructure.main.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LevelCreatorRunner
{
    public static void main(String[] args) {
	final LevelFile levelFile = askUserForLevelFile();
	final Path path = levelFile.path;
	final LevelDefinition levelDef = levelFile.levelDef;

	final LevelBlueprint levelBlueprint = LevelBlueprint.createFromDefinition(levelDef);
	final RectangularRegion camera = levelDef.getCamera();

	final LevelCreatorConstructor constructor = new LevelCreatorConstructor();
	final LevelCreator levelCreator = constructor.constructLevelCreator(levelBlueprint);

	final Screen screen = Screen.create(new FilmedWorld(levelCreator, camera));

	final CreateToPlayToggler createToPlayToggler = new CreateToPlayToggler(levelCreator, screen, true);
	final Saver saver = new Saver(levelCreator, path);
	screen.addKeyListener(new KeyAdapter()
	{
	    @Override public void keyPressed(final KeyEvent e) {
		final int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER) {
		    createToPlayToggler.toggle();
		} else if (e.isControlDown() && keyCode == KeyEvent.VK_S) {
		    saver.save();
		}
	    }
	});
	screen.start();
    }

    private static LevelFile askUserForLevelFile() {
	String pathString = "";
	do {
	    pathString = JOptionPane.showInputDialog("Please enter the path to the level file (new path = new level)", pathString);
	    if (pathString == null) {
		System.exit(0);
	    }

	    final Path path = Paths.get(System.getProperty("user.home"), pathString);
	    if (Files.notExists(path)) {
		return new LevelFile(path, LevelDefinition.createEmpty());
	    }
	    try {
		LevelDefinition levelDef = LevelIO.loadLevel(path);
		return new LevelFile(path, levelDef);
	    } catch (IOException ignored) {
		showErrorMessage("The file could not be read!", "File error");
	    } catch (JsonSyntaxException ignored) {
		showErrorMessage("The file containts invalid syntax!", "Syntax error");
	    }
	} while (true);
    }

    private static void saveLevelOrMessageError(final LevelDefinition levelDef, final Path path) {
	try {
	    LevelIO.saveLevel(levelDef, path);
	} catch (IOException ignored) {
	    showErrorMessage("Could not save the level to the file", "Save error");
	}
    }

    private static void showErrorMessage(final String message, final String title) {
	JOptionPane.showMessageDialog(
		null, message,
		title, JOptionPane.ERROR_MESSAGE);
    }

    private static class CreateToPlayToggler
    {
	private LevelCreator levelCreator;
	private Screen screen;
	private boolean playNext;

	private CreateToPlayToggler(final LevelCreator levelCreator, final Screen screen, final boolean playNext) {
	    this.levelCreator = levelCreator;
	    this.screen = screen;
	    this.playNext = playNext;
	}

	private void toggle() {
	    if (playNext) {
		final LevelBlueprint blueprint = levelCreator.getBlueprint();
		final LevelDefinition levelDef = LevelDefinition.createFromBlueprint(blueprint);
		final Level level = Level.createFromDefinition(levelDef);
		screen.setFilmedWorld(new FilmedWorld(level, levelDef.getCamera()));
		playNext = false;
	    } else {
		screen.setFilmedWorld(new FilmedWorld(levelCreator, levelCreator.getLevelCamera()));
		playNext = true;
	    }
	}
    }

    private static class Saver
    {
	private LevelCreator levelCreator;
	private Path path;

	private Saver(final LevelCreator levelCreator, final Path path) {
	    this.levelCreator = levelCreator;
	    this.path = path;
	}

	private void save() {
	    final LevelBlueprint blueprint = levelCreator.getBlueprint();
	    final LevelDefinition levelDef = LevelDefinition.createFromBlueprint(blueprint);
	    try {
		LevelIO.saveLevel(levelDef, path);
	    } catch (IOException ignored) {
		showErrorMessage("Could not save the level to the file", "Save error");
	    }
	}
    }

    private static class LevelFile
    {
	private Path path;
	private LevelDefinition levelDef;

	private LevelFile(final Path path, final LevelDefinition levelDef) {
	    this.path = path;
	    this.levelDef = levelDef;
	}
    }
}
