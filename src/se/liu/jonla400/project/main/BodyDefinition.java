package se.liu.jonla400.project.main;

import se.liu.jonla400.project.math.Vector2D;

public class BodyDefinition
{
    private Vector2D pos;
    private double mass;
    private double angularMass;

    private BodyDefinition(final Vector2D pos, final double mass, final double angularMass) {
        this.pos = pos;
        this.mass = mass;
        this.angularMass = angularMass;
    }

    public static BodyDefinition create(final Vector2D pos, final double mass, final double angularMass) {
        return new BodyDefinition(pos.copy(), mass, angularMass);
    }

    public static BodyDefinition createWithDefaultMasses(final Vector2D pos) {
        return create(pos, 1, 1);
    }


    public Vector2D getPos() {
        return pos.copy();
    }

    public void setPos(final Vector2D pos) {
        this.pos = pos.copy();
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
}
