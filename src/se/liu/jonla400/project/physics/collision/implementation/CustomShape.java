package se.liu.jonla400.project.physics.collision.implementation;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Represents a set of {@link LineSegment}s. Each line segment is associated with custom
 * user data.
 *
 * @param <T> The type of user data associated with each line segment
 */
public class CustomShape<T> implements Iterable<LineSegment<T>>
{
    private Collection<LineSegment<T>> lineSegments;

    private CustomShape(final Collection<LineSegment<T>> lineSegments) {
	this.lineSegments = lineSegments;
    }

    /**
     * Creates a CustomShape with the given line segments. No reference is kept to the input
     * collection.
     *
     * @param lineSegments The line segments
     * @param <T> The type of user data associated with each line segment
     * @return The created CustomShape
     */
    public static <T> CustomShape<T> copyFrom(final Collection<LineSegment<T>> lineSegments) {
	return new CustomShape<>(new ArrayList<>(lineSegments));
    }

    /**
     * @return An iterator over each line segment
     */
    @NotNull @Override public Iterator<LineSegment<T>> iterator() {
	return lineSegments.iterator();
    }
}
