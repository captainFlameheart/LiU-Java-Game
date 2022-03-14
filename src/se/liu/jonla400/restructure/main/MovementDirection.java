package se.liu.jonla400.restructure.main;

import se.liu.jonla400.restructure.math.Vector2D;

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

    public Vector2D getDirVector() {
	return dirVector.copy();
    }
}
