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

public class CreatorRunner
{
    public static void run(final DrawConfiguration drawConfig) {
	final LevelFile levelFile = askUserForLevelFile();
	final Path path = levelFile.path;
	final LevelDefinition levelDef = levelFile.levelDef;

	final CreateAndPlaytestWorld createAndPlaytestWorld = CreateAndPlaytestWorld.createFromLevelDef(levelDef, drawConfig);
	final WorldGUI gui = WorldGUI.createFor(createAndPlaytestWorld);
	gui.addKeyListener(new KeyAdapter()
	{
	    @Override public void keyPressed(final KeyEvent e) {
		if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_S) {
		    saveLevelOrMessageError(createAndPlaytestWorld.getLevelDef(), path);
		}
	    }
	});
	gui.start();
    }

    private static LevelFile askUserForLevelFile() {
	String pathString = "";
	do {
	    pathString = JOptionPane.showInputDialog("Please enter the path to the level file (new path = new level)", pathString);
	    if (pathString == null) {
		System.exit(0);
	    }

	    final Path path = Paths.get(pathString);
	    if (Files.notExists(path)) {
		return new LevelFile(path, LevelDefinition.createEmpty());
	    }
	    try {
		LevelDefinition levelDef = LevelIO.loadLevelFromFile(path);
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
	    LevelIO.saveLevelToFile(levelDef, path);
	    System.out.println("Level saved at " + path);
	} catch (IOException ignored) {
	    showErrorMessage("Could not save the level at " + path, "Save error");
	}
    }

    private static void showErrorMessage(final String message, final String title) {
	JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
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