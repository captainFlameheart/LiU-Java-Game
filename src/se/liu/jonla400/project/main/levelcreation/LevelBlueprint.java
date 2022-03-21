package se.liu.jonla400.project.main.levelcreation;

import se.liu.jonla400.project.main.LineSegmentType;
import se.liu.jonla400.project.main.leveldefinition.LevelDefinition;
import se.liu.jonla400.project.main.RectangularRegion;
import se.liu.jonla400.project.main.leveldefinition.LineSegmentDefinition;
import se.liu.jonla400.project.math.ClosestPointFinder;
import se.liu.jonla400.project.math.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

public class LevelBlueprint
{
    private List<Vector2D> vertices;
    private List<LineSegmentType> lineSegmentTypes;
    private Vector2D centerOfMass;
    private RectangularRegion camera;

    private LevelBlueprint(final List<Vector2D> vertices, final List<LineSegmentType> lineSegmentTypes, final Vector2D centerOfMass,
                           final RectangularRegion camera)
    {
        this.vertices = vertices;
        this.lineSegmentTypes = lineSegmentTypes;
        this.centerOfMass = centerOfMass;
        this.camera = camera;
    }

    public static LevelBlueprint createFromDefinition(final LevelDefinition def) {
        final List<Vector2D> vertices = new ArrayList<>();
        final List<LineSegmentType> types = new ArrayList<>();
        for (LineSegmentDefinition segment : def.getShape()) {
            vertices.add(segment.getStart());
            vertices.add(segment.getEnd());
            types.add(segment.getType());
        }

        final Vector2D centerOfMass = def.getCenterOfMass();
        final RectangularRegion camera = def.getCamera();
        return new LevelBlueprint(vertices, types, centerOfMass, camera);
    }

    public Set<Vector2D> getAllVertices() {
        final Set<Vector2D> uniqueVertices = new HashSet<>();
        for (Vector2D vertex : vertices) {
            uniqueVertices.add(vertex.copy());
        }
        return uniqueVertices;
    }

    public void addVertex(final Vector2D vertex) {
        vertices.add(vertex.copy());
        if (!hasIncompleteLineSegment()) {
            lineSegmentTypes.add(LineSegmentType.DEFAULT);
        }
    }

    public void removeVertex() {
        vertices.remove(vertices.size() - 1);
        if (hasIncompleteLineSegment()) {
            lineSegmentTypes.remove(lineSegmentTypes.size() - 1);
        }
    }

    public void moveVertex(final Vector2D vertex, final Vector2D newPos) {
        for (Vector2D v : vertices) {
            if (v.equals(vertex)) {
                v.set(newPos);
            }
        }
    }

    public void addLineSegment(final IndexedLineSegment segment) {
        final int segmentIndex = segment.getIndex();
        final int startVertexIndex = 2 * segmentIndex;
        vertices.addAll(startVertexIndex, Arrays.asList(segment.getStart(), segment.getEnd()));
        lineSegmentTypes.add(segmentIndex, segment.getType());
    }

    public void removeLineSegment(final int segmentIndex) {
        final int startVertexIndex = 2 * segmentIndex;
        vertices.subList(startVertexIndex, startVertexIndex + 2).clear();
        lineSegmentTypes.remove(segmentIndex);
    }

    public LineSegmentType getLineSegmentType(final int segmentIndex) {
        return lineSegmentTypes.get(segmentIndex);
    }

    public void setLineSegmentType(final int segmentIndex, final LineSegmentType type) {
        lineSegmentTypes.set(segmentIndex, type);
    }

    public Vector2D getCenterOfMass() {
        return centerOfMass.copy();
    }

    public void setCenterOfMass(final Vector2D centerOfMass) {
        this.centerOfMass.set(centerOfMass);
    }

    public RectangularRegion getCamera() {
        return camera.copy();
    }

    public void setCamera(final RectangularRegion camera) {
        this.camera = camera.copy();
    }

    public boolean hasIncompleteLineSegment() {
        return getIncompleteLineSegmentStart().isPresent();
    }

    public Optional<Vector2D> getIncompleteLineSegmentStart() {
        final int vertexCount = vertices.size();
        if (vertexCount % 2 == 0) {
            return Optional.empty();
        }
        return Optional.of(vertices.get(vertexCount - 1).copy());
    }

    public Iterator<IndexedLineSegment> getLineSegmentIterator() {
        return new Iterator<>()
        {
            private int nextVertexIndex = 0;

            @Override public boolean hasNext() {
                return nextVertexIndex < vertices.size() - 1;
            }

            @Override public IndexedLineSegment next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more line segments");
                }
                final int index = nextVertexIndex / 2;
                final Vector2D start = getNextVertex();
                final Vector2D end = getNextVertex();
                final LineSegmentType type = lineSegmentTypes.get(index);
                return IndexedLineSegment.copyEndPoints(index, start, end, type);
            }

            private Vector2D getNextVertex() {
                final Vector2D vertex = vertices.get(nextVertexIndex);
                nextVertexIndex++;
                return vertex;
            }
        };
    }

    public Set<Vector2D> getNeighboursTo(final Vector2D vertex) {
        final Set<Vector2D> neighbours = new HashSet<>();

        final Iterator<IndexedLineSegment> lineSegmentIterator = getLineSegmentIterator();
        while (lineSegmentIterator.hasNext()) {
            final IndexedLineSegment lineSegment = lineSegmentIterator.next();
            final Vector2D start = lineSegment.getStart();
            final Vector2D end = lineSegment.getEnd();

            if (start.equals(vertex)) {
                neighbours.add(end.copy());
            } else if (end.equals(vertex)) {
                neighbours.add(start.copy());
            }
        }
        return neighbours;
    }

    public Optional<IndexedLineSegment> getClosestLineSegmentTo(final Vector2D point) {
        return ClosestPointFinder.findClosestObject(getLineSegmentIterator(), point);
    }
}
