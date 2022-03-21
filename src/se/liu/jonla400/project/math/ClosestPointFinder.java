package se.liu.jonla400.project.math;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

public interface ClosestPointFinder
{
    Vector2D findClosestPointTo(final Vector2D point);

    public static <T extends ClosestPointFinder> Optional<T> findClosestObject(final Collection<T> objects, final Vector2D referencePoint) {
	return findClosestObject(objects.iterator(), referencePoint);
    }

    public static <T extends ClosestPointFinder> Optional<T> findClosestObject(final Iterator<T> objects, final Vector2D referencePoint) {
	T closestObject = null;
	double minDist = Double.POSITIVE_INFINITY;
	while (objects.hasNext()) {
	    final T object = objects.next();
	    final Vector2D closestPoint = object.findClosestPointTo(referencePoint);
	    final double dist = closestPoint.getDistanceSquaredTo(referencePoint);
	    if (dist < minDist) {
		closestObject = object;
		minDist = dist;
	    }
	}
	return Optional.ofNullable(closestObject);
    }
}
