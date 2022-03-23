package se.liu.jonla400.project.physics.collision.implementation;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class CustomShape<T> implements Iterable<LineSegment<T>>
{
    private Collection<LineSegment<T>> lineSegments;

    private CustomShape(final Collection<LineSegment<T>> lineSegments) {
	this.lineSegments = lineSegments;
    }

    public static <T> CustomShape<T> copyFrom(final Collection<LineSegment<T>> lineSegments) {
	return new CustomShape<>(new ArrayList<>(lineSegments));
    }

    @NotNull @Override public Iterator<LineSegment<T>> iterator() {
	return lineSegments.iterator();
    }
}
