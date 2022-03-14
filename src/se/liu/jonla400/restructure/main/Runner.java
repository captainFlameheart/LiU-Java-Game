package se.liu.jonla400.restructure.main;

import se.liu.jonla400.restructure.math.Interval;
import se.liu.jonla400.restructure.math.Vector2D;
import se.liu.jonla400.restructure.physics.implementation.collision.CustomShapeDefinition;
import se.liu.jonla400.restructure.physics.implementation.collision.LineSegmentDefinition;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Runner
{
    public static void main(String[] args) {
        final GlobalLevelConfiguration globalLevelConfiguration = new GlobalLevelConfiguration(
                50,
                50,
                10,
                20,
                5,
                10,
                1.1
        );
        final LevelDefinition levelDefinition = new LevelDefinition(
                globalLevelConfiguration,
                Vector2D.createCartesian(0, 0),
                CustomShapeDefinition.create(
                        LineSegmentDefinition.create(Vector2D.createCartesian(0, -1), Vector2D.createCartesian(5, -1)),
                        LineSegmentDefinition.create(Vector2D.createCartesian(5, -1), Vector2D.createCartesian(5, -3)),
                        LineSegmentDefinition.create(Vector2D.createCartesian(5, -3), Vector2D.createCartesian(1, -3)),
                        LineSegmentDefinition.create(Vector2D.createCartesian(1, -3), Vector2D.createCartesian(1, -4)),
                        LineSegmentDefinition.create(Vector2D.createCartesian(1, -4), Vector2D.createCartesian(0, -4)),
                        LineSegmentDefinition.create(Vector2D.createCartesian(7, 0), Vector2D.createCartesian(7, -3)),
                        LineSegmentDefinition.create(Vector2D.createCartesian(2, -4), Vector2D.createCartesian(7, -4)),
                        LineSegmentDefinition.create(Vector2D.createCartesian(9, 0), Vector2D.createCartesian(9, -6)),
                        LineSegmentDefinition.create(Vector2D.createCartesian(9, -6), Vector2D.createCartesian(7, -6)),
                        LineSegmentDefinition.create(Vector2D.createCartesian(7, -6), Vector2D.createCartesian(7, -4)),
                        LineSegmentDefinition.create(Vector2D.createCartesian(2, -4), Vector2D.createCartesian(2, -6)),
                        LineSegmentDefinition.create(Vector2D.createCartesian(2, -6), Vector2D.createCartesian(12, -6)),
                        LineSegmentDefinition.create(Vector2D.createCartesian(0, -9), Vector2D.createCartesian(5, -9)),
                        LineSegmentDefinition.create(Vector2D.createCartesian(5, -9), Vector2D.createCartesian(5, -8))
                ),
                Vector2D.createCartesian(0, 3),
                0.5,
                1,
                0.01,
                DrawRegion.createFromIntervals(new Interval(-10, 10), new Interval(-10, 10))
        );
        final Level level = Level.createFromDefinition(levelDefinition);

        final GameJComponent gameJComponent = GameJComponent.create(level);
        setupControls(level, gameJComponent);

        final JFrame frame = new JFrame("Become The Level!");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(gameJComponent);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        final int tickRate = 90;
        final double deltaSecondsPerTick = 1.0 / tickRate;
        final int deltaMillisecondsPerTick = 1000 / tickRate;

        final Timer tickTimer = new Timer(deltaMillisecondsPerTick, e -> {
            level.tick(deltaSecondsPerTick);
            gameJComponent.repaint();
        });
        tickTimer.setCoalesce(true);
        tickTimer.start();
    }

    private static void setupControls(final Level level, final GameJComponent gameJComponent) {
        // TEMPORARY

        final InputMap inputMap = gameJComponent.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke("pressed A"), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke("pressed D"), "moveRight");
        inputMap.put(KeyStroke.getKeyStroke("pressed W"), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke("pressed S"), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke("pressed Q"), "rotateLeft");
        inputMap.put(KeyStroke.getKeyStroke("pressed E"), "rotateRight");

        inputMap.put(KeyStroke.getKeyStroke("released A"), "endMoveLeft");
        inputMap.put(KeyStroke.getKeyStroke("released D"), "endMoveRight");
        inputMap.put(KeyStroke.getKeyStroke("released W"), "endMoveUp");
        inputMap.put(KeyStroke.getKeyStroke("released S"), "endMoveDown");
        inputMap.put(KeyStroke.getKeyStroke("released Q"), "endRotateLeft");
        inputMap.put(KeyStroke.getKeyStroke("released E"), "endRotateRight");

        inputMap.put(KeyStroke.getKeyStroke("pressed Z"), "zoomIn");
        inputMap.put(KeyStroke.getKeyStroke("pressed X"), "zoomOut");

        final ActionMap actionMap = gameJComponent.getActionMap();

        actionMap.put("moveLeft", new MoveAction(level, MovementDirection.LEFT));
        actionMap.put("moveRight", new MoveAction(level, MovementDirection.RIGHT));
        actionMap.put("moveUp", new MoveAction(level, MovementDirection.UP));
        actionMap.put("moveDown", new MoveAction(level, MovementDirection.DOWN));
        actionMap.put("rotateLeft", new RotateAction(level, RotationDirection.LEFT));
        actionMap.put("rotateRight", new RotateAction(level, RotationDirection.RIGHT));

        actionMap.put("endMoveLeft", new EndMoveAction(level, MovementDirection.LEFT));
        actionMap.put("endMoveRight", new EndMoveAction(level, MovementDirection.RIGHT));
        actionMap.put("endMoveUp", new EndMoveAction(level, MovementDirection.UP));
        actionMap.put("endMoveDown", new EndMoveAction(level, MovementDirection.DOWN));
        actionMap.put("endRotateLeft", new EndRotateAction(level, RotationDirection.LEFT));
        actionMap.put("endRotateRight", new EndRotateAction(level, RotationDirection.RIGHT));

        actionMap.put("zoomIn", new ScaleAction(level, -1));
        actionMap.put("zoomOut", new ScaleAction(level, 1));

        final MouseController mouseController = new MouseController(level, gameJComponent::convertToGamePoint);
        gameJComponent.addMouseListener(mouseController);
        gameJComponent.addMouseMotionListener(mouseController);
        gameJComponent.addMouseWheelListener(mouseController);
    }

    private static class MoveAction extends AbstractAction
    {
        private Level level;
        private MovementDirection movementDirection;

        private MoveAction(final Level level, final MovementDirection movementDirection) {
            this.level = level;
            this.movementDirection = movementDirection;
        }

        @Override public void actionPerformed(final ActionEvent e) {
            level.startMovementInDirection(movementDirection);
        }
    }

    private static class EndMoveAction extends AbstractAction
    {
        private Level level;
        private MovementDirection movementDirection;

        private EndMoveAction(final Level level, final MovementDirection movementDirection) {
            this.level = level;
            this.movementDirection = movementDirection;
        }

        @Override public void actionPerformed(final ActionEvent e) {
            level.endMovementInDirection(movementDirection);
        }
    }

    private static class RotateAction extends AbstractAction
    {
        private Level level;
        private RotationDirection rotationDirection;

        private RotateAction(final Level level, final RotationDirection rotationDirection) {
            this.level = level;
            this.rotationDirection = rotationDirection;
        }

        @Override public void actionPerformed(final ActionEvent e) {
            level.startRotationInDirection(rotationDirection);
        }
    }

    private static class EndRotateAction extends AbstractAction
    {
        private Level level;
        private RotationDirection rotationDirection;

        private EndRotateAction(final Level level, final RotationDirection rotationDirection) {
            this.level = level;
            this.rotationDirection = rotationDirection;
        }

        @Override public void actionPerformed(final ActionEvent e) {
            level.endRotationInDirection(rotationDirection);
        }
    }

    private static class ScaleAction extends AbstractAction
    {
        private Level level;
        private int scaleCount;

        private ScaleAction(final Level level, final int scaleCount) {
            this.level = level;
            this.scaleCount = scaleCount;
        }

        @Override public void actionPerformed(final ActionEvent e) {
            level.scale(scaleCount);
        }
    }
}
