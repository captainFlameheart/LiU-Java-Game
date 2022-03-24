package se.liu.jonla400.project.main.game;

/**
 * Represents one of rotation directions of a level: left and right
 */
public enum RotationDirection
{
    LEFT(1), RIGHT(-1);

    private final int sign;

    RotationDirection(final int sign) {
	this.sign = sign;
    }

    /**
     * Returns the sign of the rotation direction: left -> 1, right -> -1
     *
     * @return The sign of the rotation direction
     */
    public int getSign() {
	return sign;
    }
}
