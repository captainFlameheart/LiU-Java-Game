package se.liu.jonla400.restructure.main;

public class GlobalLevelConfiguration
{
    private double mass;
    private double angularMass;

    private double movementSpeed;
    private double maxMovementAcc;

    private double angularSpeed;
    private double maxAngularAcc;

    private double scaleFactor;

    public GlobalLevelConfiguration(final double mass, final double angularMass, final double movementSpeed, final double maxMovementAcc,
                                    final double angularSpeed, final double maxAngularAcc, final double scaleFactor)
    {
        this.mass = mass;
        this.angularMass = angularMass;
        this.movementSpeed = movementSpeed;
        this.maxMovementAcc = maxMovementAcc;
        this.angularSpeed = angularSpeed;
        this.maxAngularAcc = maxAngularAcc;
        this.scaleFactor = scaleFactor;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(final double mass) {
        this.mass = mass;
    }

    public double getAngularMass() {
        return angularMass;
    }

    public void setAngularMass(final double angularMass) {
        this.angularMass = angularMass;
    }

    public double getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(final double movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public double getMaxMovementAcc() {
        return maxMovementAcc;
    }

    public void setMaxMovementAcc(final double maxMovementAcc) {
        this.maxMovementAcc = maxMovementAcc;
    }

    public double getAngularSpeed() {
        return angularSpeed;
    }

    public void setAngularSpeed(final double angularSpeed) {
        this.angularSpeed = angularSpeed;
    }

    public double getMaxAngularAcc() {
        return maxAngularAcc;
    }

    public void setMaxAngularAcc(final double maxAngularAcc) {
        this.maxAngularAcc = maxAngularAcc;
    }

    public double getScaleFactor() {
        return scaleFactor;
    }

    public void setScaleFactor(final double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }
}
