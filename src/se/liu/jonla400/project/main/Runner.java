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
    public static void main(String[] args) {
	final DrawConfiguration drawConfig = createDrawConfig();

	final Object[] options = {"Play", "Create"};
	final int chosenOption = JOptionPane.showOptionDialog(
		null, "Do you want to play or create?", "Startup choice",
		JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
	switch (chosenOption) {
	    case 0 -> GameRunner.run(drawConfig);
	    case 1 -> CreatorRunner.run(drawConfig);
	    default -> System.exit(0);
	}
    }

    public static DrawConfiguration createDrawConfig() {
	final BallDrawer ballDrawer = BallDrawer.createDefault();
	final LineSegmentDrawer lineSegmentDrawer = LineSegmentDrawer.createDefault();
	final Drawer centerOfMassDrawer = CrossDrawer.create(Color.GREEN, 0.1f).setRadius(1);
	return new DrawConfiguration(ballDrawer, lineSegmentDrawer, centerOfMassDrawer);
    }
}
