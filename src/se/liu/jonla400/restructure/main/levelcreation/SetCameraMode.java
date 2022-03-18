package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.main.RectangularRegion;
import se.liu.jonla400.restructure.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Optional;

public class SetCameraMode implements LevelCreatorMode
{
    private Optional<Vector2D> possibleStartPoint;

    public SetCameraMode() {
	possibleStartPoint = Optional.empty();
    }

    @Override public void cursorPressed(final LevelCreator levelCreator) {
	if (possibleStartPoint.isEmpty()) {
	    levelCreator.execute(new PlaceStartCommand(levelCreator.getCursorPos()));
	}
    }

    @Override public void cursorReleased(final LevelCreator levelCreator) {
	possibleStartPoint.ifPresent(start -> {
	    final RectangularRegion newCamera = RectangularRegion.createFromCorners(start, levelCreator.getCursorPos());
	    levelCreator.execute(new SetCameraCommand(levelCreator, newCamera, start));
	});
    }

    @Override public void keyPressed(final LevelCreator levelCreator, final KeyEvent keyEvent) {}

    @Override public void keyReleased(final LevelCreator levelCreator, final KeyEvent keyEvent) {}

    public void stopSettingCamera(final LevelCreator levelCreator) {
	possibleStartPoint.ifPresent(start -> {
	    final Command stopSettingCameraCommand = new ReversedCommand(new PlaceStartCommand(start));
	    levelCreator.execute(stopSettingCameraCommand);
	});
    }

    @Override public void draw(final LevelCreator levelCreator, final Graphics2D g, final RectangularRegion region) {
	possibleStartPoint.ifPresent(start -> drawUpcomingCamera(g, RectangularRegion.createFromCorners(start, levelCreator.getCursorPos())));
    }

    private void drawUpcomingCamera(final Graphics2D g, final RectangularRegion upcomingCamera) {
	final Color color = new Color(0, 0, 0, 100);
	final float strokeWidth = 0.1f;
	final CameraDrawer cameraDrawer = CameraDrawer.createDashed(upcomingCamera, color, strokeWidth);
	cameraDrawer.draw(g);
    }

    private class PlaceStartCommand implements Command
    {
	private Vector2D pos;

	private PlaceStartCommand(final Vector2D pos) {
	    this.pos = pos;
	}

	@Override public void execute(final LevelCreator levelCreator) {
	    possibleStartPoint = Optional.of(pos);
	}

	@Override public void undo(final LevelCreator levelCreator) {
	    possibleStartPoint = Optional.empty();
	}
    }

    private class SetCameraCommand implements Command
    {
	private RectangularRegion camera;
	private RectangularRegion cameraBefore;
	private Vector2D startPointBefore;

	private SetCameraCommand(final LevelCreator levelCreator, final RectangularRegion camera, final Vector2D startPointBefore) {
	    this.camera = camera;
	    cameraBefore = levelCreator.getLevelCamera();
	    this.startPointBefore = startPointBefore;
	}

	@Override public void execute(final LevelCreator levelCreator) {
	    levelCreator.setLevelCamera(camera);
	    possibleStartPoint = Optional.empty();
	}

	@Override public void undo(final LevelCreator levelCreator) {
	    levelCreator.setLevelCamera(cameraBefore);
	    possibleStartPoint = Optional.of(startPointBefore);
	}
    }
}
