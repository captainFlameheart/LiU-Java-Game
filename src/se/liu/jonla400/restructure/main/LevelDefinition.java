package se.liu.jonla400.restructure.main;

import se.liu.jonla400.restructure.math.Vector2D;
import se.liu.jonla400.restructure.physics.implementation.collision.CustomShapeDefinition;

public class LevelDefinition
{
    private GlobalLevelConfiguration sharedConfiguration;
    private Vector2D levelPos;
    private CustomShapeDefinition levelShapeDefinition;
    private Vector2D circlePos;
    private double circleRadius;
    private double circleMass;
    private double circleAngularMass;
    private RectangularRegion preferredRectangularRegion;

    public LevelDefinition(final GlobalLevelConfiguration sharedConfiguration, final Vector2D levelPos,
			   final CustomShapeDefinition levelShapeDefinition, final Vector2D circlePos, final double circleRadius,
			   final double circleMass, final double circleAngularMass, final RectangularRegion preferredRectangularRegion)
    {
	this.sharedConfiguration = sharedConfiguration;
	this.levelPos = levelPos;
	this.levelShapeDefinition = levelShapeDefinition;
	this.circlePos = circlePos;
	this.circleRadius = circleRadius;
	this.circleMass = circleMass;
	this.circleAngularMass = circleAngularMass;
	this.preferredRectangularRegion = preferredRectangularRegion;
    }

    public double getLevelMass() {
	return sharedConfiguration.getMass();
    }

    public double getAngularMass() {
	return sharedConfiguration.getAngularMass();
    }

    public double getMovementSpeed() {
	return sharedConfiguration.getMovementSpeed();
    }

    public double getMaxMovementAcc() {
	return sharedConfiguration.getMaxMovementAcc();
    }

    public double getAngularSpeed() {
	return sharedConfiguration.getAngularSpeed();
    }

    public double getMaxAngularAcc() {
	return sharedConfiguration.getMaxAngularAcc();
    }

    public double getScaleFactor() {
	return sharedConfiguration.getScaleFactor();
    }

    public Vector2D getLevelPos() {
	return levelPos;
    }

    public void setLevelPos(final Vector2D levelPos) {
	this.levelPos = levelPos;
    }

    public CustomShapeDefinition getLevelShapeDefinition() {
	return levelShapeDefinition;
    }

    public void setLevelShapeDefinition(final CustomShapeDefinition levelShapeDefinition) {
	this.levelShapeDefinition = levelShapeDefinition;
    }

    public Vector2D getCirclePos() {
	return circlePos;
    }

    public void setCirclePos(final Vector2D circlePos) {
	this.circlePos = circlePos;
    }

    public double getCircleRadius() {
	return circleRadius;
    }

    public void setCircleRadius(final double circleRadius) {
	this.circleRadius = circleRadius;
    }

    public double getCircleMass() {
	return circleMass;
    }

    public void setCircleMass(final double circleMass) {
	this.circleMass = circleMass;
    }

    public double getCircleAngularMass() {
	return circleAngularMass;
    }

    public void setCircleAngularMass(final double circleAngularMass) {
	this.circleAngularMass = circleAngularMass;
    }

    public RectangularRegion getPreferredDrawRegion() {
	return preferredRectangularRegion;
    }

    public void setPreferredDrawRegion(final RectangularRegion preferredRectangularRegion) {
	this.preferredRectangularRegion = preferredRectangularRegion;
    }
}
