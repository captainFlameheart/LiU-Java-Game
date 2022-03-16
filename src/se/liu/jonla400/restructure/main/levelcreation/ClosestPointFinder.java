package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.math.Vector2D;

import java.util.Collection;
import java.util.Optional;

public class ClosestPointFinder
{
    private ClosestPointFinder() {
    }

    public static Optional<Vector2D> findClosestPoint(final Collection<Vector2D> points, final Vector2D referencePoint) {
	Vector2D closestPoint = null;
	double minDist = Double.POSITIVE_INFINITY;
	for (Vector2D point : points) {
	    final double dist = point.subtract(referencePoint).getMagnitudeSquared();
	    if (dist < minDist) {
		closestPoint = point;
		minDist = dist;
	    }
	}
	return Optional.ofNullable(closestPoint);
    }
}
