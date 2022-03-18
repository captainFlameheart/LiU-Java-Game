package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.main.RectangularRegion;
import se.liu.jonla400.restructure.main.drawing.CrossDrawer;
import se.liu.jonla400.restructure.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;

public class SetCenterOfMassMode implements LevelCreatorMode
{
    @Override public void cursorPressed(final LevelCreator levelCreator) {
	levelCreator.execute(new SetCenterOfMassCommand(levelCreator));
    }

    @Override public void cursorReleased(final LevelCreator levelCreator) {

    }

    @Override public void keyPressed(final LevelCreator levelCreator, final KeyEvent keyEvent) {

    }

    @Override public void keyReleased(final LevelCreator levelCreator, final KeyEvent keyEvent) {

    }

    @Override public void draw(final LevelCreator levelCreator, final Graphics2D g, final RectangularRegion region) {
	final Vector2D pos = levelCreator.getCursorPos();
	final CrossDrawer drawerAtPos = CrossDrawer.create(1, new Color(0, 0, 0, 100), 0.1f);
	final TranslatedDrawer drawer = new TranslatedDrawer(pos, drawerAtPos);
	drawer.draw(g);
    }

    private static class SetCenterOfMassCommand implements Command
    {
	private Vector2D centerOfMass;
	private Vector2D centerOfMassBefore;

	private SetCenterOfMassCommand(final LevelCreator levelCreator) {
	    this.centerOfMass = levelCreator.getCursorPos();
	    centerOfMassBefore = levelCreator.getCenterOfMass();
	}

	@Override public void execute(final LevelCreator levelCreator) {
	    levelCreator.setCenterOfMass(centerOfMass);
	}

	@Override public void undo(final LevelCreator levelCreator) {
	    levelCreator.setCenterOfMass(centerOfMassBefore);
	}
    }
}
