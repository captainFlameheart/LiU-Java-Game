package se.liu.jonla400.project.main.levelcreation.modes;

import se.liu.jonla400.project.main.levelcreation.LevelCreator;
import se.liu.jonla400.project.main.levelcreation.commands.ReversedCommand;
import se.liu.jonla400.project.main.levelcreation.commands.CombinedCommand;
import se.liu.jonla400.project.main.levelcreation.commands.Command;
import se.liu.jonla400.project.math.RectangularRegion;
import se.liu.jonla400.project.main.drawing.CameraDrawer;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Optional;

/**
 * Represents a {@link Mode} that sets the start camera of a level. A key exists to stop setting
 * the camera. Please note that the camera represents the smallest possible region visible
 * in a {@link se.liu.jonla400.project.main.world.WorldGUI}. A world GUI might however display
 * more of the level since the aspect ratio of the GUI can be different than the camera's aspect ratio.
 */
public class SetCameraMode extends AdaptingMode
{
    private Optional<Vector2D> possibleStart;
    private int deselectStartKeyCode;

    private SetCameraMode(final Optional<Vector2D> possibleStart, final int deselectStartKeyCode) {
	this.possibleStart = possibleStart;
	this.deselectStartKeyCode = deselectStartKeyCode;
    }

    /**
     * Creates a SetCameraMode with escape as the key to stop setting the camera
     *
     * @return The created SetCameraMode
     */
    public static SetCameraMode createWithDefaultDeselectKey() {
	return new SetCameraMode(Optional.empty(), KeyEvent.VK_ESCAPE);
    }

    /**
     * Places down the upcoming cameras start corner, if it doesn't already exist
     *
     * @param levelCreator The considered level creator
     */
    @Override public void cursorPressed(final LevelCreator levelCreator) {
	if (possibleStart.isEmpty()) {
	    levelCreator.execute(new SelectStartCommand(levelCreator.getCursorPos()));
	}
    }

    /**
     * Sets the camera from the placed down start to the current cursor position. If the placed
     * down start and the current cursor position are the same the camera isn't changed. Nothing
     * happens if there is no placed down start.
     *
     * @param levelCreator The considered level creator
     */
    @Override public void cursorReleased(final LevelCreator levelCreator) {
	possibleStart.ifPresent(start -> {
	    final Vector2D cursorPos = levelCreator.getCursorPos();
	    final Command deselectStartCommand = createDeselectStartCommand(start);
	    final Command command;
	    if (start.equals(cursorPos)) {
		command = deselectStartCommand; // We don't want a camera size of 0, so only deselect the start
	    } else {
		final RectangularRegion newCamera = RectangularRegion.createFromCorners(start, cursorPos);
		final Command setCameraCommand = SetCameraCommand.createFromCurrent(levelCreator, newCamera);
		command = CombinedCommand.create(setCameraCommand, deselectStartCommand);
	    }
	    levelCreator.execute(command);
	});
    }

    /**
     * If the correct key is pressed and there exists a selected start of the upcoming camera,
     * this method deselects the upcoming start
     *
     * @param levelCreator The considered level creator
     * @param keyEvent The key event containing the pressed key
     */
    @Override public void keyPressed(final LevelCreator levelCreator, final KeyEvent keyEvent) {
	if (keyEvent.getKeyCode() == deselectStartKeyCode) {
	    possibleStart.ifPresent(start -> levelCreator.execute(createDeselectStartCommand(start)));
	}
    }

    private Command createDeselectStartCommand(final Vector2D start) {
	return new ReversedCommand(new SelectStartCommand(start));
    }

    /**
     * If the start of the upcoming camera has been placed down, draws a preview of the upcoming camera
     * from the start to the cursor
     *
     * @param levelCreator The level creator containing the cursor position
     * @param g The graphics object to draw to
     */
    @Override public void draw(final LevelCreator levelCreator, final Graphics2D g) {
	possibleStart.ifPresent(start -> drawUpcomingCamera(g, RectangularRegion.createFromCorners(start, levelCreator.getCursorPos())));
    }

    private void drawUpcomingCamera(final Graphics2D g, final RectangularRegion upcomingCamera) {
	final Color color = new Color(0, 0, 0, 100);
	final float strokeWidth = 0.1f;
	final CameraDrawer cameraDrawer = CameraDrawer.createDashed(upcomingCamera, color, strokeWidth);
	cameraDrawer.draw(g);
    }

    private class SelectStartCommand implements Command
    {
	private Vector2D pos;

	private SelectStartCommand(final Vector2D pos) {
	    this.pos = pos;
	}

	@Override public void execute(final LevelCreator levelCreator) {
	    possibleStart = Optional.of(pos);
	}

	@Override public void undo(final LevelCreator levelCreator) {
	    possibleStart = Optional.empty();
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
