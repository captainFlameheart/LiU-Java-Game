package se.liu.jonla400.project.main.drawing;

import se.liu.jonla400.project.math.RectangularRegion;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Draws a camera specified as a {@link RectangularRegion}. This is used by the
 * {@link se.liu.jonla400.project.main.levelcreation.LevelCreator} to display where the camera of a
 * {@link se.liu.jonla400.project.main.game.LevelWorld} will start.
 */
public class CameraDrawer implements Drawer
{
    private RectangularRegion camera;
    private Color color;
    private BasicStroke stroke;

    private CameraDrawer(final RectangularRegion camera, final Color color, final BasicStroke stroke) {
	this.camera = camera;
	this.color = color;
	this.stroke = stroke;
    }

    /**
     * Creates a CameraDrawer for the given camera that draws the camera with a dashed outline.
     * The length of the dashes are left unspecified.
     *
     * @param camera The camera to draw
     * @param color The color of the dashed outline
     * @param strokeWidth The width of the dashed outline
     * @return The created CameraDrawer
     */
    public static CameraDrawer createDashed(final RectangularRegion camera, final Color color, final float strokeWidth) {
	final float dashLength = 1;
	final BasicStroke stroke = new BasicStroke(
		strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[]{dashLength}, 0);
	return new CameraDrawer(camera, color, stroke);
    }

    /**
     * Draws the camera onto the given {@link Graphics2D} object
     *
     * @param g The graphics object to draw to
     */
    @Override public void draw(final Graphics2D g) {
	g.setColor(color);
	g.setStroke(stroke);
	g.draw(camera.convertToDrawableRect());
    }
}
