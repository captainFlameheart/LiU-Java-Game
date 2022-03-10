package se.liu.jonla400.project.restructure.physics.constraints;

public interface VelocityConstrainer
{
    ActiveVelocityConstraint generateConstraint(double deltaTime);
}
