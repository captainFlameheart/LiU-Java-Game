package se.liu.jonla400.project.main.game;

import se.liu.jonla400.project.math.Vector2D;

/**
 * Represents one of four movement directions of a level: left, right, up and down.
 */
public enum MovementDirection
{
    LEFT(-1, 0),
    RIGHT(1, 0),
    UP(0, 1),
    DOWN(0, -1);

    private final Vector2D dirVector;

    MovementDirection(final int dirX, final int dirY) {
	this.dirVector = Vector2D.createCartesian(dirX, dirY);
    }

    /**
     * Returns a unit vector representing this direction. The positive x-axis is considered to
     * point to the right, and the positive y-axis is considered to point up
     *
     * @return The direction vector
     */
    public Vector2D getDirVector() {
	return dirVector.copy();
    }
}
