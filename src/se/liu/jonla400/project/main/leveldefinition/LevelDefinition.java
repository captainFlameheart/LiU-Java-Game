package se.liu.jonla400.project.main.leveldefinition;

import se.liu.jonla400.project.math.RectangularRegion;
import se.liu.jonla400.project.main.levelcreation.IndexedLineSegment;
import se.liu.jonla400.project.main.levelcreation.LevelBlueprint;
import se.liu.jonla400.project.math.Interval;
import se.liu.jonla400.project.math.Vector2D;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class LevelDefinition
{
    private CustomShapeDefinition shape;
    private Vector2D centerOfMass;
    private Vector2D ballPos;
    private double ballRadius;
    private RectangularRegion camera;

    private LevelDefinition() {
	// Used by gson
	shape = null;
	centerOfMass = null;
	ballPos = null;
	ballRadius = 0;
	camera = null;
    }

    private LevelDefinition(final CustomShapeDefinition shape, final Vector2D centerOfMass, final Vector2D ballPos,
			    final double ballRadius, final RectangularRegion camera)
    {
	this.shape = shape;
	this.centerOfMass = centerOfMass;
	this.ballPos = ballPos;
	this.ballRadius = ballRadius;
	this.camera = camera;
    }

    public static LevelDefinition createEmpty() {
	final CustomShapeDefinition levelShape = CustomShapeDefinition.createEmpty();
	final Vector2D levelCenterOfMass = Vector2D.createZero();
	final Vector2D ballPos = Vector2D.createZero();
	final double ballRadius = 0.5;
	final RectangularRegion camera = RectangularRegion.createFromCoordinateRanges(
		new Interval(-10, 10),
		new Interval(-10, 10)
	);
	return new LevelDefinition(levelShape, levelCenterOfMass, ballPos, ballRadius, camera);
    }

    public static LevelDefinition createFromBlueprint(final LevelBlueprint blueprint) {
	final Collection<LineSegmentDefinition> segments = new ArrayList<>();
	final Iterator<IndexedLineSegment> indexedSegmentIterator = blueprint.getLineSegmentIterator();
	while (indexedSegmentIterator.hasNext()) {
	    segments.add(indexedSegmentIterator.next().removeIndex());
	}
	final CustomShapeDefinition shape = CustomShapeDefinition.copyFrom(segments);

	final Vector2D centerOfMass = blueprint.getCenterOfMass();
	final Vector2D ballPos = blueprint.getBallPos();
	final double ballRadius = blueprint.getBallRadius();
	final RectangularRegion camera = blueprint.getCamera();
	return new LevelDefinition(shape, centerOfMass, ballPos, ballRadius, camera);
    }

    public CustomShapeDefinition getShape() {
	return shape;
    }

    public Vector2D getCenterOfMass() {
	return centerOfMass.copy();
    }

    public Vector2D getBallPos() {
	return ballPos.copy();
    }

    public double getBallRadius() {
	return ballRadius;
    }

    public RectangularRegion getCamera() {
	return camera.copy();
    }
}
