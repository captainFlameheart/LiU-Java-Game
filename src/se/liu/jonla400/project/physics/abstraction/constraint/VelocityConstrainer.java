package se.liu.jonla400.project.physics.abstraction.constraint;

public interface VelocityConstrainer
{
    ActiveVelocityConstraint generateConstraint(double deltaTime);
}
