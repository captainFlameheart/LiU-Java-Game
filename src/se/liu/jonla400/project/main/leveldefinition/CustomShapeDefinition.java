package se.liu.jonla400.project.main.leveldefinition;

import se.liu.jonla400.project.physics.collision.implementation.CustomShape;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Collectors;

public class CustomShapeDefinition implements Iterable<LineSegmentDefinition>
{
    private Collection<LineSegmentDefinition> lineSegmentDefinitions;

    private CustomShapeDefinition() {
        // Used by gson
        lineSegmentDefinitions = null;
    }

    private CustomShapeDefinition(final Collection<LineSegmentDefinition> lineSegmentDefinitions) {
        this.lineSegmentDefinitions = lineSegmentDefinitions;
    }

    public static CustomShapeDefinition createEmpty() {
        return new CustomShapeDefinition(Collections.emptyList());
    }

    public static CustomShapeDefinition copyFrom(final Collection<LineSegmentDefinition> lineSegmentDefinitions) {
        return new CustomShapeDefinition(new ArrayList<>(lineSegmentDefinitions));
    }

    @NotNull @Override public Iterator<LineSegmentDefinition> iterator() {
        return lineSegmentDefinitions.iterator();
    }

    public CustomShape<LineSegmentType> convertToCollidableShape() {
        return CustomShape.copyFrom(
                lineSegmentDefinitions.stream().map(LineSegmentDefinition::convertToCollidableSegment)
                        .collect(Collectors.toList()));
    }
}
