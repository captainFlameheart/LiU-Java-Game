package se.liu.jonla400.restructure.main;

import se.liu.jonla400.restructure.math.Vector2D;

public class LevelDefinition
{
    private GlobalLevelConfiguration sharedConfiguration;
    private Vector2D pos;
    private DrawRegion preferredDrawRegion;

    public LevelDefinition(final GlobalLevelConfiguration sharedConfiguration, final Vector2D pos, final DrawRegion preferredDrawRegion) {
	this.sharedConfiguration = sharedConfiguration;
	this.pos = pos;
	this.preferredDrawRegion = preferredDrawRegion;
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

    public Vector2D getPos() {
	return pos;
    }

    public void setPos(final Vector2D pos) {
	this.pos = pos;
    }

    public DrawRegion getPreferredDrawRegion() {
	return preferredDrawRegion;
    }

    public void setPreferredDrawRegion(final DrawRegion preferredDrawRegion) {
	this.preferredDrawRegion = preferredDrawRegion;
    }
}
