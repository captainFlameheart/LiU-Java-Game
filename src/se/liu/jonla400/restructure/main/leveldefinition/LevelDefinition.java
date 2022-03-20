package se.liu.jonla400.restructure.main.leveldefinition;

import se.liu.jonla400.restructure.constants.CameraConstants;
import se.liu.jonla400.restructure.main.RectangularRegion;
import se.liu.jonla400.restructure.main.levelcreation.IndexedLineSegment;
import se.liu.jonla400.restructure.main.levelcreation.LevelBlueprint;
import se.liu.jonla400.restructure.math.Vector2D;

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
    }

    private LevelDefinition(final CustomShapeDefinition shape, final Vector2D centerOfMass, final RectangularRegion camera) {
	this.shape = shape;
	this.centerOfMass = centerOfMass;
	this.camera = camera;
    }

    public static LevelDefinition createFromBlueprint(final LevelBlueprint blueprint) {
	final Collection<LineSegmentDefinition> lineSegments = new ArrayList<>();

	final Iterator<IndexedLineSegment> indexedSegmentIterator = blueprint.getLineSegmentIterator();
	while (indexedSegmentIterator.hasNext()) {
	    lineSegments.add(indexedSegmentIterator.next().getWithoutIndex());
	}

	final CustomShapeDefinition shape = CustomShapeDefinition.copyFrom(lineSegments);
	final Vector2D centerOfMass = blueprint.getCenterOfMass();
	final RectangularRegion camera = blueprint.getCamera();
	return new LevelDefinition(shape, centerOfMass, camera);
    }

    public static LevelDefinition createEmpty() {
	return new LevelDefinition(CustomShapeDefinition.createEmpty(), Vector2D.createZero(), CameraConstants.getDefaultCamera());
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
