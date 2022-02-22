package se.liu.jonla400.project.physics;

import se.liu.jonla400.project.timestepping.TimeStepper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PointMassSpace implements TimeStepper
{
    private List<PointMass> pointMasses;

    public PointMassSpace(PointMass... pointMasses) {
	this.pointMasses = new ArrayList<>(Arrays.asList(pointMasses));
    }

    public void addPointMass(final PointMass pointMass) {
	pointMasses.add(pointMass);
    }

    public void removePointMass(final PointMass pointMass) {
	pointMasses.remove(pointMass);
    }

    @Override public void tick(final double deltaTime) {
	if (deltaTime < 0) {
	    throw new IllegalArgumentException("Negative delta time: " + deltaTime);
	}

	for (PointMass pointMass : pointMasses) {
	    pointMass.tick(deltaTime);
	}
    }
}
