package se.liu.jonla400.restructure.main;

import se.liu.jonla400.restructure.math.Vector2D;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.function.Function;

public class MouseController extends MouseInputAdapter
{
    private Level level;
    private Function<Vector2D, Vector2D> mouseToLevelPointConverter;

    public MouseController(final Level level, final Function<Vector2D, Vector2D> mouseToLevelPointConverter) {
	this.level = level;
	this.mouseToLevelPointConverter = mouseToLevelPointConverter;
    }

    @Override public void mouseDragged(final MouseEvent e) {
	setCursorPos(e);
    }

    @Override public void mouseMoved(final MouseEvent e) {
	setCursorPos(e);
    }

    @Override public void mousePressed(final MouseEvent e) {
	if (e.getButton() == MouseEvent.BUTTON1) {
	    level.setPointAtCursorGrabbed(true);
	} else if (e.getButton() == MouseEvent.BUTTON3) {
	    level.setCenterOfMassAtCursor();
	}
    }

    @Override public void mouseReleased(final MouseEvent e) {
	if (e.getButton() == MouseEvent.BUTTON1) {
	    level.setPointAtCursorGrabbed(false);
	}
    }

    @Override public void mouseWheelMoved(final MouseWheelEvent e) {
	level.scale(e.getPreciseWheelRotation());
	setCursorPos(e);
    }

    private void setCursorPos(final MouseEvent e) {
	final Vector2D mousePos = Vector2D.createCartesianVector(e.getX(), e.getY());
	final Vector2D mousePosInLevelSpace = mouseToLevelPointConverter.apply(mousePos);
	level.setCursorPos(mousePosInLevelSpace);
    }
}
