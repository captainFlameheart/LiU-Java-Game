package se.liu.jonla400.project.main.game;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.main.Body;
import se.liu.jonla400.project.physics.collision.implementation.CustomCollider;
import se.liu.jonla400.project.physics.collision.implementation.TranslatedCustomShape;

import java.awt.event.MouseEvent;

/**
 * Is responsible for moving the center of mass of a {@link CustomCollider} without moving the global
 * position of its shape. The center of mass is moved to the mouse position when a certain mouse button
 * is pressed.
 */
public class CenterOfMassController
{
    private int setCenterOfMassMouseButton;
    private Vector2D mousePos;

    private CustomCollider<?> collider;

    private CenterOfMassController(final int setCenterOfMassMouseButton, final Vector2D mousePos, final CustomCollider<?> collider) {
	this.setCenterOfMassMouseButton = setCenterOfMassMouseButton;
	this.mousePos = mousePos;
	this.collider = collider;
    }

    /**
     * Creates a CenterOfMassController with the first mouse button used to move the center of mass
     * of the given {@link CustomCollider}. The mouse position is considered to start at (0, 0)
     *
     * @param collider The collider to controll the center of mass for
     * @return The created CenterOfMassController
     */
    public static CenterOfMassController createWithDefaultConfigFor(final CustomCollider<?> collider) {
	return new CenterOfMassController(MouseEvent.BUTTON1, Vector2D.createZero(), collider);
    }

    /**
     * Updates the considered mouse position
     *
     * @param newMousePos The new mouse position
     */
    public void updateMousePos(final Vector2D newMousePos) {
	mousePos.set(newMousePos);
    }

    /**
     * If the correct button is pressed, this controller sets the center of mass of the
     * collider at the mouse position
     *
     * @param mouseEvent The mouse event indicating which button is pressed
     */
    public void mousePressed(final MouseEvent mouseEvent) {
	if (mouseEvent.getButton() == setCenterOfMassMouseButton) {
	    final Body body = collider.getBody();
	    final TranslatedCustomShape<?> shape = collider.getShape();

	    // Save the global position of the shape
	    final Vector2D globalShapePos = body.convertLocalToGlobalPoint(shape.getTranslation());
	    body.setPos(mousePos); // Move the underlying body
	    // Set the shape's position in the body's local space so that the shape retains the saved global position
	    shape.setTranslation(body.convertGlobalToLocalPoint(globalShapePos));
	}
    }
}
