package se.liu.jonla400.project.main.world;

import se.liu.jonla400.project.math.RectangularRegion;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Pairs {@link World} with a movable camera, making it a {@link FilmedWorld}. When pressing
 * and holding a mouse button, the world follows the mouse. So when the mouse is moved to the
 * left, the world is moved to the left which in turn means that the camera moves to the right
 * relative to the world.
 *
 * @param <T> The type of world combined with a movable camera
 */
public class WorldWithMovableCamera<T extends World> implements FilmedWorld
{
    private T world;
    private RectangularRegion camera;
    private int dragWorldMouseButton;

    private boolean worldFollowsMouse;
    private Vector2D mousePos;

    private WorldWithMovableCamera(final T world, final RectangularRegion camera, final int dragWorldMouseButton,
				  final boolean worldFollowsMouse, final Vector2D mousePos)
    {
	this.world = world;
	this.camera = camera;
	this.dragWorldMouseButton = dragWorldMouseButton;
	this.worldFollowsMouse = worldFollowsMouse;
	this.mousePos = mousePos;
    }

    /**
     * Combines the given world with the given camera which can later be moved by pressing the
     * right mouse button.
     *
     * @param world The world
     * @param camera The camera to combine the world with
     * @param <T> The type of the world
     * @return The created WorldWithMovableCamera
     */
    public static <T extends World> WorldWithMovableCamera<T> create(final T world, final RectangularRegion camera) {
	return new WorldWithMovableCamera<>(world, camera, MouseEvent.BUTTON3, false, Vector2D.createZero());
    }

    /**
     * @return The world without the knowledge about the camera
     */
    public T getWorld() {
	return world;
    }

    /**
     * @return A read-only view of the camera
     */
    @Override public RectangularRegion getCamera() {
	return camera.copy();
    }

    /**
     * If the right mouse button is pressed, moves the camera to make the world appear
     * to move along with the mouse. Otherwise updates the considered mouse position by the
     * camera-unaware world.
     *
     * @param newMousePos The new mouse position
     */
    @Override public void updateMousePos(final Vector2D newMousePos) {
	if (worldFollowsMouse) {
	    final Vector2D deltaWorldPos = newMousePos.subtract(mousePos);
	    final Vector2D deltaCameraPos = deltaWorldPos.negate();
	    camera.move(deltaCameraPos);
	} else {
	    world.updateMousePos(newMousePos);
	    mousePos.set(newMousePos);
	}
    }

    /**
     * Starts moving the camera if the right mouse button is pressed.
     * Also tells the camera-unaware world about the mouse press.
     *
     * @param mouseEvent Contains the information about the mouse press
     */
    @Override public void mousePressed(final MouseEvent mouseEvent) {
	setWorldFollowsMouseIfCorrectButton(mouseEvent, true);
	world.mousePressed(mouseEvent);
    }

    /**
     * Stops moving the camera if the right mouse button is released.
     * Also tells the camera-unaware world about the mouse release.
     *
     * @param mouseEvent Contains the information about the mouse release
     */
    @Override public void mouseReleased(final MouseEvent mouseEvent) {
	setWorldFollowsMouseIfCorrectButton(mouseEvent, false);
	world.mouseReleased(mouseEvent);
    }

    private void setWorldFollowsMouseIfCorrectButton(final MouseEvent mouseEvent, boolean pressed) {
	if (mouseEvent.getButton() == dragWorldMouseButton) {
	    worldFollowsMouse = pressed;
	}
    }

    /**
     * Scales the camera
     *
     * @param mouseWheelEvent Contains the information about the mouse wheel movement
     */
    @Override public void mouseWheelMoved(final MouseWheelEvent mouseWheelEvent) {
	final double scaleFactor = 1.1;
	final double scale = Math.pow(scaleFactor, mouseWheelEvent.getPreciseWheelRotation());
	camera.scale(scale);
    }

    /**
     * Lets the camera-unaware world handle the key press
     *
     * @param keyEvent Contains the information about the key press
     */
    @Override public void keyPressed(final KeyEvent keyEvent) {
	world.keyPressed(keyEvent);
    }

    /**
     * Lets the camera-unaware world handle the key release
     *
     * @param keyEvent Contains the information about the key release
     */
    @Override public void keyReleased(final KeyEvent keyEvent) {
	world.keyReleased(keyEvent);
    }

    /**
     * Ticks the camera-unaware world forward
     *
     * @param deltaTime The amount of time to tick forward
     */
    @Override public void tick(final double deltaTime) {
	world.tick(deltaTime);
    }

    /**
     * Draws the camera unaware world onto the {@link Graphics2D} object at the given region.
     * This method might draw outside the region too.
     *
     * @param g The graphics object to draw to
     * @param region The region required to draw to, but not limited by
     */
    @Override public void draw(final Graphics2D g, final RectangularRegion region) {
	world.draw(g, region);
    }
}
