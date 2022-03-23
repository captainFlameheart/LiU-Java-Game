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

public class Runner
{
    private final static boolean IN_DEV_MODE = true;

    public static void main(String[] args) {
	final DrawConfiguration drawConfig = createDrawConfig();

	if (IN_DEV_MODE) {
	    final Object[] options = new Object[2];
	    final int playOptionIndex = 0;
	    final int createOptionIndex = 1;

	    options[playOptionIndex] = "Play";
	    options[createOptionIndex] = "Create";

	    final int chosenOption = JOptionPane.showOptionDialog(
		    null, "Do you want to play or create?", "Startup choice", JOptionPane.YES_NO_OPTION,
		    JOptionPane.QUESTION_MESSAGE, null, options, options[playOptionIndex]);

	    switch (chosenOption) {
		case playOptionIndex -> GameRunner.run(drawConfig);
		case createOptionIndex -> CreatorRunner.run(drawConfig);
		default -> System.exit(0);
	    }
	} else {
	    GameRunner.run(drawConfig);
	}
    }

    public static DrawConfiguration createDrawConfig() {
	final BallDrawer ballDrawer = BallDrawer.createDefault();
	final LineSegmentDrawer lineSegmentDrawer = LineSegmentDrawer.createDefault();

	final float centerOfMassStrokeWidth = 0.1f;
	final float centerOfMassRadius = 1;
	final Drawer centerOfMassDrawer = CrossDrawer.create(Color.GREEN, centerOfMassStrokeWidth).setRadius(centerOfMassRadius);

	return new DrawConfiguration(ballDrawer, lineSegmentDrawer, centerOfMassDrawer);
    }
}
