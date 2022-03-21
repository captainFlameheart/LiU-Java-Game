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
    private RectangularRegion camera;

    private LevelDefinition() {
	// Used by gson
	shape = null;
	centerOfMass = null;
	camera = null;
    }

    private LevelDefinition(final CustomShapeDefinition shape, final Vector2D centerOfMass, final RectangularRegion camera) {
	this.shape = shape;
	this.centerOfMass = centerOfMass;
	this.camera = camera;
    }

    public static LevelDefinition createEmpty() {
	final CustomShapeDefinition shape = CustomShapeDefinition.createEmpty();
	final Vector2D centerOfMass = Vector2D.createZero();
	final RectangularRegion camera = RectangularRegion.createFromCoordinateRanges(
		new Interval(-10, 10),
		new Interval(-10, 10)
	);
	return new LevelDefinition(shape, centerOfMass, camera);
    }

    public static LevelDefinition createFromBlueprint(final LevelBlueprint blueprint) {
	final Collection<LineSegmentDefinition> segments = new ArrayList<>();
	final Iterator<IndexedLineSegment> indexedSegmentIterator = blueprint.getLineSegmentIterator();
	while (indexedSegmentIterator.hasNext()) {
	    segments.add(indexedSegmentIterator.next().removeIndex());
	}
	final CustomShapeDefinition shape = CustomShapeDefinition.copyFrom(segments);

	final Vector2D centerOfMass = blueprint.getCenterOfMass();
	final RectangularRegion camera = blueprint.getCamera();
	return new LevelDefinition(shape, centerOfMass, camera);
    }

    public CustomShapeDefinition getShape() {
	return shape;
    }

    public Vector2D getCenterOfMass() {
	return centerOfMass.copy();
    }

    public RectangularRegion getCamera() {
	return camera.copy();
    }
}
