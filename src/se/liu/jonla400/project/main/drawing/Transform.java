package se.liu.jonla400.project.main.drawing;

import se.liu.jonla400.project.math.Vector2D;

public class Transform
{
    private Vector2D translation;
    private double rotation;
    private double scale;

    private Transform(final Vector2D translation, final double rotation, final double scale) {
	this.translation = translation;
	this.rotation = rotation;
	this.scale = scale;
    }

    public static Transform createWithTranslation(final Vector2D translation) {
	return new Transform(translation.copy(), 0, 1);
    }

    public static Transform createWithTranslationAndRotation(final Vector2D translation, final double rotation) {
	return new Transform(translation.copy(), rotation, 1);
    }

    public Vector2D getTranslation() {
	return translation.copy();
    }

    public double getRotation() {
	return rotation;
    }

    public double getScale() {
	return scale;
    }
}
