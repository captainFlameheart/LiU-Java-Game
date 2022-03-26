package se.liu.jonla400.project.main.levelcreation;

import se.liu.jonla400.project.main.leveldefinition.LineSegmentType;
import se.liu.jonla400.project.main.leveldefinition.LevelDefinition;
import se.liu.jonla400.project.math.RectangularRegion;
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

/**
 * Represents a level under construction by a {@link LevelCreator}. Vertices and line segments
 * are in this representation stored in a sequential order. Unlike a {@link LevelDefinition},
 * this representation can contain an incomplete line segment that only contains a start but no end.
 */
public class LevelBlueprint
{
    private final static int VERTICES_PER_SEGMENT = 2;

    private List<Vector2D> vertices;
    private List<LineSegmentType> lineSegmentTypes;
    private Vector2D centerOfMass;
    private Vector2D ballPos;
    private double ballRadius;
    private RectangularRegion camera;

    private LevelBlueprint(final List<Vector2D> vertices, final List<LineSegmentType> lineSegmentTypes, final Vector2D centerOfMass,
                          final Vector2D ballPos, final double ballRadius, final RectangularRegion camera)
    {
        this.vertices = vertices;
        this.lineSegmentTypes = lineSegmentTypes;
        this.centerOfMass = centerOfMass;
        this.ballPos = ballPos;
        this.ballRadius = ballRadius;
        this.camera = camera;
    }

    /**
     * Creates a LevelBlueprint from the given {@link LevelDefinition}
     *
     * @param definition The definition to convert to a LevelBlueprint
     * @return The created LevelBlueprint
     */
    public static LevelBlueprint createFromDefinition(final LevelDefinition definition) {
        final List<Vector2D> vertices = new ArrayList<>();
        final List<LineSegmentType> lineSegmentTypes = new ArrayList<>();
        for (LineSegmentDefinition segment : definition.getShape()) {
            vertices.add(segment.getStart());
            vertices.add(segment.getEnd());
            lineSegmentTypes.add(segment.getType());
        }

        final Vector2D centerOfMass = definition.getCenterOfMass();
        final Vector2D ballPos = definition.getBallPos();
        final double ballRadius = definition.getBallRadius();
        final RectangularRegion camera = definition.getCamera();
        return new LevelBlueprint(vertices, lineSegmentTypes, centerOfMass, ballPos, ballRadius, camera);
    }

    /**
     * @return A read-only set of all the vertices
     */
    public Set<Vector2D> getAllVertices() {
        final Set<Vector2D> uniqueVertices = new HashSet<>();
        for (Vector2D vertex : vertices) {
            uniqueVertices.add(vertex.copy());
        }
        return uniqueVertices;
    }

    /**
     * @param vertex The vertex to add. No reference to the vector is kept.
     */
    public void addVertex(final Vector2D vertex) {
        vertices.add(vertex.copy());
        if (!hasIncompleteLineSegment()) {
            lineSegmentTypes.add(LineSegmentType.DEFAULT);
        }
    }

    /**
     * Removes the last added vertex. If no vertex exists throws an {@link IndexOutOfBoundsException}
     */
    public void removeVertex() {
        vertices.remove(vertices.size() - 1);
        if (hasIncompleteLineSegment()) {
            lineSegmentTypes.remove(lineSegmentTypes.size() - 1);
        }
    }

    /**
     * @param vertex The vertex to move (equivalently the position of the vertex)
     * @param newPos The new position of the vertex
     */
    public void moveVertex(final Vector2D vertex, final Vector2D newPos) {
        for (Vector2D v : vertices) {
            if (v.equals(vertex)) {
                v.set(newPos);
            }
        }
    }

    /**
     * Adds a line segment at its index
     *
     * @param lineSegment The line segment (including its index) to add
     */
    public void addLineSegment(final IndexedLineSegment segment) {
        final int segmentIndex = segment.getIndex();
        final int startVertexIndex = VERTICES_PER_SEGMENT * segmentIndex;
        vertices.addAll(startVertexIndex, Arrays.asList(segment.getStart(), segment.getEnd()));
        lineSegmentTypes.add(segmentIndex, segment.getType());
    }

