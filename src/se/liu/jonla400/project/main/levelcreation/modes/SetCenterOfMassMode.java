package se.liu.jonla400.project.main.levelcreation.modes;

import se.liu.jonla400.project.main.drawing.Drawer;
import se.liu.jonla400.project.main.drawing.Transform;
import se.liu.jonla400.project.main.drawing.TransformedDrawer;
import se.liu.jonla400.project.main.levelcreation.LevelCreator;
import se.liu.jonla400.project.main.levelcreation.commands.Command;
import se.liu.jonla400.project.math.RectangularRegion;
import se.liu.jonla400.project.main.drawing.CrossDrawer;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;

public class SetCenterOfMassMode extends AdaptingMode
{
    private Drawer upcomingCenterOfMassDrawer;

    private SetCenterOfMassMode(final Drawer upcomingCenterOfMassDrawer) {
	this.upcomingCenterOfMassDrawer = upcomingCenterOfMassDrawer;
    }

    public static SetCenterOfMassMode createWithDefaultDrawing() {
	final Drawer drawer = CrossDrawer.create(Color.RED, 0.05f).setRadius(0.2f);
	return new SetCenterOfMassMode(drawer);
    }

    @Override public void cursorPressed(final LevelCreator levelCreator) {
	levelCreator.execute(SetCenterOfMassCommand.createFromCurrentToCursor(levelCreator));
    }

    @Override public void draw(final LevelCreator levelCreator, final Graphics2D g, final RectangularRegion region) {
	TransformedDrawer.draw(
		g, Transform.createWithTranslation(levelCreator.getCursorPos()),
		upcomingCenterOfMassDrawer
	);
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
