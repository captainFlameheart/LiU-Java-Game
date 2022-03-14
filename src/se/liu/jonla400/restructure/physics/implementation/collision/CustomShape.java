package se.liu.jonla400.restructure.physics.implementation.collision;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class CustomShape implements Iterable<LineSegment>
{
    private LineSegment[] lineSegments;

    // TEMPORARY
    public CustomShape(final LineSegment... lineSegments) {
	this.lineSegments = lineSegments.clone();
    }

    public static CustomShape createFromDefinition(final CustomShapeDefinition definition) {
	final Collection<LineSegment> lineSegments = new ArrayList<>();
	for (LineSegmentDefinition lineSegmentDefinition : definition) {
	    lineSegments.add(LineSegment.createFromDefinition(lineSegmentDefinition));
	}
	return new CustomShape(lineSegments.toArray(new LineSegment[0]));
    }

    @NotNull @Override public Iterator<LineSegment> iterator() {
	return Arrays.stream(lineSegments).iterator();
    }
}
