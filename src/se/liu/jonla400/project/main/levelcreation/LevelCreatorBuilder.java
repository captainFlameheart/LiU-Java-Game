package se.liu.jonla400.project.main.levelcreation;

import se.liu.jonla400.project.main.drawing.DrawConfiguration;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class LevelCreatorBuilder
{
    public LevelCreator buildLevelCreator(final LevelBlueprint blueprint, final DrawConfiguration drawConfig) {
        final DrawableLevelBlueprint drawableBlueprint = new DrawableLevelBlueprint(blueprint, drawConfig);
        final CommandTimeLine commandTimeLine = CommandTimeLine.createEmpty();

        final int cursorActionButton = MouseEvent.BUTTON1;

        final AddVertexMode addVertexMode = AddVertexMode.createWithDefaultConfigAndKeys();
        final MoveVertexMode moveVertexMode = MoveVertexMode.createWithDefaultUnmarkKey();
        final RemoveLineSegmentMode removeLineSegmentMode = new RemoveLineSegmentMode();
        final ChangeTypeMode changeTypeMode = new ChangeTypeMode();
        final SetCenterOfMassMode setCenterOfMassMode = SetCenterOfMassMode.createWithDefaultDrawing();
        final SetCameraMode setCameraMode = SetCameraMode.createWithDefaultUnmarkKey();

        final Mode currentMode = addVertexMode;

        final Vector2D cursorPos = Vector2D.createZero();

        final KeyListener keyListener = new DefaultKeyListener(
                addVertexMode, moveVertexMode, removeLineSegmentMode, changeTypeMode, setCenterOfMassMode, setCameraMode
        );

        return new LevelCreator(drawableBlueprint, commandTimeLine, cursorActionButton, currentMode, cursorPos, keyListener);
    }

    private static class DefaultKeyListener implements KeyListener
    {
        private AddVertexMode addVertexMode;
        private MoveVertexMode moveVertexMode;
        private RemoveLineSegmentMode removeLineSegmentMode;
        private ChangeTypeMode changeTypeMode;
        private SetCenterOfMassMode setCenterOfMassMode;
        private SetCameraMode setCameraMode;

        private DefaultKeyListener(final AddVertexMode addVertexMode, final MoveVertexMode moveVertexMode,
                                  final RemoveLineSegmentMode removeLineSegmentMode, final ChangeTypeMode changeTypeMode,
                                  final SetCenterOfMassMode setCenterOfMassMode, final SetCameraMode setCameraMode)
        {
            this.addVertexMode = addVertexMode;
            this.moveVertexMode = moveVertexMode;
            this.removeLineSegmentMode = removeLineSegmentMode;
            this.changeTypeMode = changeTypeMode;
            this.setCenterOfMassMode = setCenterOfMassMode;
            this.setCameraMode = setCameraMode;
        }

        @Override public void keyPressed(final LevelCreator levelCreator, final KeyEvent keyEvent) {
            final int keyCode = keyEvent.getKeyCode();
            if (keyEvent.isControlDown()) {
                if (keyCode == KeyEvent.VK_Z) {
                    levelCreator.undo();
                    return;
                }
                if (keyCode == KeyEvent.VK_Y) {
                    levelCreator.redo();
                    return;
                }
            }
            Mode newMode = switch (keyCode) {
                case KeyEvent.VK_1 -> addVertexMode;
                case KeyEvent.VK_2 -> moveVertexMode;
                case KeyEvent.VK_3 -> removeLineSegmentMode;
                case KeyEvent.VK_4 -> changeTypeMode;
                case KeyEvent.VK_5 -> setCenterOfMassMode;
                case KeyEvent.VK_6 -> setCameraMode;
                default -> null;
            };
            if (newMode != null && !newMode.equals(levelCreator.getCurrentMode())) {
                levelCreator.execute(SetModeCommand.createFromCurrentMode(levelCreator, newMode));
            }
        }

        @Override public void keyReleased(final LevelCreator levelCreator, final KeyEvent keyEvent) {}
    }
}
