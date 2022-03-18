package se.liu.jonla400.restructure.main.leveldefinition;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import se.liu.jonla400.restructure.main.levelcreation.LineSegmentType;
import se.liu.jonla400.restructure.math.Vector2D;
import se.liu.jonla400.restructure.physics.implementation.collision.CustomShape;
import se.liu.jonla400.restructure.physics.implementation.collision.LineSegment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class CustomShapeDefinition implements Iterable<LineSegmentDefinition>
{
    private Collection<LineSegmentDefinition> lineSegmentDefinitions;

    private CustomShapeDefinition() {
        // Used by gson
    }

    private CustomShapeDefinition(final Collection<LineSegmentDefinition> lineSegmentDefinitions) {
        this.lineSegmentDefinitions = lineSegmentDefinitions;
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
