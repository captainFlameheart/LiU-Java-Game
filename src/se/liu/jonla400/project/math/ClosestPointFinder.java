package se.liu.jonla400.project.math;

import java.util.Collection;
import java.util.Iterator;
import java.util.Optional;

/**
 * Represents an object that can find its closest point to a given point. Also contains
 * static methods for finding the closest object to a given point from a set of objects each implementing
 * this interface.
 */
public interface ClosestPointFinder
{
    /**
     * Returns the closest point to the given point
     *
     * @param point The reference point
     * @return The closest point
     */
    Vector2D findClosestPointTo(final Vector2D point);

    /**
     * Returns the object that has the closest point to the given point. If no objects exist, returns
     * Optional.empty()
     *
     * @param objects The objects to pick between
     * @param referencePoint The point to compare each object to
     * @param <T> The type of object
     * @return The object closest to the given point, if any objects exist
     */
    public static <T extends ClosestPointFinder> Optional<T> findClosestObject(final Collection<T> objects, final Vector2D referencePoint) {
	return findClosestObject(objects.iterator(), referencePoint);
    }

    /**
     * Returns the object that has the closest point to the given point. If no objects exist, returns
     * Optional.empty()
     *
     * @param objects The objects to pick between
     * @param referencePoint The point to compare each object to
     * @param <T> The type of object
     * @return The object closest to the given point, if any objects exist
     */
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
