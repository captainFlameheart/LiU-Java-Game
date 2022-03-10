package se.liu.jonla400.project.physics.collision.collisiondetection.types.fresh;

import se.liu.jonla400.project.physics.PointMass;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class PlaneCollider implements Iterable<Plane>
{
    private PointMass pointMass;
    private Plane[] planes;

    public PlaneCollider(final PointMass pointMass, final Plane... planes) {
	this.pointMass = pointMass;
	this.planes = planes.clone();
    }

    public PointMass getPointMass() {
	return pointMass;
    }

    public Collection<Plane> getPlanes() {
	return Arrays.asList(planes);
    }

    @NotNull @Override public Iterator<Plane> iterator() {
	return Arrays.stream(planes).iterator();
    }
}
