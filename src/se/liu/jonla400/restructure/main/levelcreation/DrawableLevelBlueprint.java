package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.constants.DrawConstants;
import se.liu.jonla400.restructure.constants.PhysicsConstants;
import se.liu.jonla400.restructure.main.RectangularRegion;
import se.liu.jonla400.restructure.main.drawing.CrossDrawer;
import se.liu.jonla400.restructure.math.Vector2D;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public class DrawableLevelBlueprint
{
    private LevelBlueprint blueprint;

    private DrawableLevelBlueprint(final LevelBlueprint blueprint) {
        this.blueprint = blueprint;
    }

    public static DrawableLevelBlueprint createFromBlueprint(final LevelBlueprint blueprint) {
        return new DrawableLevelBlueprint(blueprint);
    }

    public static DrawableLevelBlueprint createEmpty() {
        return new DrawableLevelBlueprint(LevelBlueprint.createEmpty());
    }

    public void draw(final Graphics2D g, final RectangularRegion drawRegion) {
        drawBackground(g, drawRegion);
        drawFullLineSegments(g);
        drawBall(g);
        drawCenterOfMass(g);
        drawLevelCamera(g);
    }

    private void drawBackground(final Graphics2D g, final RectangularRegion drawRegion) {
        g.setColor(Color.WHITE);
        g.fill(new Rectangle2D.Double(drawRegion.getLeftX(), drawRegion.getBottomY(), drawRegion.getWidth(), drawRegion.getHeight()));
    }

    private void drawFullLineSegments(final Graphics2D g) {
        g.setStroke(new BasicStroke(0.1f));

        final Iterator<IndexedLineSegment> lineSegmentIterator = getLineSegmentIterator();
        while (lineSegmentIterator.hasNext()) {
            final IndexedLineSegment lineSegment = lineSegmentIterator.next();
            final Vector2D start = lineSegment.getStart();
            final Vector2D end = lineSegment.getEnd();
            g.setColor(lineSegment.getType().getColor());
            g.draw(new Line2D.Double(start.getX(), start.getY(), end.getX(), end.getY()));
        }
    }

    private void drawBall(final Graphics2D g) {
        final Vector2D pos = PhysicsConstants.getBallSpawnPos();
        final double radius = PhysicsConstants.getBallRadius();
        final double diameter = 2 * radius;
        final Ellipse2D circle = new Ellipse2D.Double(pos.getX() - radius, pos.getY() - radius, diameter, diameter);
        g.setColor(DrawConstants.getBallFillColor());
        g.fill(circle);
        g.setColor(DrawConstants.getBallStrokeColor());
        g.setStroke(new BasicStroke(DrawConstants.getDefaultStrokeWidth()));
        g.draw(circle);
    }

    private void drawCenterOfMass(final Graphics2D g) {
        final Vector2D pos = blueprint.getCenterOfMass();
        final CrossDrawer drawerAtPos = CrossDrawer.createWithDefaultColor(1, 0.1f);
        final TranslatedDrawer drawer = new TranslatedDrawer(pos, drawerAtPos);
        drawer.draw(g);
    }

    private void drawLevelCamera(final Graphics2D g) {
        final CameraDrawer cameraDrawer = CameraDrawer.createDashed(blueprint.getCamera(), Color.BLACK, 0.1f);
        cameraDrawer.draw(g);
    }

    public LevelBlueprint getBlueprint() {
        return blueprint;
    }

    public Set<Vector2D> getAllVertices() {
        return blueprint.getAllVertices();
    }

    public Vector2D getVertex(final int index) {
        return blueprint.getVertex(index);
    }

    public void addVertex(final Vector2D vertex) {
        blueprint.addVertex(vertex);
    }

    public void removeVertex() {
        blueprint.removeVertex();
    }

    public void addLineSegment(final IndexedLineSegment lineSegment) {
        blueprint.addLineSegment(lineSegment);
    }

    public void removeLineSegment(final int lineSegmentIndex) {
        blueprint.removeLineSegment(lineSegmentIndex);
    }

    public LineSegmentType getType(final int lineSegmentIndex) {
        return blueprint.getType(lineSegmentIndex);
    }

    public void setType(final int lineSegmentIndex, final LineSegmentType type) {
        blueprint.setType(lineSegmentIndex, type);
    }

    public Vector2D getCenterOfMass() {
        return blueprint.getCenterOfMass();
    }

    public void setCenterOfMass(final Vector2D centerOfMass) {
        blueprint.setCenterOfMass(centerOfMass);
    }

    public RectangularRegion getCamera() {
        return blueprint.getCamera();
    }

    public void setCamera(final RectangularRegion camera) {
        blueprint.setCamera(camera);
    }

    public boolean hasIncompleteLineSegment() {
        return blueprint.hasIncompleteLineSegment();
    }

    public Optional<Vector2D> getIncompleteLineSegmentStart() {
        return blueprint.getIncompleteLineSegmentStart();
    }

    public Iterator<IndexedLineSegment> getLineSegmentIterator() {
        return blueprint.getLineSegmentIterator();
    }

    public Set<Vector2D> getNeighboursTo(final Vector2D vertex) {
        return blueprint.getNeighboursTo(vertex);
    }

    public Optional<IndexedLineSegment> getClosestLineSegmentTo(final Vector2D point) {
        return blueprint.getClosestLineSegmentTo(point);
    }
}
