package se.liu.jonla400.project.main.leveldefinition;

import se.liu.jonla400.project.physics.collision.implementation.CustomShape;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * Defines how a level shape by specifying each line segment and their types
 * using {@link LineSegmentDefinition}
 */
public class LevelShapeDefinition implements Iterable<LineSegmentDefinition>
{
    private Collection<LineSegmentDefinition> lineSegmentDefinitions;

    private LevelShapeDefinition() {
        // Used by gson
        lineSegmentDefinitions = null;
    }

    private LevelShapeDefinition(final Collection<LineSegmentDefinition> lineSegmentDefinitions) {
        this.lineSegmentDefinitions = lineSegmentDefinitions;
    }

    /**
     * Creates a LevelShapeDefinition with no line segments
     *
     * @return The created LevelShapeDefinition
     */
    public static LevelShapeDefinition createEmpty() {
        return new LevelShapeDefinition(Collections.emptyList());
    }

    /**
     * Creates a LevelShapeDefinition of the given {@link LineSegmentDefinition}s. No reference to the input
     * collection is kept.
     *
     * @param lineSegmentDefinitions The line segment definitions of this shape
     * @return The created LevelShapeDefinition
     */
    public static LevelShapeDefinition copyFrom(final Collection<LineSegmentDefinition> lineSegmentDefinitions) {
        return new LevelShapeDefinition(new ArrayList<>(lineSegmentDefinitions));
    }

    /**
     * @return Whether this definition is considered invalid
     */
    public boolean isInvalid() {
        return lineSegmentDefinitions == null || lineSegmentDefinitions.stream().anyMatch(this::isLineSegmentDefinitionInvalid);
    }

    private boolean isLineSegmentDefinitionInvalid(final LineSegmentDefinition segmentDefinition) {
        return segmentDefinition == null || segmentDefinition.isInvalid();
    }

    /**
     * @return An iterator of the line segments of this shape
     */
    @NotNull @Override public Iterator<LineSegmentDefinition> iterator() {
        return lineSegmentDefinitions.iterator();
    }

    /**
     * @return The corresponding shape used by the {@link se.liu.jonla400.project.physics} package
     */
    public CustomShape<LineSegmentType> convertToCollidableShape() {
        return CustomShape.copyFrom(
                lineSegmentDefinitions.stream().map(LineSegmentDefinition::convertToCollidableSegment)
                        .collect(Collectors.toList()));
    }
}
