package se.liu.jonla400.project.physics.collision.collisiondetection.types;

import se.liu.jonla400.project.math.Interval;
import se.liu.jonla400.project.math.Vector2D;
import se.liu.jonla400.project.math.rootfinding.ContinousErrorStateFunction;
import se.liu.jonla400.project.math.rootfinding.ErrorState;
import se.liu.jonla400.project.math.rootfinding.InputToErrorStateMap;
import se.liu.jonla400.project.math.rootfinding.SmallErrorFinder;
import se.liu.jonla400.project.physics.PointMass;
import se.liu.jonla400.project.physics.collision.collisiondetection.ContinousCollisionDetector;
import se.liu.jonla400.project.physics.collision.collisioninfo.UpcomingCollision;

import java.util.Optional;

public class CircleVsPointCollisionDetector implements ContinousCollisionDetector
{
    private PointMass pointMass;
    private double radius;

    private Vector2D point;

    public CircleVsPointCollisionDetector(final PointMass pointMass, final double radius, final Vector2D point) {
	this.pointMass = pointMass;
	this.radius = radius;
	this.point = point;
    }

    @Override public Optional<UpcomingCollision> detectCollision(final double upperTimeLimit) {
	final Vector2D pointMassPos = pointMass.getPos();

	double time = 0;
	while (time < upperTimeLimit) {

	    final Vector2D pointToCircleCenter = pointMass.getPos().subtract(point);
	    final double dist = pointToCircleCenter.getMagnitude();
	    final Vector2D normal = pointToCircleCenter.divide(dist);
	    final double separation = dist - radius;
	    if (separation <= 0) {
		return Optional.of(createCollision(normal, separation, 0));
	    }

	    final double planeOffset = normal.dot(point);
	    CollisionFunction function = new CollisionFunction(normal, planeOffset);
	    SmallErrorFinder collisionFinder = new SmallErrorFinder(10, 100, 0.0001);
	    Optional<InputToErrorStateMap<CollisionState>> possibleCollision = collisionFinder.findSmallErrorState(function, new Interval(time, upperTimeLimit));
	    if (possibleCollision.isEmpty()) {
		return Optional.empty();
	    }

	    InputToErrorStateMap<CollisionState> collision = possibleCollision.get();
	    final double timeOfImpact = collision.getInput();
	    final CollisionState collisionState = collision.getErrorState();

	    final Vector2D tangent = normal.rotate90Degrees(Vector2D.RotationDirection.Y_TO_X);
	    final double pointAlongTangent = tangent.dot(point);
	    final double circleAlongTangent = pointMass.getPos().dot(tangent);
	    final double distAlongTangent = Math.abs(circleAlongTangent - pointAlongTangent);
	    final double tolerence = 0.1;
	    if (distAlongTangent <= tolerence) {
		return Optional.of(createCollision(normal, collisionState.separation, timeOfImpact));
	    }
	    time -= timeOfImpact;
	}
	return Optional.empty();
    }

    private class CollisionFunction implements ContinousErrorStateFunction<CollisionState>
    {
	private Vector2D normal;
	private double planeOffset;

	private CollisionFunction(final Vector2D normal, final double planeOffset) {
	    this.normal = normal;
	    this.planeOffset = planeOffset;
	}

	@Override public CollisionState getErrorStateOf(final double time) {
	    final Vector2D initPos = pointMass.getPos();
	    final Vector2D vel = pointMass.getVel();
	    final Vector2D finalPos = initPos.add(vel.multiply(time));
	    final double finalNormalPos = finalPos.dot(normal);
	    final double separation = (finalNormalPos - radius) - planeOffset;
	    return new CollisionState(finalPos, separation);
	}
    }

    private class CollisionState implements ErrorState
    {
	private Vector2D circlePos;
	private double separation;

	private CollisionState(final Vector2D circlePos, final double separation) {
	    this.circlePos = circlePos;
	    this.separation = separation;
	}

	@Override public double getError() {
	    return separation;
	}
    }

    private UpcomingCollision createCollision(final Vector2D normal, final double separation, final double timeOfImpact) {
	final double bounceCoeff = 0;
	final double frictionCoeff = 0.1;
	return new UpcomingCollision(pointMass, normal.multiply(-radius), normal, separation, bounceCoeff, frictionCoeff, timeOfImpact);
    }
}
