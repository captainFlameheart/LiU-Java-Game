package se.liu.jonla400.restructure.main.levelcreation;

import se.liu.jonla400.restructure.main.DrawRegion;
import se.liu.jonla400.restructure.main.Level;
import se.liu.jonla400.restructure.math.Interval;
import se.liu.jonla400.restructure.math.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class LevelCreatorJComponent extends JComponent
{
    private LevelCreator1 levelCreator;

    private LevelCreatorJComponent(final LevelCreator1 levelCreator) {
	this.levelCreator = levelCreator;
    }

    public static LevelCreatorJComponent create(final LevelCreator1 levelCreator) {
	return new LevelCreatorJComponent(levelCreator);
    }

    public Vector2D convertToGamePoint(final Vector2D point) {
	final Interval xInterval = new Interval(0, getWidth());
	final Interval yInterval = new Interval(0, getHeight());

	final DrawRegion gameRegion = encloseGameWithCurrentAspectRatio();
	final Interval gameXInterval = gameRegion.getMinToMaxX();
	final Interval gameYInterval = gameRegion.getMinToMaxY();

	return Vector2D.createCartesian(
		xInterval.mapValueToOtherInterval(point.getX(), gameXInterval),
		yInterval.mapValueToOtherInterval(point.getY(), gameYInterval)
	);
    }

    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	final Graphics2D g2d = (Graphics2D) g;
	drawGame(g2d);
    }

    private void drawGame(final Graphics2D g) {
	final AffineTransform oldTransform = g.getTransform();

	flipGraphics(g);
	final DrawRegion drawRegion = encloseGameWithCurrentAspectRatio();
	final double scale = getWidth() / drawRegion.getWidth();
	g.scale(scale, scale);
	g.translate(-drawRegion.getLeftX(), -drawRegion.getBottomY());
	levelCreator.draw(g, drawRegion);

	g.setTransform(oldTransform);
    }

    private void flipGraphics(final Graphics2D g) {
	g.scale(1, -1);
	g.translate(0, -getHeight());
    }

    private DrawRegion encloseGameWithCurrentAspectRatio() {
	final DrawRegion gameRegion = levelCreator.getCamera();
	final double gameWidth = gameRegion.getWidth();
	final double gameHeight = gameRegion.getHeight();

	final double gameWidthToHeightRatio = gameWidth / gameHeight;
	final double targetWidthToHeightRatio = (double) getWidth() / getHeight();

	final double enclosingWidth;
	final double enclosingHeight;
	if (gameWidthToHeightRatio > targetWidthToHeightRatio) {
	    enclosingWidth = gameWidth;
	    enclosingHeight = enclosingWidth / targetWidthToHeightRatio;
	} else {
	    enclosingHeight = gameHeight;
	    enclosingWidth = enclosingHeight * targetWidthToHeightRatio;
	}

	return DrawRegion.createFromCenter(gameRegion.getCenter(), Vector2D.createCartesian(enclosingWidth, enclosingHeight));
    }
}
