package se.liu.jonla400.project.main.drawing;

import java.awt.*;

/**
 * Draws the ball of this game onto a {@link Graphics2D} object at the origin. The radius
 * can be specified when drawing, which is useful if the radius can change at any time. The
 * radius can also be set if it is constant each time the ball is drawn.
 */
public class BallDrawer
{
    private CircleDrawer circleDrawer;
    private CrossDrawer crossDrawer;

    private BallDrawer(final CircleDrawer circleDrawer, final CrossDrawer crossDrawer) {
        this.circleDrawer = circleDrawer;
        this.crossDrawer = crossDrawer;
    }

    /**
     * Creates a BallDrawer with default settings
     *
     * @return The created BallDrawer
     */
    public static BallDrawer createDefault() {
        final Color fillColor = Color.RED;
        final Color strokeColor = Color.BLACK;
        final float strokeWidth = 0.1f;
        final CircleDrawer circleDrawer = CircleDrawer.create(fillColor, strokeColor, strokeWidth);

        final float crossStrokeWidth = 0.03f;
        final CrossDrawer crossDrawer = CrossDrawer.create(Color.BLACK, crossStrokeWidth);

        return new BallDrawer(circleDrawer, crossDrawer);
    }

    /**
     * Fixates the radius by returning a {@link Drawer} that can draw the ball with the given radius.
     * When the draw method of the {@link Drawer} is called, it is the same as calling the
     * {@link #draw(Graphics2D, double) draw} method of this BallDrawer with the given radius.
     *
     * @param radius The radius of the ball
     * @return The {@link Drawer} used to draw the ball with the given radius at (0, 0)
     */
    public Drawer setRadius(final double radius) {
        return g -> draw(g, radius);
    }

    /**
     * Draws the ball onto the {@link Graphics2D} object centered at (0, 0) and with the given radius
     *
     * @param g The graphics object to draw to
     * @param radius The radius of the ball
     */
    public void draw(final Graphics2D g, final double radius) {
        circleDrawer.draw(g, radius);
        crossDrawer.draw(g, 0.3 * radius);
    }
}
