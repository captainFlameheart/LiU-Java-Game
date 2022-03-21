package se.liu.jonla400.project.main.levelcreation;

import se.liu.jonla400.project.main.RectangularRegion;
import se.liu.jonla400.project.main.drawing.Drawer;

import java.awt.*;
import java.awt.geom.Rectangle2D;

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

    public static CameraDrawer createDashed(final RectangularRegion camera, final Color color, final float strokeWidth) {
	final float[] dash = {1};
	final BasicStroke stroke = new BasicStroke(
		strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, dash, 0);
	return new CameraDrawer(camera, color, stroke);
    }

    @Override public void draw(final Graphics2D g) {
	g.setColor(color);
	g.setStroke(stroke);
	g.draw(new Rectangle2D.Double(camera.getLeftX(), camera.getBottomY(), camera.getWidth(), camera.getHeight()));
    }
}
