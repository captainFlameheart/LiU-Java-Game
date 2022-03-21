package se.liu.jonla400.project.constants;

import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;

public class Constants
{
    private final static double LEVEL_MASS = 50;
    private final static double LEVEL_SPEED = 10;
    private final static double LEVEL_MAX_ACC = 20;

    private final static double LEVEL_ANGULAR_MASS = 50;
    private final static double LEVEL_ANGULAR_SPEED = 2;
    private final static double LEVEL_MAX_ANGULAR_ACC = 10;

    private final static Vector2D BALL_SPAWN_POS = Vector2D.createZero();
    private final static double BALL_RADIUS = 0.5;

    private final static float DEFAULT_STROKE_WIDTH = 0.1f;
    private final static float LEVEL_CROSS_RADIUS = 1;
    private final static Color LEVEL_CROSS_COLOR = Color.RED;
    private final static float LEVEL_CROSS_STROKE_WIDTH = DEFAULT_STROKE_WIDTH;
    private final static Color BALL_FILL_COLOR = Color.RED;
    private final static Color BALL_STROKE_COLOR = Color.BLACK;
    private final static double BALL_CROSS_RADIUS = 0.1;
    private final static Color BALL_CROSS_COLOR = Color.BLACK;
    private final static float BALL_CROSS_STROKE_WIDTH = 0.03f;

    private Constants() {}

    public final static double getLevelMass() {
        return LEVEL_MASS;
    }

    public final static double getLevelSpeed() {
        return LEVEL_SPEED;
    }

    public final static double getLevelMaxAcc() {
        return LEVEL_MAX_ACC;
    }

    public final static double getLevelAngularMass() {
        return LEVEL_ANGULAR_MASS;
    }

    public final static double getLevelAngularSpeed() {
        return LEVEL_ANGULAR_SPEED;
    }

    public final static double getLevelMaxAngularAcc() {
        return LEVEL_MAX_ANGULAR_ACC;
    }

    public final static Vector2D getBallSpawnPos() {
	return BALL_SPAWN_POS.copy();
    }

    public final static double getBallRadius() {
	return BALL_RADIUS;
    }

    //public final static double getBallMass() {
	//return BALL_MASS;
    //}

    //public final static double getBallAngularMass() {
	//return BALL_ANGULAR_MASS;
    //}

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
