package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.main.RectangularRegion;
import se.liu.jonla400.restructure.math.Interval;
import se.liu.jonla400.restructure.math.Vector2D;

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
    private List<LineSegmentType> types;

    private Vector2D centerOfMass;

    private RectangularRegion camera;

    private LevelBlueprint(final List<Vector2D> vertices, final List<LineSegmentType> types, final Vector2D centerOfMass,
                          final RectangularRegion camera)
    {
        this.vertices = vertices;
        this.types = types;
        this.centerOfMass = centerOfMass;
        this.camera = camera;
    }

    public static LevelBlueprint createEmpty() {
        final List<Vector2D> vertices = new ArrayList<>();
        final List<LineSegmentType> types = new ArrayList<>();
        final Vector2D centerOfMass = Vector2D.createZero();
        final RectangularRegion camera = RectangularRegion.createFromIntervals(new Interval(-10, 10), new Interval(-10, 10));
        return new LevelBlueprint(vertices, types, centerOfMass, camera);
    }

    public Set<Vector2D> getAllVertices() {
        return new HashSet<>(vertices);
    }

    public Vector2D getVertex(int index) {
        return vertices.get(index);
    }

    public void addVertex(final Vector2D vertex) {
        vertices.add(vertex);
        if (!hasIncompleteLineSegment()) {
            types.add(LineSegmentType.DEFAULT);
        }
    }

    public void removeVertex() {
        vertices.remove(vertices.size() - 1);
        if (hasIncompleteLineSegment()) {
            types.remove(types.size() - 1);
        }
    }

    public void addLineSegment(final IndexedLineSegment lineSegment) {
        final int lineSegmentIndex = lineSegment.getIndex();
        final int startVertexIndex = 2 * lineSegmentIndex;
        vertices.addAll(startVertexIndex, Arrays.asList(lineSegment.getStart(), lineSegment.getEnd()));
        types.add(lineSegmentIndex, lineSegment.getType());
    }

    public void removeLineSegment(final int lineSegmentIndex) {
        final int startVertexIndex = 2 * lineSegmentIndex;
        vertices.subList(startVertexIndex, startVertexIndex + 2).clear();
        types.remove(lineSegmentIndex);
    }

    public LineSegmentType getType(final int lineSegmentIndex) {
        return types.get(lineSegmentIndex);
    }

    public void setType(final int lineSegmentIndex, final LineSegmentType type) {
        types.set(lineSegmentIndex, type);
    }

    public Vector2D getCenterOfMass() {
        return centerOfMass;
    }

    public void setCenterOfMass(final Vector2D centerOfMass) {
        this.centerOfMass.set(centerOfMass);
    }

    public RectangularRegion getCamera() {
        return camera;
    }

    public void setCamera(final RectangularRegion camera) {
        this.camera = camera;
    }

    public boolean hasIncompleteLineSegment() {
        return getIncompleteLineSegmentStart().isPresent();
    }

    public Optional<Vector2D> getIncompleteLineSegmentStart() {
        final int vertexCount = vertices.size();
        if (vertexCount % 2 == 0) {
            return Optional.empty();
        }
        return Optional.of(vertices.get(vertexCount - 1));
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
                final LineSegmentType type = types.get(index);
                return IndexedLineSegment.create(index, start, end, type);
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
                neighbours.add(end);
            } else if (end.equals(vertex)) {
                neighbours.add(start);
            }
        }
        return neighbours;
    }

    public Optional<IndexedLineSegment> getClosestLineSegmentTo(final Vector2D point) {
        // LOOK FOR CODE REDUNDANCY!

        IndexedLineSegment closestLineSegment = null;
        double minDist = Double.POSITIVE_INFINITY;

        final Iterator<IndexedLineSegment> lineSegmentIterator = getLineSegmentIterator();
        while (lineSegmentIterator.hasNext()) {
            final IndexedLineSegment lineSegment = lineSegmentIterator.next();
            final Vector2D closestPoint = lineSegment.getClosestPointTo(point);
            final double dist = closestPoint.subtract(point).getMagnitudeSquared();
            if (dist < minDist) {
                closestLineSegment = lineSegment;
                minDist = dist;
            }
        }
        return Optional.ofNullable(closestLineSegment);
    }
}
