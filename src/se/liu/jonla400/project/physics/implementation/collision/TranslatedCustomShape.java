package se.liu.jonla400.project.physics.implementation.collision;

import se.liu.jonla400.project.math.Vector2D;

public class TranslatedCustomShape<T>
{
    private Vector2D translation;
    private CustomShape<T> shape;

    public TranslatedCustomShape(final Vector2D translation, final CustomShape<T> shape) {
        this.translation = translation;
        this.shape = shape;
    }

    public Vector2D getTranslation() {
        return translation.copy();
    }

    public void setTranslation(final Vector2D translation) {
        this.translation.set(translation);
    }

    public CustomShape<T> getShape() {
        return shape;
    }
}
