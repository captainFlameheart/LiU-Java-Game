package se.liu.jonla400.restructure.main;

import se.liu.jonla400.restructure.main.levelcreation.LevelBlueprint;
import se.liu.jonla400.restructure.main.levelcreation.LevelCreator;
import se.liu.jonla400.restructure.main.levelcreation.LevelCreatorConstructor;
import se.liu.jonla400.restructure.math.Interval;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Runner
{
    public static void main(String[] args) {
	final LevelCreatorConstructor constructor = new LevelCreatorConstructor();
	final LevelCreator world = constructor.constructLevelCreator(LevelBlueprint.createEmpty());
	final RectangularRegion camera = RectangularRegion.createFromIntervals(new Interval(-10, 10), new Interval(-10, 10));
	final FilmedWorld filmedWorld = new FilmedWorld(world, camera);

	final Screen screen = Screen.create(filmedWorld);
	screen.addKeyListener(new Toggler(screen, world));

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

    private static class Toggler extends KeyAdapter {
	private Screen screen;
	private LevelCreator levelCreator;
	private Level level;

	private Toggler(final Screen screen, final LevelCreator levelCreator) {
	    this.screen = screen;
	    this.levelCreator = levelCreator;
	    this.level = null;
	}

	@Override public void keyPressed(final KeyEvent e) {
	    if (e.getKeyCode() == KeyEvent.VK_ENTER && level == null) {
		    final LevelBlueprint blueprint = levelCreator.getBlueprint();
		    final LevelDefinition def = LevelDefinition.createFromBlueprint(blueprint);
		    level = Level.createFromDefinition(def);
		    screen.setFilmedWorld(new FilmedWorld(level, def.getCamera()));
	    }
	    if (e.getKeyCode() == KeyEvent.VK_SPACE && level != null) {
		screen.setFilmedWorld(new FilmedWorld(levelCreator, levelCreator.getLevelCamera()));
		level = null;
	    }
	}
    }
}
