package se.liu.jonla400.project.main.levelcreation;

import se.liu.jonla400.project.main.drawing.DrawConfiguration;
import se.liu.jonla400.project.main.levelcreation.commands.CommandTimeLine;
import se.liu.jonla400.project.main.levelcreation.commands.SetModeCommand;
import se.liu.jonla400.project.main.levelcreation.modes.AddVertexMode;
import se.liu.jonla400.project.main.levelcreation.modes.ChangeTypeMode;
import se.liu.jonla400.project.main.levelcreation.modes.Mode;
import se.liu.jonla400.project.main.levelcreation.modes.MoveVertexMode;
import se.liu.jonla400.project.main.levelcreation.modes.RemoveLineSegmentMode;
import se.liu.jonla400.project.main.levelcreation.modes.SetCameraMode;
import se.liu.jonla400.project.main.levelcreation.modes.SetCenterOfMassMode;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Map;

/**
 * Is responsible for building a {@link LevelCreator}. Not only does this entail initializing the level creator,
 * but also to initialize all the modes of the level creator and bind keys to the modes.
 */
public class LevelCreatorBuilder
{
    /**
     * Builds a level creator that starts with the given {@link LevelBlueprint}
     * and draws itself according to the given {@link DrawConfiguration}.
     *
     * Controls:
     * CTRL+Z -> undo
     * CTRL+Y -> redo
     * 1 -> {@link AddVertexMode}
     * 2 -> {@link MoveVertexMode}
     * 3 -> {@link RemoveLineSegmentMode}
     * 4 -> {@link ChangeTypeMode}
     * 5 -> {@link SetCenterOfMassMode}
     * 6 -> {@link SetCameraMode}
     *
     * @param blueprint The blueprint to start with
     * @param drawConfig How the level creator should be drawn
     * @return The created LevelCreator
     */
    public LevelCreator buildLevelCreator(final LevelBlueprint blueprint, final DrawConfiguration drawConfig) {
        final DrawableLevelBlueprint drawableBlueprint = new DrawableLevelBlueprint(blueprint, drawConfig);
        final CommandTimeLine commandTimeLine = CommandTimeLine.createEmpty();

        final int cursorActionButton = MouseEvent.BUTTON1; // Each mode will respond to this button

        // Create modes
        final AddVertexMode addVertexMode = AddVertexMode.createWithDefaultConfigAndKeys();
        final MoveVertexMode moveVertexMode = MoveVertexMode.createWithDefaultDeselectKey();
        final RemoveLineSegmentMode removeLineSegmentMode = new RemoveLineSegmentMode();
        final ChangeTypeMode changeTypeMode = new ChangeTypeMode();
        final SetCenterOfMassMode setCenterOfMassMode = SetCenterOfMassMode.createWithDefaultDrawing();
        final SetCameraMode setCameraMode = SetCameraMode.createWithDefaultDeselectKey();

        final Mode currentMode = addVertexMode; // Start with adding vertices

        final Vector2D cursorPos = Vector2D.createZero(); // Initialize the considered cursor position to (0, 0)

        final CreatorKeyListener keyListener = DefaultCreatorKeyListener.create(
                addVertexMode, moveVertexMode, removeLineSegmentMode, changeTypeMode, setCenterOfMassMode, setCameraMode);

        return new LevelCreator(drawableBlueprint, commandTimeLine, cursorActionButton, currentMode, cursorPos, keyListener);
    }

    private static class DefaultCreatorKeyListener implements CreatorKeyListener
    {
        private Map<Integer, Mode> keyCodeToMode;

        private DefaultCreatorKeyListener(final Map<Integer, Mode> keyCodeToMode) {
            this.keyCodeToMode = keyCodeToMode;
        }

        private static DefaultCreatorKeyListener create(
                final AddVertexMode addVertexMode, final MoveVertexMode moveVertexMode,
                final RemoveLineSegmentMode removeLineSegmentMode, final ChangeTypeMode changeTypeMode,
                final SetCenterOfMassMode setCenterOfMassMode, final SetCameraMode setCameraMode)
        {
            final Map<Integer, Mode> keyCodeToMode = Map.of(
                    KeyEvent.VK_1, addVertexMode,
                    KeyEvent.VK_2, moveVertexMode,
                    KeyEvent.VK_3, removeLineSegmentMode,
                    KeyEvent.VK_4, changeTypeMode,
                    KeyEvent.VK_5, setCenterOfMassMode,
                    KeyEvent.VK_6, setCameraMode
            );
            return new DefaultCreatorKeyListener(keyCodeToMode);
        }

        @Override public void keyPressed(final LevelCreator levelCreator, final KeyEvent keyEvent) {
            possiblyUndoOrRedo(levelCreator, keyEvent);
            possiblyChangeMode(levelCreator, keyEvent);
        }

        private void possiblyUndoOrRedo(final LevelCreator levelCreator, final KeyEvent keyEvent) {
            // CTRL+Z -> undo
            // CTRL+Y -> redo
            if (keyEvent.isControlDown()) {
                final int keyCode = keyEvent.getKeyCode();
                if (keyCode == KeyEvent.VK_Z) {
                    levelCreator.undo();
                } else if (keyCode == KeyEvent.VK_Y) {
                    levelCreator.redo();
                }
            }
        }

        private void possiblyChangeMode(final LevelCreator levelCreator, final KeyEvent keyEvent) {
            final Mode newMode = keyCodeToMode.get(keyEvent.getKeyCode());
            if (newMode != null && !newMode.equals(levelCreator.getCurrentMode())) {
                levelCreator.execute(SetModeCommand.createFromCurrentMode(levelCreator, newMode));
            }
        }

        @Override public void keyReleased(final LevelCreator levelCreator, final KeyEvent keyEvent) {}
    }
}
