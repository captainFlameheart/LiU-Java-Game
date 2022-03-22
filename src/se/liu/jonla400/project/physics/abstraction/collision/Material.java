package se.liu.jonla400.project.physics.abstraction.collision;

public class Material
{
    private double bounceCoefficient;
    private double frictionCoefficient;

    public Material(final double bounceCoefficient, final double frictionCoefficient) {
	this.bounceCoefficient = bounceCoefficient;
	this.frictionCoefficient = frictionCoefficient;
    }

    public double getBounceCoefficient() {
	return bounceCoefficient;
    }

    public double getFrictionCoefficient() {
	return frictionCoefficient;
    }
}
