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

public class DrawableLevelBlueprint
{
    private LevelBlueprint blueprint;
    private DrawConfiguration drawConfig;

    public DrawableLevelBlueprint(final LevelBlueprint blueprint, final DrawConfiguration drawConfig) {
        this.blueprint = blueprint;
        this.drawConfig = drawConfig;
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
            final LineSegmentDefinition lineSegment = lineSegmentIterator.next().removeIndex();
            drawConfig.getLineSegmentDrawer().draw(g, lineSegment);
        }
    }

    private void drawBall(final Graphics2D g) {
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

    private void drawLevelCamera(final Graphics2D g) {
        final CameraDrawer cameraDrawer = CameraDrawer.createDashed(blueprint.getCamera(), Color.BLACK, 0.1f);
        cameraDrawer.draw(g);
    }

    public LevelBlueprint getBlueprint() {
        return blueprint;
    }

    public DrawConfiguration getDrawConfig() {
        return drawConfig;
    }

    public Set<Vector2D> getAllVertices() {
        return blueprint.getAllVertices();
    }

    public void addVertex(final Vector2D vertex) {
        blueprint.addVertex(vertex);
    }

    public void removeVertex() {
        blueprint.removeVertex();
    }

    public void moveVertex(final Vector2D vertex, final Vector2D newPos) {
        blueprint.moveVertex(vertex, newPos);
    }

    public void addLineSegment(final IndexedLineSegment lineSegment) {
        blueprint.addLineSegment(lineSegment);
    }

    public void removeLineSegment(final int lineSegmentIndex) {
        blueprint.removeLineSegment(lineSegmentIndex);
    }

    public LineSegmentType getType(final int lineSegmentIndex) {
        return blueprint.getLineSegmentType(lineSegmentIndex);
    }

    public void setType(final int lineSegmentIndex, final LineSegmentType type) {
        blueprint.setLineSegmentType(lineSegmentIndex, type);
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
