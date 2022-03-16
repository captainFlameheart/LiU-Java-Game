package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.math.Vector2D;

import javax.swing.event.MouseInputAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Function;

public class MouseLevelCreatorController extends MouseInputAdapter
{
    private LevelCreator1 levelCreator;
    private Function<Vector2D, Vector2D> mouseToLevelCreatorPointConverter;

    public MouseLevelCreatorController(final LevelCreator1 levelCreator,
				       final Function<Vector2D, Vector2D> mouseToLevelCreatorPointConverter)
    {
	this.levelCreator = levelCreator;
	this.mouseToLevelCreatorPointConverter = mouseToLevelCreatorPointConverter;
    }

    @Override public void mouseDragged(final MouseEvent e) {
	setLevelCreatorCursor(e);
    }

    @Override public void mouseMoved(final MouseEvent e) {
	setLevelCreatorCursor(e);
    }

    @Override public void mousePressed(final MouseEvent e) {
	levelCreator.performCursorAction();
    }

    private void setLevelCreatorCursor(final MouseEvent e) {
	final Vector2D mousePosInLevelCreatorSpace = getMousePosInLevelCreatorSpace(e);
	levelCreator.setCursorPos(mousePosInLevelCreatorSpace);
    }

    private Vector2D getMousePosInLevelCreatorSpace(final MouseEvent e) {
	final Vector2D mousePos = Vector2D.createCartesian(e.getX(), e.getY());
	return mouseToLevelCreatorPointConverter.apply(mousePos);
    }
}
