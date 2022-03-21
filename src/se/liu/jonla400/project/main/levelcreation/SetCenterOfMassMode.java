package se.liu.jonla400.project.main.levelcreation;

import se.liu.jonla400.project.main.RectangularRegion;
import se.liu.jonla400.project.main.drawing.CrossDrawer;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;

public class SetCenterOfMassMode extends AdaptingMode
{
    @Override public void cursorPressed(final LevelCreator levelCreator) {
	levelCreator.execute(SetCenterOfMassCommand.createFromCurrentToCursor(levelCreator));
    }

    @Override public void draw(final LevelCreator levelCreator, final Graphics2D g, final RectangularRegion region) {
	final Vector2D pos = levelCreator.getCursorPos();
	final CrossDrawer drawerAtPos = CrossDrawer.create(1, new Color(0, 0, 0, 100), 0.1f);
	final TranslatedDrawer drawer = new TranslatedDrawer(pos, drawerAtPos);
	drawer.draw(g);
    }

    private static class SetCenterOfMassCommand implements Command
    {
	private Vector2D oldCenterOfMass;
	private Vector2D newCenterOfMass;

	private SetCenterOfMassCommand(final Vector2D oldCenterOfMass, final Vector2D newCenterOfMass) {
	    this.oldCenterOfMass = oldCenterOfMass;
	    this.newCenterOfMass = newCenterOfMass;
	}

	private static SetCenterOfMassCommand createFromCurrentToCursor(final LevelCreator levelCreator) {
	    return new SetCenterOfMassCommand(levelCreator.getCenterOfMass(), levelCreator.getCursorPos());
	}

	@Override public void execute(final LevelCreator levelCreator) {
	    levelCreator.setCenterOfMass(newCenterOfMass);
	}

	@Override public void undo(final LevelCreator levelCreator) {
	    levelCreator.setCenterOfMass(oldCenterOfMass);
	}
    }
}
