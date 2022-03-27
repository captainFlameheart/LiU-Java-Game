package se.liu.jonla400.project.math;

import java.awt.geom.Rectangle2D;

/**
 * Represents a rectangular region without any rotation. The region can be scaled and moved.
 */
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

    /**
     * Creates a RectangularRegion starting at a corner and extending with a size
     *
     * @param cornerPos A corner
     * @param size The size (can have negative components)
     * @return The created RectangularRegion
     */
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

    /**
     * Creates a RectangularRegion with the given coordinate ranges
     *
     * @param startToEndX The start and end x
     * @param startToEndY The start and end y
     * @return The created RectangularRegion
     */
    public static RectangularRegion createFromCoordinateRanges(final Interval startToEndX, final Interval startToEndY) {
        final Vector2D startPos = Vector2D.createCartesian(startToEndX.getStart(), startToEndY.getStart());
        final Vector2D size = Vector2D.createCartesian(startToEndX.getStartToEndDisplacement(), startToEndY.getStartToEndDisplacement());
        return createFromCornerAndSize(startPos, size);
    }

    /**
     * Creates a RectangularRegion with the given center and size
     *
     * @param center The center
     * @param size The size (can have negative components)
     * @return The created RectangularRegion
     */
    public static RectangularRegion createFromCenter(final Vector2D center, final Vector2D size) {
        final Vector2D cornerPos = center.subtract(size.getHalf());
        return createFromCornerAndSize(cornerPos, size);
    }

    /**
     * Creates a RectangularRegion with the given start and end corners
     *
     * @param start The start corner
     * @param end The end corner
     * @return The created RectangularRegion
     */
    public static RectangularRegion createFromCorners(final Vector2D start, final Vector2D end) {
        return createFromCoordinateRanges(new Interval(start.getX(), end.getX()), new Interval(start.getY(), end.getY()));
    }

    /**
     * @return Whether this region is considered to be invalid
     */
    public boolean isInvalid() {
        return bottomLeft == null || size == null || size.getX() < 0 || size.getY() < 0;
    }

    /**
     * @return A corresponding rectangle that can be drawn by a {@link java.awt.Graphics2D} object
     */
    public Rectangle2D convertToDrawableRect() {
        return new Rectangle2D.Double(bottomLeft.getX(), bottomLeft.getY(), size.getX(), size.getY());
    }

    /**
     * @return An interval starting at the left-most and ending at the right-most x coordinate
     */
    public Interval getLeftToRightX() {
        final double leftX = bottomLeft.getX();
        return new Interval(leftX, leftX + size.getX());
    }

    /**
     * @return An interval starting at the upper and ending at the lower y coordinate
     */
    public Interval getTopToBottomY() {
        final double bottomY = bottomLeft.getY();
        return new Interval(bottomY + size.getY(), bottomY);
    }

    /**
     * @return A read-only view of the bottom left position
     */
    public Vector2D getBottomLeft() {
        return bottomLeft.copy();
    }

    /**
     * @return A read-only view of the positive size
     */
    public Vector2D getSize() {
        return size.copy();
    }

    /**
     * @return A read-only view of the center
     */
    public Vector2D getCenter() {
        return bottomLeft.add(size.getHalf());
    }

    /**
     * @param deltaPos The change in position to apply
     */
    public void move(final Vector2D deltaPos) {
        bottomLeft.addLocally(deltaPos);
    }

    /**
     * Scales this region and retains its center position
     *
     * @param scale How much to scale
     */
    public void scale(final double scale) {
        set(createFromCenter(getCenter(), size.multiply(scale)));
    }

    private void set(RectangularRegion other) {
        bottomLeft.set(other.bottomLeft);
        size.set(other.size);
    }

    /**
     * @return A copy of this region
     */
    public RectangularRegion copy() {
        return new RectangularRegion(bottomLeft.copy(), size.copy());
    }
}
