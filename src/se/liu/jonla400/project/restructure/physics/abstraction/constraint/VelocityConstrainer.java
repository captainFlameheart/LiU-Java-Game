package se.liu.jonla400.project.restructure.physics.abstraction.constraint;

public interface VelocityConstrainer
{
    ActiveVelocityConstraint generateConstraint(double deltaTime);
}
