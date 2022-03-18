package se.liu.jonla400.restructure.main;

import se.liu.jonla400.restructure.main.levelcreation.LevelCreatorConstructor;
import se.liu.jonla400.restructure.main.levelcreation.LineSegmentType;
import se.liu.jonla400.restructure.math.Interval;
import se.liu.jonla400.restructure.math.Vector2D;
import se.liu.jonla400.restructure.physics.implementation.collision.CustomShape;
import se.liu.jonla400.restructure.physics.implementation.collision.LineSegment;

import javax.swing.*;
import java.util.List;
import java.awt.*;

public class LevelRunner
{
    public static void main(String[] args) {
        CustomShape<LineSegmentType> shape = CustomShape.copyFrom(List.of(
                LineSegment.create(Vector2D.createCartesian(0, -1), Vector2D.createCartesian(10, -1), LineSegmentType.WIN)
        ));
        final Vector2D centerOfMass = Vector2D.createZero();
        final RectangularRegion camera = RectangularRegion.createFromIntervals(new Interval(-10, 10), new Interval(-10, 10));
        final LevelDefinition levelDefinition = new LevelDefinition(shape, centerOfMass, camera);

        final World world = Level.createFromDefinition(levelDefinition);
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
