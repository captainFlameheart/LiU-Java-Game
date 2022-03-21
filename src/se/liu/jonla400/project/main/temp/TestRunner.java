package se.liu.jonla400.project.main.temp;

import com.google.gson.JsonSyntaxException;
import se.liu.jonla400.project.main.filehandling.LevelIO;
import se.liu.jonla400.project.main.levelcreation.CreateAndPlaytestWorld;
import se.liu.jonla400.project.main.leveldefinition.LevelDefinition;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestRunner
{
    public static void main(String[] args) {
	final LevelFile levelFile = askUserForLevelFile();
	final Path path = levelFile.path;
	final LevelDefinition levelDef = levelFile.levelDef;

	final FilmedWorld world = CreateAndPlaytestWorld.createFromLevelDef(levelDef);
	WorldGUI.createAndStart(world);
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