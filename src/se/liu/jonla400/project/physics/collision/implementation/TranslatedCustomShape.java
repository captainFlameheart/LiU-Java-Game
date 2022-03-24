package se.liu.jonla400.project.physics.collision.implementation;

import se.liu.jonla400.project.math.Vector2D;

/**
 * Represents a {@link CustomShape} translated by an arbitrary vector. Each line segment
 * contains custom user data.
 *
 * @param <T> The type of user data associated with each line segment
 */
public class TranslatedCustomShape<T>
{
    private Vector2D translation;
    private CustomShape<T> shape;

    private TranslatedCustomShape(final Vector2D translation, final CustomShape<T> shape) {
        this.translation = translation;
        this.shape = shape;
    }

    /**
     * Creates a new TranslatedCustomShape. No reference is kept of the input translation.
     *
     * @param translation The translation
     * @param shape The shape
     * @param <T> The type of user input associated with each line segment
     * @return The created TranslatedCustomShape
     */
    public static <T> TranslatedCustomShape<T> copyTranslation(final Vector2D translation, final CustomShape<T> shape) {
        return new TranslatedCustomShape<>(translation.copy(), shape);
    }

    /**
     * @return A read-only view of the translation
     */
    public Vector2D getTranslation() {
        return translation.copy();
    }

    /**
     * Sets the translation. No reference is kept to the given translation.
     *
     * @param translation The new translation.
     */
    public void setTranslation(final Vector2D translation) {
        this.translation.set(translation);
    }

    /**
     * @return The shape without the translation
     */
    public CustomShape<T> getShape() {
        return shape;
    }
}
