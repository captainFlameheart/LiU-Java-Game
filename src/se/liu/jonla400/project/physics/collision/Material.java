package se.liu.jonla400.project.physics.collision;

/**
 * Represents material properties of a collision: bounce and friction coefficients. The bounce
 * coefficient (coefficient of restitution) determines how much of the velocity to be negated
 * along the collisions normal, and should be between 0 and 1 for realistic results. The friction
 * coefficient represents the maximum friction impulse relative to the normal impulse. A friction
 * coefficent of 0.5 means that the maximum friction impulse is half of the normal impulse.
 */
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
