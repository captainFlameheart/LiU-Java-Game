package se.liu.jonla400.restructure.main.levelcreation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class LevelCreatorRunner
{
    public static void main(String[] args) {
	final Path path = Paths.get("C:tmp\\foo");

	final Optional<LevelDefinition> levelDef = getLevelDefFromFile(path);
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

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		System.out.println(gson.toJson(currentLevelDef));
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

    private static Optional<LevelDefinition> getLevelDefFromFile(Path path) {
	return Optional.empty();
    }
}
