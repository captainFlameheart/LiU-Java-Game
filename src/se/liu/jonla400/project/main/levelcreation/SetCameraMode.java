package se.liu.jonla400.project.main.levelcreation;

import se.liu.jonla400.project.main.RectangularRegion;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.util.Optional;

public class SetCameraMode extends AdaptingMode
{
    private Optional<Vector2D> markedStart;

    public SetCameraMode() {
	markedStart = Optional.empty();
    }

    @Override public void cursorPressed(final LevelCreator levelCreator) {
	if (markedStart.isEmpty()) {
	    levelCreator.execute(new MarkStartCommand(levelCreator.getCursorPos()));
	}
    }

    @Override public void cursorReleased(final LevelCreator levelCreator) {
	markedStart.ifPresent(start -> levelCreator.execute(
		new SetCameraAndUnmarkStartCommand(levelCreator.getCamera(), start, levelCreator.getCursorPos()))
	);
    }

    public void stopSettingCamera(final LevelCreator levelCreator) {
	markedStart.ifPresent(start -> {
	    final Command stopSettingCameraCommand = new ReversedCommand(new MarkStartCommand(start));
	    levelCreator.execute(stopSettingCameraCommand);
	});
    }

    @Override public void draw(final LevelCreator levelCreator, final Graphics2D g, final RectangularRegion region) {
	markedStart.ifPresent(start -> drawUpcomingCamera(g, RectangularRegion.createFromCorners(start, levelCreator.getCursorPos())));
    }

    private void drawUpcomingCamera(final Graphics2D g, final RectangularRegion upcomingCamera) {
	final Color color = new Color(0, 0, 0, 100);
	final float strokeWidth = 0.1f;
	final CameraDrawer cameraDrawer = CameraDrawer.createDashed(upcomingCamera, color, strokeWidth);
	cameraDrawer.draw(g);
    }

    private class MarkStartCommand implements Command
    {
	private Vector2D pos;

	private MarkStartCommand(final Vector2D pos) {
	    this.pos = pos;
	}

	@Override public void execute(final LevelCreator levelCreator) {
	    markedStart = Optional.of(pos);
	}

	@Override public void undo(final LevelCreator levelCreator) {
	    markedStart = Optional.empty();
	}
    }

    private class SetCameraAndUnmarkStartCommand implements Command
    {
	private RectangularRegion oldCamera;
	private Vector2D newCameraStart;
	private Vector2D newCameraEnd;

	private SetCameraAndUnmarkStartCommand(final RectangularRegion oldCamera, final Vector2D newCameraStart,
					      final Vector2D newCameraEnd)
	{
	    this.oldCamera = oldCamera;
	    this.newCameraStart = newCameraStart;
	    this.newCameraEnd = newCameraEnd;
	}

	@Override public void execute(final LevelCreator levelCreator) {
	    levelCreator.setCamera(RectangularRegion.createFromCorners(newCameraStart, newCameraEnd));
	    markedStart = Optional.empty();
	}

	@Override public void undo(final LevelCreator levelCreator) {
	    levelCreator.setCamera(oldCamera);
	    markedStart = Optional.of(newCameraStart);
	}
    }
}
