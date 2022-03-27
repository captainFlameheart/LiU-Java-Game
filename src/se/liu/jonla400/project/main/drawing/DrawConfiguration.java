package se.liu.jonla400.project.main.drawing;

import java.awt.*;

/**
 * Specifies how graphical entities shared between a {@link se.liu.jonla400.project.main.game.LevelWorld}
 * and a {@link se.liu.jonla400.project.main.levelcreation.LevelCreator} are to be drawn. This makes
 * the level creator accurately display how the level will look when playing.
 */
public class DrawConfiguration
{
    private Color backgroundColor;
    private BallDrawer ballDrawer;
    private LineSegmentDrawer lineSegmentDrawer;
    private Drawer centerOfMassDrawer;

    /**
     * Creates a DrawConfiguration that specifies how the ball of the game, each line segment and
     * the center of mass of the level are to be drawn
     *
     * @param backgroundColor The background color of a world
     * @param ballDrawer How the ball is to be drawn, independent of the position and angle
     * @param lineSegmentDrawer How an arbitrary line segment is to be drawn
     * @param centerOfMassDrawer How the center of mass of a level is to be drawn, independent of its positon and angle
     */
    public DrawConfiguration(final Color backgroundColor, final BallDrawer ballDrawer, final LineSegmentDrawer lineSegmentDrawer,
			     final Drawer centerOfMassDrawer)
    {
	this.backgroundColor = backgroundColor;
	this.ballDrawer = ballDrawer;
	this.lineSegmentDrawer = lineSegmentDrawer;
	this.centerOfMassDrawer = centerOfMassDrawer;
    }

    /**
     * @return The background color of a world
     */
    public Color getBackgroundColor() {
	return backgroundColor;
    }

    /**
     * Returns a {@link Drawer} used to draw the ball with the given radius centered at (0, 0)
     *
     * @param radius The radius of the ball
     * @return The drawer used to draw the ball at (0, 0)
     */
    public Drawer getBallDrawer(final double radius) {
	return ballDrawer.setRadius(radius);
    }

    /**
     * Returns a {@link LineSegmentDrawer} used to draw a line segment
     *
     * @return The line segment drawer
     */
    public LineSegmentDrawer getLineSegmentDrawer() {
	return lineSegmentDrawer;
    }

    /**
     * Returns a {@link Drawer} used to draw the center of mass of a level
     *
     * @return The center of mass drawer
     */
    public Drawer getCenterOfMassDrawer() {
	return centerOfMassDrawer;
    }
}
