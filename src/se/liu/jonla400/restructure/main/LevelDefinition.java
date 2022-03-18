package se.liu.jonla400.restructure.main;

import se.liu.jonla400.restructure.main.levelcreation.IndexedLineSegment;
import se.liu.jonla400.restructure.main.levelcreation.LevelBlueprint;
import se.liu.jonla400.restructure.main.levelcreation.LineSegmentType;
import se.liu.jonla400.restructure.math.Vector2D;
import se.liu.jonla400.restructure.physics.implementation.collision.CustomShape;
import se.liu.jonla400.restructure.physics.implementation.collision.LineSegment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class LevelDefinition
{
    private CustomShape<LineSegmentType> shape;
    private Vector2D centerOfMass;
    private RectangularRegion camera;

    public LevelDefinition(final CustomShape<LineSegmentType> shape, final Vector2D centerOfMass, final RectangularRegion camera)
    {
	this.shape = shape;
	this.centerOfMass = centerOfMass;
	this.camera = camera;
    }

    public static LevelDefinition createFromBlueprint(final LevelBlueprint blueprint) {
	final Collection<LineSegment<LineSegmentType>> collidableLineSegments = new ArrayList<>();

	final Iterator<IndexedLineSegment> lineSegIterator = blueprint.getLineSegmentIterator();
	while (lineSegIterator.hasNext()) {
	    collidableLineSegments.add(lineSegIterator.next().convertToCollidableLineSegment());
	}

	final CustomShape<LineSegmentType> collidableShape = CustomShape.copyFrom(collidableLineSegments);

	return new LevelDefinition(collidableShape, blueprint.getCenterOfMass(), blueprint.getCamera());
    }

    public Vector2D getCenterOfMass() {
	return centerOfMass;
    }

    public CustomShape<LineSegmentType> getShape() {
	return shape;
    }

    public RectangularRegion getCamera() {
	return camera;
    }
}
