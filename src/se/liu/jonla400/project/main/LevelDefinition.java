package se.liu.jonla400.project.main;

import se.liu.jonla400.project.physics.implementation.collision.CustomShapeDefinition;

public class LevelDefinition
{
    private DrawRegion minDrawRegion;
    private int velConstraintIterations;
    private BodyDefinition circleBodyDefinition;
    private double circleRadius;
    private BodyDefinition levelBodyDefinition;
    private CustomShapeDefinition levelShapeDefinition;

    public LevelDefinition(final DrawRegion minDrawRegion, final int velConstraintIterations, final BodyDefinition circleBodyDefinition,
			   final double circleRadius, final BodyDefinition levelBodyDefinition,
			   final CustomShapeDefinition levelShapeDefinition)
    {
	this.minDrawRegion = minDrawRegion;
	this.velConstraintIterations = velConstraintIterations;
	this.circleBodyDefinition = circleBodyDefinition;
	this.circleRadius = circleRadius;
	this.levelBodyDefinition = levelBodyDefinition;
	this.levelShapeDefinition = levelShapeDefinition;
    }

    public DrawRegion getMinDrawRegion() {
	return minDrawRegion;
    }

    public void setMinDrawRegion(final DrawRegion minDrawRegion) {
	this.minDrawRegion = minDrawRegion;
    }

    public int getVelConstraintIterations() {
	return velConstraintIterations;
    }

    public void setVelConstraintIterations(final int velConstraintIterations) {
	this.velConstraintIterations = velConstraintIterations;
    }

    public BodyDefinition getCircleBodyDefinition() {
	return circleBodyDefinition;
    }

    public void setCircleBodyDefinition(final BodyDefinition circleBodyDefinition) {
	this.circleBodyDefinition = circleBodyDefinition;
    }

    public double getCircleRadius() {
	return circleRadius;
    }

    public void setCircleRadius(final double circleRadius) {
	this.circleRadius = circleRadius;
    }

    public BodyDefinition getLevelBodyDefinition() {
	return levelBodyDefinition;
    }

    public void setLevelBodyDefinition(final BodyDefinition levelBodyDefinition) {
	this.levelBodyDefinition = levelBodyDefinition;
    }

    public CustomShapeDefinition getLevelShapeDefinition() {
	return levelShapeDefinition;
    }

    public void setLevelShapeDefinition(final CustomShapeDefinition levelShapeDefinition) {
	this.levelShapeDefinition = levelShapeDefinition;
    }
}
