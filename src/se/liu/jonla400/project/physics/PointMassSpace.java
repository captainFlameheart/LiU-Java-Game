package se.liu.jonla400.project.physics;

import se.liu.jonla400.project.timestepping.TimeStepper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Represents a space where point masses reside. This class has methods for
 * adding and removing point masses, as well as time stepping which causes
 * each point mass to move for a given time. This class does not know about collisions.
 */
public class PointMassSpace implements TimeStepper
{
    private List<PointMass> pointMasses;

    /**
     * Creates a new space containing the given point masses
     *
     * @param pointMasses The initial point masses in this space
     */
    public PointMassSpace(PointMass... pointMasses) {
	this.pointMasses = new ArrayList<>(Arrays.asList(pointMasses));
    }

    /**
     * Adds a point mass
     *
     * @param pointMass The point mass to add
     */
    public void addPointMass(final PointMass pointMass) {
	pointMasses.add(pointMass);
    }

    /**
     * Removes a point mass
     *
     * @param pointMass The point mass to remove
     */
    public void removePointMass(final PointMass pointMass) {
	pointMasses.remove(pointMass);
    }

    /**
     * Advances time by moving each point mass in this space
     *
     * @param deltaTime	The amount of time that should be advanced
     */
    @Override public void tick(final double deltaTime) {
	if (deltaTime < 0) {
	    throw new IllegalArgumentException("Negative delta time: " + deltaTime);
	}

	for (PointMass pointMass : pointMasses) {
	    pointMass.tick(deltaTime);
	}
    }
}
