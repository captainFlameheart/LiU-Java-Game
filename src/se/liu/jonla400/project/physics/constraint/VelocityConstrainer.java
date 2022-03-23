package se.liu.jonla400.project.physics.constraint;

public interface VelocityConstrainer
{
    ActiveVelocityConstraint generateConstraint(double deltaTime);
}
