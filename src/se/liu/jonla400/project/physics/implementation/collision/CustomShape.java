package se.liu.jonla400.project.physics.implementation.collision;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;

public class CustomShape implements Iterable<LineSegment>
{
    private LineSegment[] lineSegments;

    public CustomShape(final LineSegment... lineSegments) {
	this.lineSegments = lineSegments.clone();
    }

    @NotNull @Override public Iterator<LineSegment> iterator() {
	return Arrays.stream(lineSegments).iterator();
    }
}
