package se.liu.jonla400.restructure.main.levelcreation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import se.liu.jonla400.restructure.constants.CameraConstants;
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
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class LevelCreatorRunner
{
    public static void main(String[] args) {
	final LevelFile levelFile = askUserForLevelFile();
	final Path path = levelFile.path;
	final Optional<LevelDefinition> levelDef = levelFile.levelDef;

	final LevelBlueprint levelBlueprint;
	final RectangularRegion camera;
	if (levelDef.isPresent()) {
	    final LevelDefinition def = levelDef.get();
	    levelBlueprint = LevelBlueprint.createFromDefinition(def);
	    camera = def.getCamera();
	} else {
	    levelBlueprint = LevelBlueprint.createEmpty();
	    camera = CameraConstants.getDefaultCamera();
	}

	final LevelCreatorConstructor constructor = new LevelCreatorConstructor();
	final LevelCreator levelCreator = constructor.constructLevelCreator(levelBlueprint);

	final FilmedWorld filmedWorld = new FilmedWorld(levelCreator, camera);
	final Screen screen = Screen.create(filmedWorld);

	screen.addKeyListener(new KeyAdapter()
	{
	    boolean playTesting = false;

	    @Override public void keyPressed(final KeyEvent e) {
		final int keyCode = e.getKeyCode();
		if (keyCode == KeyEvent.VK_ENTER) {
		    togglePlayTesting();
		} else if (e.isControlDown() && keyCode == KeyEvent.VK_S) {
		    save();
		}
	    }

	    private void togglePlayTesting() {
		if (playTesting) {
		    screen.setFilmedWorld(new FilmedWorld(levelCreator, levelCreator.getLevelCamera()));
		    playTesting = false;
		} else {
		    final LevelBlueprint currentLevelBlueprint = levelCreator.getBlueprint();
		    final LevelDefinition currentLevelDef = LevelDefinition.createFromBlueprint(currentLevelBlueprint);
		    final Level level = Level.createFromDefinition(currentLevelDef);
		    screen.setFilmedWorld(new FilmedWorld(level, currentLevelDef.getCamera()));
		    playTesting = true;
		}
	    }

	    private void save() {
		final LevelBlueprint currentLevelBlueprint = levelCreator.getBlueprint();
		final LevelDefinition currentLevelDef = LevelDefinition.createFromBlueprint(currentLevelBlueprint);
		saveLevelOrMessageError(currentLevelDef, path);
	    }
	});

	startScreen(screen);
    }

    private static void startScreen(final Screen screen) {
	final JFrame frame = new JFrame("Test!");
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	frame.setContentPane(screen);
	frame.setSize(400, 400);
	frame.setLocationRelativeTo(null);
	frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	frame.setVisible(true);

	final int tickRate = 90;
	final double deltaSecondsPerTick = 1.0 / tickRate;
	final int deltaMillisecondsPerTick = 1000 / tickRate;
	final Timer tickTimer = new Timer(deltaMillisecondsPerTick, e -> screen.tick(deltaSecondsPerTick));
	tickTimer.setCoalesce(true);
	tickTimer.start();
    }

    private static LevelFile askUserForLevelFile() {
	String pathString = "";
	do {
	    pathString = JOptionPane.showInputDialog("Please enter the path to the level file (new path = new level)", pathString);
	    if (pathString == null) {
		System.exit(0);
	    }
	    final Path path = Paths.get(System.getProperty("user.home"), pathString);

	    try {
		Optional<LevelDefinition> levelDef = readLevelDefFromFile(path);
		return new LevelFile(path, levelDef);
	    } catch (IOException ignored) {
		showErrorMessage("The file could not be read!", "File error");
	    } catch (JsonSyntaxException ignored) {
		showErrorMessage("The file containts invalid syntax!", "Syntax error");
	    }
	} while (true);
    }

    private static void saveLevelOrMessageError(final LevelDefinition levelDef, final Path path) {
	Gson gson = new GsonBuilder().setPrettyPrinting().create();
	final String defAsJson = gson.toJson(levelDef);
	try {
	    Files.writeString(path, defAsJson);
	} catch (IOException ignored) {
	    showErrorMessage("Could not save the level to the file", "File error");
	}
    }

    private static void showErrorMessage(final String message, final String title) {
	JOptionPane.showMessageDialog(
		null, message,
		title, JOptionPane.ERROR_MESSAGE);
    }


    private static Optional<LevelDefinition> readLevelDefFromFile(final Path path) throws IOException, JsonSyntaxException {
	try {
	    final String levelDefAsJson = Files.readString(path);
	    final Gson gson = new GsonBuilder().create();
	    return Optional.ofNullable(gson.fromJson(levelDefAsJson, LevelDefinition.class));
	} catch (NoSuchFileException ignored) {
	    return Optional.empty();
	}
    }

    private static class LevelFile
    {
	private Path path;
	private Optional<LevelDefinition> levelDef;

	private LevelFile(final Path path, final Optional<LevelDefinition> levelDef) {
	    this.path = path;
	    this.levelDef = levelDef;
	}
    }
}
