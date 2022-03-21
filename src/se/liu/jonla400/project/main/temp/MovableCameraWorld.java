package se.liu.jonla400.project.main.temp;

import se.liu.jonla400.project.main.RectangularRegion;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class MovableCameraWorld<T extends World> implements FilmedWorld
{
    private final static int DRAG_WORLD_MOUSE_BUTTON = MouseEvent.BUTTON3;

    private T world;
    private RectangularRegion camera;

    private boolean worldFollowsMouse;
    private Vector2D mousePos;

    private MovableCameraWorld(final T world, final RectangularRegion camera, final boolean worldFollowsMouse, final Vector2D mousePos) {
	this.world = world;
	this.camera = camera;
	this.worldFollowsMouse = worldFollowsMouse;
	this.mousePos = mousePos;
    }

    public static <T extends World> MovableCameraWorld<T> create(final T world, final RectangularRegion camera) {
	return new MovableCameraWorld<>(world, camera, false, Vector2D.createZero());
    }

    public T getWorld() {
	return world;
    }

    @Override public RectangularRegion getCamera() {
	return camera.copy();
    }

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

    @Override public void mousePressed(final MouseEvent mouseEvent) {
	setWorldFollowsMouseIfCorrectMouseButton(mouseEvent, true);
	world.mousePressed(mouseEvent);
    }

    @Override public void mouseReleased(final MouseEvent mouseEvent) {
	setWorldFollowsMouseIfCorrectMouseButton(mouseEvent, false);
	world.mouseReleased(mouseEvent);
    }

    private void setWorldFollowsMouseIfCorrectMouseButton(final MouseEvent mouseEvent, boolean pressed) {
	if (mouseEvent.getButton() == DRAG_WORLD_MOUSE_BUTTON) {
	    worldFollowsMouse = pressed;
	}
    }

    @Override public void mouseWheelMoved(final MouseWheelEvent mouseWheelEvent) {
	final double scaleFactor = 1.1;
	final double scale = Math.pow(scaleFactor, mouseWheelEvent.getPreciseWheelRotation());
	camera.scale(scale);
    }

    @Override public void keyPressed(final KeyEvent keyEvent) {
	world.keyPressed(keyEvent);
    }

    @Override public void keyReleased(final KeyEvent keyEvent) {
	world.keyReleased(keyEvent);
    }

    @Override public void tick(final double deltaTime) {
	world.tick(deltaTime);
    }

    @Override public void draw(final Graphics2D g, final RectangularRegion region) {
	world.draw(g, region);
    }
}
