package se.liu.jonla400.project.main;

import se.liu.jonla400.project.math.Interval;
import se.liu.jonla400.project.math.Vector2D;

public class DrawRegion
{
    private Vector2D bottomLeft;
    private Vector2D size;

    private DrawRegion(final Vector2D bottomLeft, final Vector2D size) {
        this.bottomLeft = bottomLeft;
        this.size = size;
    }

    public static DrawRegion create(final Vector2D cornerPos, final Vector2D size) {
        final PosAndDimensionPair leftXAndPositiveWidth = requirePositiveDimension(cornerPos.getX(), size.getX());
        final PosAndDimensionPair bottomYAndPositiveHeight = requirePositiveDimension(cornerPos.getY(), size.getY());

        final double leftX = leftXAndPositiveWidth.pos;
        final double positiveWidth = leftXAndPositiveWidth.dimension;
        final double bottomY = bottomYAndPositiveHeight.pos;
        final double positiveHeight = bottomYAndPositiveHeight.dimension;

        return new DrawRegion(Vector2D.createCartesianVector(leftX, bottomY), Vector2D.createCartesianVector(positiveWidth, positiveHeight));
    }

    private static PosAndDimensionPair requirePositiveDimension(final double pos, final double dimension) {
        if (dimension < 0) {
            return new PosAndDimensionPair(pos + dimension + 1, -dimension);
        } else {
            return new PosAndDimensionPair(pos, dimension);
        }
    }

    private static class PosAndDimensionPair {
        private double pos;
        private double dimension;

        private PosAndDimensionPair(final double pos, final double dimension) {
            this.pos = pos;
            this.dimension = dimension;
        }
    }

    public static DrawRegion createFromIntervals(final Interval startToEndX, final Interval startToEndY) {
        final Vector2D startPos = Vector2D.createCartesianVector(startToEndX.getStart(), startToEndY.getStart());
        final Vector2D size = Vector2D.createCartesianVector(startToEndX.getStartToEndDisplacement(), startToEndY.getStartToEndDisplacement());
        return create(startPos, size);
    }

    public static DrawRegion createFromCenter(final Vector2D center, final Vector2D size) {
        final Vector2D cornerPos = center.subtract(size.divide(2));
        return create(cornerPos, size);
    }

    public double getLeftX() {
        return bottomLeft.getX();
    }

    public double getRightX() {
        return bottomLeft.getX() + size.getX();
    }

    public double getBottomY() {
        return bottomLeft.getY();
    }

    public double getTopY() {
        return bottomLeft.getY() + size.getY();
    }

    public double getWidth() {
        return size.getX();
    }

    public double getHeight() {
        return size.getY();
    }

    public Vector2D getCenter() {
        return bottomLeft.add(size.divide(2));
    }
}
