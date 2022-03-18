package se.liu.jonla400.restructure.constants;

import se.liu.jonla400.restructure.math.Vector2D;

public class PhysicsConstants
{
    private final static double LEVEL_MASS = 50;
    private final static double LEVEL_ANGULAR_MASS = 50;
    private final static double LEVEL_SPEED = 10;
    private final static double LEVEL_MAX_ACC = 20;
    private final static double LEVEL_ANGULAR_SPEED = 2;
    private final static double LEVEL_MAX_ANGULAR_ACC = 10;

    private final static Vector2D BALL_SPAWN_POS = Vector2D.createZero();
    private final static double BALL_RADIUS = 0.5;
    private final static double BALL_MASS = 1;
    private final static double BALL_ANGULAR_MASS = 0.01;

    private PhysicsConstants() {}

    public final static double getLevelMass() {
        return LEVEL_MASS;
    }

    public final static double getLevelAngularMass() {
        return LEVEL_ANGULAR_MASS;
    }

    public final static double getLevelSpeed() {
        return LEVEL_SPEED;
    }

    public final static double getLevelMaxAcc() {
        return LEVEL_MAX_ACC;
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

    public final static double getBallMass() {
	return BALL_MASS;
    }

    public final static double getBallAngularMass() {
	return BALL_ANGULAR_MASS;
    }
}
