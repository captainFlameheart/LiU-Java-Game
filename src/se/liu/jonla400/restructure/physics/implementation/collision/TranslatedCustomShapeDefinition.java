package se.liu.jonla400.restructure.physics.implementation.collision;

import se.liu.jonla400.restructure.math.Vector2D;

public class TranslatedCustomShapeDefinition
{
    private Vector2D translation;
    private CustomShapeDefinition customShapeDefinition;

    private TranslatedCustomShapeDefinition(final Vector2D translation, final CustomShapeDefinition customShapeDefinition) {
	this.translation = translation;
	this.customShapeDefinition = customShapeDefinition;
    }

    public static TranslatedCustomShapeDefinition create(final Vector2D translation, final CustomShapeDefinition customShapeDefinition) {
	return new TranslatedCustomShapeDefinition(translation.copy(), customShapeDefinition);
    }

    public static TranslatedCustomShapeDefinition createAtOrigin(final CustomShapeDefinition customShapeDefinition) {
	return create(Vector2D.createZeroVector(), customShapeDefinition);
    }

    public Vector2D getTranslation() {
	return translation;
    }

    public void setTranslation(final Vector2D translation) {
	this.translation = translation;
    }

    public CustomShapeDefinition getCustomShapeDefinition() {
	return customShapeDefinition;
    }

    public void setCustomShapeDefinition(final CustomShapeDefinition customShapeDefinition) {
	this.customShapeDefinition = customShapeDefinition;
    }
}
