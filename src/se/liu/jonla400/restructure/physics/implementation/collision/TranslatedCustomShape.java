package se.liu.jonla400.restructure.physics.implementation.collision;

import se.liu.jonla400.restructure.math.Vector2D;

public class TranslatedCustomShape
{
    private Vector2D translation;
    private CustomShape shape;

    private TranslatedCustomShape(final Vector2D translation, final CustomShape shape) {
        this.translation = translation;
        this.shape = shape;
    }

    public static TranslatedCustomShape createFromDefinition(final TranslatedCustomShapeDefinition definition) {
        final Vector2D translation = definition.getTranslation();
        final CustomShape customShape = CustomShape.createFromDefinition(definition.getCustomShapeDefinition());
        return new TranslatedCustomShape(translation, customShape);
    }

    public Vector2D getTranslation() {
        return translation.copy();
    }

    public void setTranslation(final Vector2D translation) {
        this.translation.set(translation);
    }

    public CustomShape getShape() {
        return shape;
    }
}
