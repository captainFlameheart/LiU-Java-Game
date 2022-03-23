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

public class LevelCreatorBuilder
{
    public LevelCreator buildLevelCreator(final LevelBlueprint blueprint, final DrawConfiguration drawConfig) {
        final DrawableLevelBlueprint drawableBlueprint = new DrawableLevelBlueprint(blueprint, drawConfig);
        final CommandTimeLine commandTimeLine = CommandTimeLine.createEmpty();

        final int cursorActionButton = MouseEvent.BUTTON1;

        final AddVertexMode addVertexMode = AddVertexMode.createWithDefaultConfigAndKeys();
        final MoveVertexMode moveVertexMode = MoveVertexMode.createWithDefaultDeselectKey();
        final RemoveLineSegmentMode removeLineSegmentMode = new RemoveLineSegmentMode();
        final ChangeTypeMode changeTypeMode = new ChangeTypeMode();
        final SetCenterOfMassMode setCenterOfMassMode = SetCenterOfMassMode.createWithDefaultDrawing();
        final SetCameraMode setCameraMode = SetCameraMode.createWithDefaultDeselectKey();

        final Mode currentMode = addVertexMode;

        final Vector2D cursorPos = Vector2D.createZero();

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
            final int keyCode = keyEvent.getKeyCode();
            if (keyEvent.isControlDown()) {
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
