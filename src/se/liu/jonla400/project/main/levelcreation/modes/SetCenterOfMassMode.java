package se.liu.jonla400.project.main.levelcreation.modes;

import se.liu.jonla400.project.main.drawing.Drawer;
import se.liu.jonla400.project.main.drawing.Transform;
import se.liu.jonla400.project.main.drawing.TransformedDrawer;
import se.liu.jonla400.project.main.levelcreation.LevelCreator;
import se.liu.jonla400.project.main.levelcreation.commands.Command;
import se.liu.jonla400.project.main.drawing.CrossDrawer;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;

/**
 * Represents a {@link Mode} that sets the initial center of mass of a level. This means
 * that the start position of the level's {@link se.liu.jonla400.project.physics.main.Body}
 * will be set.
 */
public class SetCenterOfMassMode extends AdaptingMode
{
    private Drawer upcomingCenterOfMassDrawer;

    private SetCenterOfMassMode(final Drawer upcomingCenterOfMassDrawer) {
	this.upcomingCenterOfMassDrawer = upcomingCenterOfMassDrawer;
    }

    /**
     * Creates a new SetCenterOfMassMode with a default way of drawing where the center of mass
     * will be placed when the cursor is pressed
     *
     * @return The created SetCenterOfMassMode
     */
    public static SetCenterOfMassMode createWithDefaultDrawing() {
	final float strokeWidth = 0.05f;
	final double radius = 0.2;
	final Drawer upcomingCenterOfMassDrawer = CrossDrawer.create(Color.RED, strokeWidth).setRadius(radius);
	return new SetCenterOfMassMode(upcomingCenterOfMassDrawer);
    }

    /**
     * Sets the center of mass at the current cursor position
     *
     * @param levelCreator The level creator considered
     */
    @Override public void cursorPressed(final LevelCreator levelCreator) {
	levelCreator.execute(SetCenterOfMassCommand.createFromCurrentToCursor(levelCreator));
    }

    /**
     * Draws a preview of where the center of mass will be placed when the cursor is pressed,
     * which is at the current cursor position
     *
     * @param levelCreator The level creator containing the cursor position
     * @param g The graphics object to draw to
     */
    @Override public void draw(final LevelCreator levelCreator, final Graphics2D g) {
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
