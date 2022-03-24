package se.liu.jonla400.project.main.levelcreation;

import se.liu.jonla400.project.main.drawing.DrawConfiguration;
import se.liu.jonla400.project.main.drawing.Transform;
import se.liu.jonla400.project.main.drawing.TransformedDrawer;
import se.liu.jonla400.project.main.leveldefinition.LineSegmentDefinition;
import se.liu.jonla400.project.main.leveldefinition.LineSegmentType;
import se.liu.jonla400.project.math.RectangularRegion;
import se.liu.jonla400.project.main.drawing.CameraDrawer;
import se.liu.jonla400.project.math.Vector2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

/**
 * Wraps a {@link LevelBlueprint} and can draw the blueprint according to a {@link DrawConfiguration}
 */
public class DrawableLevelBlueprint
{
    private LevelBlueprint blueprint;
    private DrawConfiguration drawConfig;

    /**
     * Creates a DrawableLevelBlueprint that wraps the given {@link LevelBlueprint} and draws it
     * according to the given {@link DrawConfiguration}
     *
     * @param blueprint The blueprint to append graphics capabilies to
     * @param drawConfig How the blueprint is to be drawn
     */
    public DrawableLevelBlueprint(final LevelBlueprint blueprint, final DrawConfiguration drawConfig) {
        this.blueprint = blueprint;
        this.drawConfig = drawConfig;
    }

    /**
     * Draws the wrapped blueprint onto the {@link Graphics2D} object at the given region.
     * This method might draw outside the region too.
     *
     * @param g The graphics object to draw to
     * @param region The region required to draw to, but not limited by
     */
    public void draw(final Graphics2D g, final RectangularRegion region) {
        drawBackground(g, region);
        drawFullLineSegments(g);
        drawBall(g);
        drawCenterOfMass(g);
        drawCamera(g);
    }

    private void drawBackground(final Graphics2D g, final RectangularRegion region) {
        g.setColor(Color.WHITE);
        g.fill(new Rectangle2D.Double(region.getLeftX(), region.getBottomY(), region.getWidth(), region.getHeight()));
    }

    private void drawFullLineSegments(final Graphics2D g) {
        final Iterator<IndexedLineSegment> lineSegmentIterator = getLineSegmentIterator();
        while (lineSegmentIterator.hasNext()) {
            final LineSegmentDefinition lineSegment = lineSegmentIterator.next().removeIndex();
            drawConfig.getLineSegmentDrawer().draw(g, lineSegment);
        }
    }

    private void drawBall(final Graphics2D g) {
        // Note: The ball position and radius are constant, but level creator
        // modes in the future might be able to change them
        TransformedDrawer.draw(
                g, Transform.createWithTranslation(blueprint.getBallPos()),
                drawConfig.getBallDrawer(blueprint.getBallRadius())
        );
    }

    private void drawCenterOfMass(final Graphics2D g) {
        TransformedDrawer.draw(
                g, Transform.createWithTranslation(blueprint.getCenterOfMass()),
                drawConfig.getCenterOfMassDrawer()
        );
    }

    private void drawCamera(final Graphics2D g) {
        final float strokeWidth = 0.1f;
        final CameraDrawer cameraDrawer = CameraDrawer.createDashed(blueprint.getCamera(), Color.BLACK, strokeWidth);
        cameraDrawer.draw(g);
    }

    /**
     * @return The level blueprint
     */
    public LevelBlueprint getBlueprint() {
        return blueprint;
    }

    /**
     * @return How the level blueprint is drawn
     */
    public DrawConfiguration getDrawConfig() {
        return drawConfig;
    }

    /**
     * @return A read-only set of all the vertices
     */
    public Set<Vector2D> getAllVertices() {
        return blueprint.getAllVertices();
    }

    /**
     * @param vertex The vertex to add. No reference to the vector is kept.
     */
    public void addVertex(final Vector2D vertex) {
        blueprint.addVertex(vertex);
    }

    /**
     * Removes the last added vertex
     */
    public void removeVertex() {
        blueprint.removeVertex();
    }

    /**
     * @param vertex The vertex to move (equivalently the position of the vertex)
     * @param newPos The new position of the vertex
     */
    public void moveVertex(final Vector2D vertex, final Vector2D newPos) {
        blueprint.moveVertex(vertex, newPos);
    }

    /**
     * Adds a line segment at its index
     *
     * @param lineSegment The line segment (including its index) to add
     */
    public void addLineSegment(final IndexedLineSegment lineSegment) {
        blueprint.addLineSegment(lineSegment);
    }

    /**
     * Removes the line segment at the given index
     *
     * @param lineSegmentIndex The index of the line segment to remove
     */
    public void removeLineSegment(final int lineSegmentIndex) {
        blueprint.removeLineSegment(lineSegmentIndex);
    }

    /**
     * Gets the type of the line segment at the given index
     *
     * @param lineSegmentIndex The index of the line segment to get the type of
     */
    public LineSegmentType getLineSegmentType(final int lineSegmentIndex) {
        return blueprint.getLineSegmentType(lineSegmentIndex);
    }

    /**
     * Sets the type of the line segment at the given index
     *
     * @param lineSegmentIndex The index of the line segment to set the type of
     * @param type The type to set
     */
    public void setLineSegmentType(final int lineSegmentIndex, final LineSegmentType type) {
        blueprint.setLineSegmentType(lineSegmentIndex, type);
    }

    /**
     * @return A read-only view of the level's center of mass
     */
    public Vector2D getCenterOfMass() {
        return blueprint.getCenterOfMass();
    }

    /**
     * Sets the level's center of mass. No reference to the input vector is kept.
     *
     * @param centerOfMass The new position of the level's center of mass
     */
    public void setCenterOfMass(final Vector2D centerOfMass) {
        blueprint.setCenterOfMass(centerOfMass);
    }

    /**
     * @return A read-only view of the level's camera
     */
    public RectangularRegion getCamera() {
        return blueprint.getCamera();
    }

    /**
     * Sets the level camera. No reference to the input camera is kept.
     *
     * @param camera The new camera
     */
    public void setCamera(final RectangularRegion camera) {
        blueprint.setCamera(camera);
    }

    /**
     * @return Whether there exists a line segment with a start but without an end
     */
    public boolean hasIncompleteLineSegment() {
        return blueprint.hasIncompleteLineSegment();
    }

    /**
     * @return The start of the incomplete line segment, if it exists
     */
    public Optional<Vector2D> getIncompleteLineSegmentStart() {
        return blueprint.getIncompleteLineSegmentStart();
    }

    /**
     * @return An iterator over the full line segments, including their indicies
     */
    public Iterator<IndexedLineSegment> getLineSegmentIterator() {
        return blueprint.getLineSegmentIterator();
    }

    /**
     * Returns the read-only neighbours to the given vertex
     *
     * @param vertex The vertex to get the neighbours to
     * @return The neighbours to the vertex
     */
    public Set<Vector2D> getNeighboursTo(final Vector2D vertex) {
        return blueprint.getNeighboursTo(vertex);
    }

    /**
     * Returns the line segment that contains the closest point to the given point.
     * If no line segments exist, returns Optional.empty()
     *
     * @param point The reference point
     * @return The closest line segment, if any exists
     */
    public Optional<IndexedLineSegment> getClosestLineSegmentTo(final Vector2D point) {
        return blueprint.getClosestLineSegmentTo(point);
    }
}
