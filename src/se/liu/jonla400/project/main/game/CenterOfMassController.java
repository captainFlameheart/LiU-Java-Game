package se.liu.jonla400.project.main.game;

import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.physics.main.Body;
import se.liu.jonla400.project.physics.collision.implementation.CustomCollider;
import se.liu.jonla400.project.physics.collision.implementation.TranslatedCustomShape;

import java.awt.event.MouseEvent;

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

    public static CenterOfMassController createWithDefaultConfigFor(final CustomCollider<?> collider) {
	return new CenterOfMassController(MouseEvent.BUTTON1, Vector2D.createZero(), collider);
    }

    public void updateMousePos(final Vector2D newMousePos) {
	mousePos.set(newMousePos);
    }

    public void mousePressed(final MouseEvent mouseEvent) {
	if (mouseEvent.getButton() == setCenterOfMassMouseButton) {
	    final Body body = collider.getBody();
	    final TranslatedCustomShape<?> shape = collider.getShape();

	    final Vector2D globalShapePos = body.convertLocalPointToGlobalPoint(shape.getTranslation());
	    body.setPos(mousePos);
	    shape.setTranslation(body.convertGlobalToLocalPoint(globalShapePos));
	}
    }
}
