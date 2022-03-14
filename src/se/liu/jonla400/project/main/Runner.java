package se.liu.jonla400.project.main;

import se.liu.jonla400.project.math.Interval;
import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.implementation.collision.CustomShapeDefinition;
import se.liu.jonla400.project.physics.implementation.collision.LineSegmentDefinition;

import javax.swing.*;
import java.awt.*;

public class Runner
{
    public static void main(String[] args) {
	final LevelDefinition levelDefinition = new LevelDefinition(
		DrawRegion.createFromIntervals(new Interval(-10, 10), new Interval(-10, 10)),
		10,
		BodyDefinition.create(Vector2D.createCartesianVector(-7, 1), 1, 0.01),
		0.5,
		BodyDefinition.create(Vector2D.createCartesianVector(0, 0), 100, 100),
		CustomShapeDefinition.create(
			LineSegmentDefinition.create(Vector2D.createCartesianVector(-10, 0), Vector2D.createCartesianVector(10, 0))
		)
	);

	final Game game = Game.createStartingWithFirstLevel(levelDefinition);

	final JFrame frame = new JFrame("You See Me Rolling");
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	final GameJComponent contentPane = GameJComponent.create(game);
	final MouseGameController mouseGameController = MouseGameController.createWithDefaultControls(
		game, contentPane::convertToGamePoint);
	contentPane.addMouseListener(mouseGameController);
	contentPane.addMouseMotionListener(mouseGameController);
	contentPane.addMouseWheelListener(mouseGameController);

	frame.setContentPane(contentPane);
	frame.setSize(new Dimension(600, 600));
	frame.setLocationRelativeTo(null);
	frame.setExtendedState(Frame.MAXIMIZED_BOTH);
	frame.setVisible(true);

	final int tickRate = 90;
	final double deltaSeconds = 1.0 / tickRate;
	final int deltaMilliseconds = 1000 / tickRate;
	final Timer clockTimer = new Timer(deltaMilliseconds, e -> {
	    game.tick(deltaSeconds);
	    contentPane.repaint();
	});

	clockTimer.setCoalesce(true);
	clockTimer.start();
    }
}
