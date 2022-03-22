package se.liu.jonla400.project.main.levelcreation;

import se.liu.jonla400.project.math.RectangularRegion;
import se.liu.jonla400.project.main.drawing.CameraDrawer;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Optional;

public class SetCameraMode extends AdaptingMode
{
    private Optional<Vector2D> markedStart;
    private int unmarkStartKeyCode;

    private SetCameraMode(final Optional<Vector2D> markedStart, final int unmarkStartKeyCode) {
	this.markedStart = markedStart;
	this.unmarkStartKeyCode = unmarkStartKeyCode;
    }

    public static SetCameraMode createWithDefaultUnmarkKey() {
	return new SetCameraMode(Optional.empty(), KeyEvent.VK_ESCAPE);
    }

    @Override public void cursorPressed(final LevelCreator levelCreator) {
	if (markedStart.isEmpty()) {
	    levelCreator.execute(new MarkStartCommand(levelCreator.getCursorPos()));
	}
    }

    @Override public void cursorReleased(final LevelCreator levelCreator) {
	markedStart.ifPresent(start -> {
	    final Vector2D cursorPos = levelCreator.getCursorPos();
	    final Command unmarkStartCommand = getUnmarkStartCommand(start);
	    final Command command;
	    if (start.equals(cursorPos)) {
		command = unmarkStartCommand;
	    } else {
		final RectangularRegion newCamera = RectangularRegion.createFromCorners(start, cursorPos);
		final Command setCameraCommand = SetCameraCommand.createFromCurrent(levelCreator, newCamera);
		command = CombinedCommand.create(setCameraCommand, unmarkStartCommand);
	    }
	    levelCreator.execute(command);
	});
    }

    @Override public void keyPressed(final LevelCreator levelCreator, final KeyEvent keyEvent) {
	if (keyEvent.getKeyCode() == unmarkStartKeyCode) {
	    markedStart.ifPresent(start -> levelCreator.execute(getUnmarkStartCommand(start)));
	}
    }

    private Command getUnmarkStartCommand(final Vector2D start) {
	return new ReversedCommand(new MarkStartCommand(start));
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

    private static class SetCameraCommand implements Command
    {
	private RectangularRegion oldCamera;
	private RectangularRegion newCamera;

	private SetCameraCommand(final RectangularRegion oldCamera, final RectangularRegion newCamera) {
	    this.oldCamera = oldCamera;
	    this.newCamera = newCamera;
	}

	private static SetCameraCommand createFromCurrent(final LevelCreator levelCreator, final RectangularRegion newCamera) {
	    return new SetCameraCommand(levelCreator.getCamera(), newCamera);
	}

	@Override public void execute(final LevelCreator levelCreator) {
	    levelCreator.setCamera(newCamera);
	}

	@Override public void undo(final LevelCreator levelCreator) {
	    levelCreator.setCamera(oldCamera);
	}
    }
}
