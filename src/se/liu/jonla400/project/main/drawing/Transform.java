package se.liu.jonla400.project.main.drawing;

import se.liu.jonla400.project.math.Vector2D;

/**
 * Represents a grouping of a translation, rotation and scale. This is used by {@link TransformedDrawer}
 * to determine how to transform a {@link java.awt.Graphics2D} object before performing the drawing
 * procedure of a {@link Drawer}
 */
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

    /**
     * Creates a transform with the given translation and with no rotation and a scale of 1.
     * The created transform does not hold a reference to the given translation vector.
     *
     * @param translation The translation of the transform
     * @return The transform representing the given translation
     */
    public static Transform createWithTranslation(final Vector2D translation) {
	return new Transform(translation.copy(), 0, 1);
    }

    /**
     * Creates a transform with the given translation and rotation, and a scale of 1.
     * The created transform does not hold a reference to the given translation vector.
     *
     * @param translation The translation of the transform
     * @param rotation The rotation of the transform (interpreted by {@link TransformedDrawer} to be in radians)
     * @return The transform representing the translation and rotation
     */
    public static Transform createWithTranslationAndRotation(final Vector2D translation, final double rotation) {
	return new Transform(translation.copy(), rotation, 1);
    }

    /**
     * @return A read only view of the translation
     */
    public Vector2D getTranslation() {
	return translation.copy();
    }

    /**
     * @return The rotation
     */
    public double getRotation() {
	return rotation;
    }

    /**
     * @return The scale
     */
    public double getScale() {
	return scale;
    }
}
