package se.liu.jonla400.project.restructure.physics.timestepping;

import se.liu.jonla400.project.restructure.physics.Body;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BodyTimeStepper implements TimeStepper
{
    private List<Body> bodies;

    public BodyTimeStepper(Body... bodies) {
	this.bodies = new ArrayList<>(Arrays.asList(bodies));
    }

    public boolean add(final Body body) {
	return bodies.add(body);
    }

    public boolean remove(final Object o) {
	return bodies.remove(o);
    }

    @Override public void tick(final double deltaTime) {
	for (Body body : bodies) {
	    body.tick(deltaTime);
	}
    }
}
