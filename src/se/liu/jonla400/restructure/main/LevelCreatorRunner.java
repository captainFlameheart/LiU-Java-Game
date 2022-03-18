package se.liu.jonla400.restructure.main;

import se.liu.jonla400.restructure.main.levelcreation.LevelBlueprint;
import se.liu.jonla400.restructure.main.levelcreation.LevelCreatorConstructor;
import se.liu.jonla400.restructure.math.Interval;

import javax.swing.*;
import java.awt.*;

public class LevelCreatorRunner
{
    public static void main(String[] args) {
	final LevelCreatorConstructor constructor = new LevelCreatorConstructor();
	final World world = constructor.constructLevelCreator(LevelBlueprint.createEmpty());
	final RectangularRegion camera = RectangularRegion.createFromIntervals(new Interval(-10, 10), new Interval(-10, 10));
	final FilmedWorld filmedWorld = new FilmedWorld(world, camera);

	final Screen screen = Screen.create(filmedWorld);

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
}
