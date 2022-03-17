package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.main.DrawRegion;
import se.liu.jonla400.restructure.math.Vector2D;

import java.awt.*;
import java.util.Optional;

public class SetCameraMode implements LevelCreatorMode
{
    private Optional<Vector2D> possibleStartPoint;

    public SetCameraMode() {
	possibleStartPoint = Optional.empty();
    }

    @Override public void enter(final LevelCreator levelCreator) {
    }

    @Override public void exit(final LevelCreator levelCreator) {
    }

    @Override public void cursorPosChanged(final LevelCreator levelCreator) {
    }

    @Override public void cursorActionPerformed(final LevelCreator levelCreator) {
	levelCreator.execute(getCommand(levelCreator));
    }

    private Command getCommand(final LevelCreator levelCreator) {
	final Vector2D cursorPos = levelCreator.getCursorPos();
	if (possibleStartPoint.isEmpty()) {
	    return new PlaceStartCommand(cursorPos);
	} else {
	    final Vector2D startPoint = possibleStartPoint.get();
	    final DrawRegion newCamera = DrawRegion.createFromCorners(startPoint, cursorPos);
	    return new SetCameraCommand(levelCreator, newCamera, startPoint);
	}
    }

    public void stopSettingCamera(final LevelCreator levelCreator) {
	possibleStartPoint.ifPresent(start -> {
	    final Command stopSettingCameraCommand = new ReversedCommand(new PlaceStartCommand(start));
	    levelCreator.execute(stopSettingCameraCommand);
	});
    }

    @Override public void draw(final LevelCreator levelCreator, final Graphics2D g, final DrawRegion region) {
	possibleStartPoint.ifPresent(start -> drawUpcomingCamera(g, DrawRegion.createFromCorners(start, levelCreator.getCursorPos())));
    }

    private void drawUpcomingCamera(final Graphics2D g, final DrawRegion upcomingCamera) {
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
	private DrawRegion camera;
	private DrawRegion cameraBefore;
	private Vector2D startPointBefore;

	private SetCameraCommand(final LevelCreator levelCreator, final DrawRegion camera, final Vector2D startPointBefore) {
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
