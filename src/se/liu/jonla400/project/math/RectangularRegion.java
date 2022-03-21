package se.liu.jonla400.project.math;

public class RectangularRegion
{
    private Vector2D bottomLeft;
    private Vector2D size;

    private RectangularRegion() {
        // Used by gson
        bottomLeft = null;
        size = null;
    }

    private RectangularRegion(final Vector2D bottomLeft, final Vector2D size) {
        this.bottomLeft = bottomLeft;
        this.size = size;
    }

    public static RectangularRegion createFromCornerAndSize(final Vector2D cornerPos, final Vector2D size) {
        final PosAndDimensionPair leftXAndPositiveWidth = requirePositiveDimension(cornerPos.getX(), size.getX());
        final PosAndDimensionPair bottomYAndPositiveHeight = requirePositiveDimension(cornerPos.getY(), size.getY());

        final double leftX = leftXAndPositiveWidth.pos;
        final double positiveWidth = leftXAndPositiveWidth.dimension;
        final double bottomY = bottomYAndPositiveHeight.pos;
        final double positiveHeight = bottomYAndPositiveHeight.dimension;

        return new RectangularRegion(Vector2D.createCartesian(leftX, bottomY), Vector2D.createCartesian(positiveWidth, positiveHeight));
    }

    private static PosAndDimensionPair requirePositiveDimension(final double pos, final double dimension) {
        if (dimension < 0) {
            return new PosAndDimensionPair(pos + dimension, -dimension);
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

    public static RectangularRegion createFromCoordinateRanges(final Interval startToEndX, final Interval startToEndY) {
        final Vector2D startPos = Vector2D.createCartesian(startToEndX.getStart(), startToEndY.getStart());
        final Vector2D size = Vector2D.createCartesian(startToEndX.getStartToEndDisplacement(), startToEndY.getStartToEndDisplacement());
        return createFromCornerAndSize(startPos, size);
    }

    public static RectangularRegion createFromCenter(final Vector2D center, final Vector2D size) {
        final Vector2D cornerPos = center.subtract(size.divide(2));
        return createFromCornerAndSize(cornerPos, size);
    }

    public static RectangularRegion createFromCorners(final Vector2D start, final Vector2D end) {
        return createFromCoordinateRanges(new Interval(start.getX(), end.getX()), new Interval(start.getY(), end.getY()));
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

    public Vector2D getSize() {
        return size.copy();
    }

    public Vector2D getCenter() {
        return bottomLeft.add(size.divide(2));
    }

    public void move(final Vector2D deltaPos) {
        bottomLeft.addLocally(deltaPos);
    }

    public void scale(final double scale) {
        set(createFromCenter(getCenter(), size.multiply(scale)));
    }

    private void set(RectangularRegion other) {
        bottomLeft.set(other.bottomLeft);
        size.set(other.size);
    }

    public RectangularRegion copy() {
        return new RectangularRegion(bottomLeft.copy(), size.copy());
    }
}
