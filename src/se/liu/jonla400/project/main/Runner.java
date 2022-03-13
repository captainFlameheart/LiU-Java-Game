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
		BodyDefinition.createWithDefaultMasses(Vector2D.createCartesianVector(-10 + 3, 10)),
		0.5,
		BodyDefinition.create(Vector2D.createCartesianVector(0, -1), 1000, 870),
		CustomShapeDefinition.create(
			LineSegmentDefinition.create(Vector2D.createCartesianVector(-10, 5), Vector2D.createCartesianVector(10, -5)),
			LineSegmentDefinition.create(Vector2D.createCartesianVector(10, -5), Vector2D.createCartesianVector(13, -5)),
			LineSegmentDefinition.create(Vector2D.createCartesianVector(13, -5), Vector2D.createCartesianVector(14, -4.5))
		)
	);

	final Game game = Game.createStartingWithFirstLevel(levelDefinition);

	final JFrame frame = new JFrame("You See Me Rolling");
	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	final JComponent contentPane = GameJComponent.create(game);
	contentPane.setPreferredSize(new Dimension(1200, 600));
	frame.setContentPane(contentPane);
	frame.pack();
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
