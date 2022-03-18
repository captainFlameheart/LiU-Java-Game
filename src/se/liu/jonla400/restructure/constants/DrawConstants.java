package se.liu.jonla400.restructure.constants;

import java.awt.*;

public class DrawConstants
{
    private final static float DEFAULT_STROKE_WIDTH = 0.1f;
    private final static Color BALL_FILL_COLOR = Color.BLUE;
    private final static Color BALL_STROKE_COLOR = Color.BLACK;

    private DrawConstants() {}

    public static float getDefaultStrokeWidth() {
	return DEFAULT_STROKE_WIDTH;
    }

    public static Color getBallFillColor() {
	return BALL_FILL_COLOR;
    }

    public static Color getBallStrokeColor() {
	return BALL_STROKE_COLOR;
    }
}