    /**
     * Removes the line segment at the given index
     *
     * @param lineSegmentIndex The index of the line segment to remove
     */
    public void removeLineSegment(final int segmentIndex) {
        final int startVertexIndex = VERTICES_PER_SEGMENT * segmentIndex;
        vertices.subList(startVertexIndex, startVertexIndex + VERTICES_PER_SEGMENT).clear();
        lineSegmentTypes.remove(segmentIndex);
    }

    /**
     * Gets the type of the line segment at the given index
     *
     * @param lineSegmentIndex The index of the line segment to get the type of
     */
    public LineSegmentType getLineSegmentType(final int segmentIndex) {
        return lineSegmentTypes.get(segmentIndex);
    }

    /**
     * Sets the type of the line segment at the given index
     *
     * @param lineSegmentIndex The index of the line segment to set the type of
     * @param type The type to set
     */
    public void setLineSegmentType(final int segmentIndex, final LineSegmentType type) {
        lineSegmentTypes.set(segmentIndex, type);
    }

    /**
     * @return A read-only view of the level's center of mass
     */
    public Vector2D getCenterOfMass() {
        return centerOfMass.copy();
    }

    /**
     * Sets the level's center of mass. No reference to the input vector is kept.
     *
     * @param centerOfMass The new position of the level's center of mass
     */
    public void setCenterOfMass(final Vector2D centerOfMass) {
        this.centerOfMass.set(centerOfMass);
    }

    /**
     * @return A read-only view of the level's camera
     */
    public RectangularRegion getCamera() {
        return camera.copy();
    }

    /**
     * Sets the level camera. No reference to the input camera is kept.
     *
     * @param camera The new camera
     */
    public void setCamera(final RectangularRegion camera) {
        this.camera = camera.copy();
    }

    /**
     * @return A read-only view of the ball position
     */
    public Vector2D getBallPos() {
        return ballPos.copy();
    }

    /**
     * @return The ball's radius
     */
    public double getBallRadius() {
        return ballRadius;
    }

    /**
     * @return Whether there exists a line segment with a start but without an end
     */
    public boolean hasIncompleteLineSegment() {
        return getIncompleteLineSegmentStart().isPresent();
    }

    /**
     * @return The start of the incomplete line segment, if it exists
     */
    public Optional<Vector2D> getIncompleteLineSegmentStart() {
        final int vertexCount = vertices.size();
        if (vertexCount % VERTICES_PER_SEGMENT == 0) {
            return Optional.empty();
        }
        return Optional.of(vertices.get(vertexCount - 1).copy());
    }

    /**
     * @return An iterator over the full line segments, including their indicies
     */
    public Iterator<IndexedLineSegment> getLineSegmentIterator() {
        return new Iterator<>()
        {
            private int nextVertexIndex = 0;

            @Override public boolean hasNext() {
                // We are picking VERTICES_PER_SEGMENT vertices at a time
                return nextVertexIndex <= vertices.size() - VERTICES_PER_SEGMENT;
            }

            @Override public IndexedLineSegment next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No more line segments");
                }
                final int index = nextVertexIndex / VERTICES_PER_SEGMENT;
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

    /**
     * Returns the read-only neighbours to the given vertex
     *
     * @param vertex The vertex to get the neighbours to
     * @return The neighbours to the vertex
     */
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

    /**
     * Returns the line segment that contains the closest point to the given point.
     * If no line segments exist, returns Optional.empty()
     *
     * @param point The reference point
     * @return The closest line segment, if any exists
     */
    public Optional<IndexedLineSegment> getClosestLineSegmentTo(final Vector2D point) {
        return ClosestPointFinder.findClosestObject(getLineSegmentIterator(), point);
    }
}
