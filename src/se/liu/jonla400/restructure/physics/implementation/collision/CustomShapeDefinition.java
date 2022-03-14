package se.liu.jonla400.restructure.physics.implementation.collision;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class CustomShapeDefinition implements Iterable<LineSegmentDefinition>
{
    private Collection<LineSegmentDefinition> lineSegmentDefinitions;

    private CustomShapeDefinition(final Collection<LineSegmentDefinition> lineSegmentDefinitions) {
	this.lineSegmentDefinitions = lineSegmentDefinitions;
    }

    public static CustomShapeDefinition create(LineSegmentDefinition... lineSegmentDefinitions) {
	return new CustomShapeDefinition(new ArrayList<>(Arrays.asList(lineSegmentDefinitions)));
    }

    public void add(final LineSegmentDefinition lineSegmentDefinition) {
	lineSegmentDefinitions.add(lineSegmentDefinition);
    }

    public void remove(final LineSegmentDefinition lineSegmentDefinition) {
	lineSegmentDefinitions.remove(lineSegmentDefinition);
    }

    @NotNull @Override public Iterator<LineSegmentDefinition> iterator() {
	return lineSegmentDefinitions.iterator();
    }
}
