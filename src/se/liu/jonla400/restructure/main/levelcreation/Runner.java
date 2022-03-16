package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.main.Level;
import se.liu.jonla400.restructure.main.MovementDirection;
import se.liu.jonla400.restructure.main.RotationDirection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Runner
{
    public static void main(String[] args) {
        final LevelCreator levelCreator = new LevelCreator();
        final AddVertexState addVertexState = new AddVertexState();
        addVertexState.setChainsLineSegments(true);
        final MoveVertexState moveVertexState = new MoveVertexState();
        final DeleteLineSegmentState deleteLineSegmentState = new DeleteLineSegmentState();
        levelCreator.setState(addVertexState);

        final LevelCreatorJComponent levelCreatorJComponent = LevelCreatorJComponent.create(levelCreator);
        setupControls(levelCreator, levelCreatorJComponent, addVertexState, moveVertexState, deleteLineSegmentState);

        final JFrame frame = new JFrame("Become The Level!");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(levelCreatorJComponent);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        final int tickRate = 90;
        final double deltaSecondsPerTick = 1.0 / tickRate;
        final int deltaMillisecondsPerTick = 1000 / tickRate;

        final Timer tickTimer = new Timer(deltaMillisecondsPerTick, e -> {
            levelCreatorJComponent.repaint();
        });
        tickTimer.setCoalesce(true);
        tickTimer.start();
    }

    private static void setupControls(final LevelCreator levelCreator, final LevelCreatorJComponent levelCreatorJComponent,
                                      final AddVertexState addVertexState, final MoveVertexState moveVertexState,
                                      final DeleteLineSegmentState deleteLineSegmentState) {
        // TEMPORARY

        final InputMap inputMap = levelCreatorJComponent.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke("control Z"), "undo");
        inputMap.put(KeyStroke.getKeyStroke("control Y"), "redo");
        inputMap.put(KeyStroke.getKeyStroke("pressed M"), "toggleMagnetize");
        inputMap.put(KeyStroke.getKeyStroke("pressed C"), "toggleChain");
        inputMap.put(KeyStroke.getKeyStroke("pressed 1"), "add");
        inputMap.put(KeyStroke.getKeyStroke("pressed 2"), "move");
        inputMap.put(KeyStroke.getKeyStroke("pressed 3"), "delete");
        inputMap.put(KeyStroke.getKeyStroke("pressed ESCAPE"), "deleteInProgress");

        final ActionMap actionMap = levelCreatorJComponent.getActionMap();
        actionMap.put("undo", new AbstractAction()
        {
            @Override public void actionPerformed(final ActionEvent e) {
                levelCreator.undo();
            }
        });
        actionMap.put("redo", new AbstractAction()
        {
            @Override public void actionPerformed(final ActionEvent e) {
                levelCreator.redo();
            }
        });
        actionMap.put("toggleMagnetize", new AbstractAction()
        {
            @Override public void actionPerformed(final ActionEvent e) {
                addVertexState.setMagnetized(!addVertexState.isMagnetized());
            }
        });
        actionMap.put("toggleChain", new AbstractAction()
        {
            @Override public void actionPerformed(final ActionEvent e) {
                addVertexState.setChainsLineSegments(!addVertexState.chainsLineSegments());
            }
        });
        actionMap.put("add", new AbstractAction()
        {
            @Override public void actionPerformed(final ActionEvent e) {
                levelCreator.execute(new SetStateCommand(addVertexState));
            }
        });
        actionMap.put("move", new AbstractAction()
        {
            @Override public void actionPerformed(final ActionEvent e) {
                levelCreator.execute(new SetStateCommand(moveVertexState));
            }
        });
        actionMap.put("delete", new AbstractAction()
        {
            @Override public void actionPerformed(final ActionEvent e) {
                levelCreator.execute(new SetStateCommand(deleteLineSegmentState));
            }
        });
        actionMap.put("deleteInProgress", new AbstractAction()
        {
            @Override public void actionPerformed(final ActionEvent e) {
                if (levelCreator.getState().equals(addVertexState)) {
                    addVertexState.deleteNewLineSegmentStart(levelCreator);
                } else if (levelCreator.getState().equals(moveVertexState)) {
                    moveVertexState.putBackVertex(levelCreator);
                }
            }
        });

        final MouseLevelCreatorController mouseController = new MouseLevelCreatorController(levelCreator, levelCreatorJComponent::convertToGamePoint);
        levelCreatorJComponent.addMouseListener(mouseController);
        levelCreatorJComponent.addMouseMotionListener(mouseController);
        levelCreatorJComponent.addMouseWheelListener(mouseController);
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
