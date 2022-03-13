package se.liu.jonla400.project.main;

import se.liu.jonla400.project.math.Vector2D;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class GameJComponent extends JComponent
{
    private Game game;

    private GameJComponent(final Game game) {
	this.game = game;
    }

    public static GameJComponent create(final Game game) {
	return new GameJComponent(game);
    }

    @Override protected void paintComponent(final Graphics g) {
	super.paintComponent(g);
	final Graphics2D g2d = (Graphics2D) g;
	drawGame(g2d);
    }

    private void flipGraphics(final Graphics2D g) {
	g.scale(1, -1);
	g.translate(0, -getHeight());
    }

    private void drawGame(final Graphics2D g) {
	final AffineTransform oldTransform = g.getTransform();

	flipGraphics(g);
	final DrawRegion enclosingRegion = encloseGameRegionWithCurrentAspectRatio();
	final double scale = getWidth() / enclosingRegion.getWidth();
	g.scale(scale, scale);
	g.translate(-enclosingRegion.getLeftX(), -enclosingRegion.getBottomY());
	game.draw(g, enclosingRegion);

	g.setTransform(oldTransform);
    }

    private DrawRegion encloseGameRegionWithCurrentAspectRatio() {
	final DrawRegion gameRegion = game.getMinDrawRegion();
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

	return DrawRegion.createFromCenter(gameRegion.getCenter(), Vector2D.createCartesianVector(enclosingWidth, enclosingHeight));
    }
}
