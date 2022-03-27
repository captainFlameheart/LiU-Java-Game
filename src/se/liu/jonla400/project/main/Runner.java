package se.liu.jonla400.project.main;

import se.liu.jonla400.project.main.drawing.BallDrawer;
import se.liu.jonla400.project.main.drawing.CrossDrawer;
import se.liu.jonla400.project.main.drawing.DrawConfiguration;
import se.liu.jonla400.project.main.drawing.Drawer;
import se.liu.jonla400.project.main.drawing.LineSegmentDrawer;
import se.liu.jonla400.project.main.game.GameRunner;
import se.liu.jonla400.project.main.levelcreation.CreatorRunner;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Represents the main runner of the program. If dev mode is turned on, a choise
 * between starting the level creator or actual game is displayed. Otherwise the
 * game is started automatically.
 */
public class Runner
{
    private final static boolean IN_DEV_MODE = true;

    public static void main(String[] args) {
	tryToConfigureLogging();
	final DrawConfiguration drawConfig = createDrawConfig();

	if (IN_DEV_MODE) {
	    final Object[] options = {"Play", "Create"};
	    final int defaultOptionIndex = 0;

	    final int chosenOption = JOptionPane.showOptionDialog(
		    null, "Do you want to play or create?", "Startup choice", JOptionPane.YES_NO_OPTION,
		    JOptionPane.QUESTION_MESSAGE, null, options, options[defaultOptionIndex]);

	    switch (chosenOption) {
		case JOptionPane.YES_OPTION -> GameRunner.run(drawConfig);
		case JOptionPane.NO_OPTION -> CreatorRunner.run(drawConfig);
		default -> System.exit(0);
	    }
	} else {
	    GameRunner.run(drawConfig);
	}
    }

    private static void tryToConfigureLogging() {
	final Logger logger = Logger.getLogger(Runner.class.getName());
	final URL logConfig = ClassLoader.getSystemResource("mylogging.properties");
	if (logConfig == null) {
	    logger.severe("Unable to access the logging configuration!");
	    return;
	}
	try (final InputStream logConfigStream = logConfig.openStream()) {
	    LogManager.getLogManager().readConfiguration(logConfigStream);
	} catch (IOException e) {
	    logger.log(Level.SEVERE, "Could not read from the logging configuration!", e);
	}
    }

    private static DrawConfiguration createDrawConfig() {
	final BallDrawer ballDrawer = BallDrawer.createDefault();
	final LineSegmentDrawer lineSegmentDrawer = LineSegmentDrawer.createDefault();

	final float centerOfMassStrokeWidth = 0.1f;
	final float centerOfMassRadius = 1;
	final Drawer centerOfMassDrawer = CrossDrawer.create(Color.MAGENTA, centerOfMassStrokeWidth).setRadius(centerOfMassRadius);

	return new DrawConfiguration(ballDrawer, lineSegmentDrawer, centerOfMassDrawer);
    }
}
