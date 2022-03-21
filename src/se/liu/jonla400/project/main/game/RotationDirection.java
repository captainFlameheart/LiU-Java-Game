package se.liu.jonla400.project.main.game;

public enum RotationDirection
{
    LEFT(1), RIGHT(-1);

    private final int sign;

    RotationDirection(final int sign) {
	this.sign = sign;
    }

    public int getSign() {
	return sign;
    }
}
