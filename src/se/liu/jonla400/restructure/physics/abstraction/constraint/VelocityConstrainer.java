package se.liu.jonla400.restructure.physics.abstraction.constraint;

public interface VelocityConstrainer
{
    ActiveVelocityConstraint generateConstraint(double deltaTime);
}
